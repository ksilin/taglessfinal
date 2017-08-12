package com.example.taglessfinal

import cats.Id
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.example.taglessfinal.ImplicitConversions.NaturalTransformation
import com.example.taglessfinal.ImplicitConversions.idToMonad

class RepoFutureIdInterpreter(logger: Logger[Id]) extends ProductRepo[Future] {

  override def findProduct(id: ProductId): Future[Option[Product]] =
    for {
      _    <- logger.info(s"looking for product $id").liftTo[Future] //toFuture
      prod <- Future.successful(Some(Product(id, "default")))
    } yield prod

  override def storeProduct(p: Product): Future[Unit] =
    for {
      _ <- logger.info(s"storing product $p").liftTo[Future] //#(idToMonad) //toFuture
//      prod <- Future.successful(())
    } yield ()

  override def incrementProductSales(productId: ProductId, amount: Int): Future[Unit] =
    for {
      _ <- logger.info(s"incrementing sales for $productId").liftTo[Future] //toFuture
//      prod <- Future.successful(())
    } yield ()
}
