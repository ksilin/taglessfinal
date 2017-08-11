package com.example.taglessfinal

trait ProductRepo[M[_]] {
  def findProduct(id: ProductId): M[Option[Product]]
  def storeProduct(p: Product): M[Unit]
  def incrementProductSales(productId: ProductId, amount: Int): M[Unit]
}
