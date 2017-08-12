package com.example.taglessfinal

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RepoFutureInterpreter(logger: Logger[Future]) extends ProductRepo[Future] {

  override def findProduct(id: ProductId): Future[Option[Product]] =
    for {
      _    <- logger.info(s"looking for product $id")
      prod <- Future.successful(Some(Product(id, "default")))
    } yield prod

  override def storeProduct(p: Product): Future[Unit] =
    for {
      _ <- logger.info(s"storing product $p")
//      prod <- Future.successful(())
    } yield ()

  override def incrementProductSales(productId: ProductId, amount: Int): Future[Unit] =
    for {
      _ <- logger.info(s"incrementing sales for $productId")
//      prod <- Future.successful(())
    } yield ()
}
