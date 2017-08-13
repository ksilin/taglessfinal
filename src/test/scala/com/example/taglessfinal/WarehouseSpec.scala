package com.example.taglessfinal

import cats.{ Id, Monad, ~> }
import cats.implicits._
import com.example.taglessfinal.WarehouseTraits.{ Inventory, Storage }
import org.scalatest.{ FreeSpec, MustMatchers }
import com.example.taglessfinal.ImplicitConversions.NaturalTransformation

import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

class WarehouseSpec extends FreeSpec with MustMatchers {

  // https://gist.github.com/btlines/2403b10db3cd140423e4b8666c03b45f

  val idLogger: Logger[Id] = new Logger[Id] {
    override def info(msg: => String): Id[Unit] = println(s"[INFO ] $msg")

    override def error(msg: => String): Id[Unit] = println(s"[ERROR ] $msg")
  }

  val kvStore = new Storage[Future] {
    var store = Map.empty[Any, Any]

    override def get[K, V](k: K): Future[Option[V]] =
      Future.successful(store.get(k).asInstanceOf[Option[V]])

    override def put[K, V](k: K, v: V): Future[Unit] = Future.successful(store += k -> v)
  }

  def inventory[F[_], G[_], H[_]](
      logger: Logger[F],
      store: Storage[G]
  )(implicit storeM: Monad[G], fToG: F ~> G, gToH: G ~> H) = new Inventory[H] {

    private def stockAt(locationId: LocationId, productId: ProductId): G[Int] = {
      val found: G[Option[Int]] = store.get[(LocationId, ProductId), Int]((locationId, productId))
      //found.map(_.getOrElse(0)) // same as
      storeM.map(found)(_.getOrElse(0))
    }

    override def increment(locationId: LocationId, productId: ProductId, quantity: Int): H[Unit] = {
      //      import cats.Monad.ops._
      for {
        _        <- logger.info(s"add $quantity $productId at $locationId").liftTo[G]
        existing <- stockAt(locationId, productId)
        inc      <- store.put((locationId, productId), existing + quantity)
      } yield inc
    }.liftTo[H]

    override def decrement(locationId: LocationId, productId: ProductId, quantity: Int): H[Unit] = {
      //      import cats.Monad.ops._
      for {
        _        <- logger.info(s"remove $quantity $productId at $locationId").liftTo[G]
        existing <- stockAt(locationId, productId)
        dec      <- store.put((locationId, productId), existing - quantity)
      } yield dec
    }.liftTo[H]
  }

  "simple test" in {

    // the Monad[Future] instance is provided by cats
    // nat transform Id ~> Future
    // nat transform Future ~> List

//    implicit def identityTransform[T[_]] = new (T ~> T) {
//      override def apply[A](fa: T[A]): T[A] = fa
//    }
    implicit def monadTransform[T[_]: Monad] = new (Id ~> T) {
      override def apply[A](a: Id[A]): T[A] = Monad[T].pure(a)
    }

    implicit def futureListTransform[T[_]: Monad] = new (Future ~> T) {
      override def apply[A](a: Future[A]): T[A] = Monad[T].pure(Await.result(a, Duration.Inf))
    }
    // our F is Id
    // our G is Future
    // our H is List
    val inv = inventory[Id, Future, List](idLogger, kvStore)

    val location = LocationId("123")
    val prodId   = ProductId("123")

    val inc: List[Unit] = inv.increment(location, prodId, 10)
    println(inc)

  }

}
