package com.example.taglessfinal

import org.scalatest.{ FreeSpec, MustMatchers }
import cats.implicits._

import monix.cats._
import monix.eval.Task

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

class ProductRepoSpec extends FreeSpec with MustMatchers {

  "must run future-based program" in {

    val futureRepo = new ProductRepo[Future] {
      override def findProduct(id: ProductId): Future[Option[Product]] =
        Future.successful(Some(Product(id, "found")))

      override def storeProduct(p: Product): Future[Unit] = Future.successful(())

      override def incrementProductSales(productId: ProductId, amount: Int): Future[Unit] =
        Future.successful(())
    }

    val prog                               = new Program[Future](futureRepo)
    val getResult: Future[Option[Product]] = prog.renameProduct(ProductId("abc"), "someName")
    val res                                = Await.result(getResult, 10.seconds)
    println(res)
  }

  "must run task-based program" in {
    // does not work with regular scala Future, conflicts with the default implicit EC
    import monix.execution.Scheduler.Implicits.global

    val taskRepo = new ProductRepo[Task] {
      override def findProduct(id: ProductId): Task[Option[Product]] =
        Task.now(Some(Product(id, "found")))

      override def storeProduct(p: Product): Task[Unit] = Task.unit

      override def incrementProductSales(productId: ProductId, amount: Int): Task[Unit] =
        Task.unit
    }
    val prog                             = new Program[Task](taskRepo)
    val getResult: Task[Option[Product]] = prog.renameProduct(ProductId("abc"), "someName")
    val res = Await.result(getResult.runAsync, 10.seconds)
    println(res)
  }

}
