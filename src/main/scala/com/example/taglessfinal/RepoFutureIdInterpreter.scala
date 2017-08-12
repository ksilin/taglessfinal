package com.example.taglessfinal

import cats.Id

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.example.taglessfinal.ImplicitConversions.IdToFuture

class RepoFutureIdInterpreter(logger: Logger[Id]) extends ProductRepo[Future] {

  override def findProduct(id: ProductId): Future[Option[Product]] =
    for {
      _    <- logger.info(s"looking for product $id").toFuture
      prod <- Future.successful(Some(Product(id, "default")))
    } yield prod

  override def storeProduct(p: Product): Future[Unit] =
    for {
      _ <- logger.info(s"storing product $p").toFuture
//      prod <- Future.successful(())
    } yield ()

  override def incrementProductSales(productId: ProductId, amount: Int): Future[Unit] =
    for {
      _ <- logger.info(s"incrementing sales for $productId").toFuture
//      prod <- Future.successful(())
    } yield ()
}
