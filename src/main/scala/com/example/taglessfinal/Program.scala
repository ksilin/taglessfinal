package com.example.taglessfinal

import cats.implicits._
import cats.Monad

class Program[M[_]: Monad](repo: ProductRepo[M]) {

  def renameProduct(id: ProductId, name: String): M[Option[Product]] =
    repo.findProduct(id).flatMap {
      case Some(p) =>
        val renamed = p.copy(name = name)
        repo.storeProduct(renamed) map (_ => Some(renamed))
      case None => Monad[M].pure(None)
    }

  val products = List[Product]()
  val sellOneOfEverything: M[List[Unit]] =
    Monad[M].traverse(products)(p => repo.incrementProductSales(p.id, 1))

}
