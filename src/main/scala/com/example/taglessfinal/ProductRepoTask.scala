package com.example.taglessfinal

import monix.eval.Task

class ProductRepoTask extends ProductRepo[Task] {
  override def findProduct(id: ProductId): Task[Option[Product]] =
    Task.now(Some(Product(id, "found")))

  override def storeProduct(p: Product): Task[Unit] = Task.unit

  override def incrementProductSales(productId: ProductId, amount: Int): Task[Unit] =
    Task.unit
}
