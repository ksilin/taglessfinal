package com.example

package object taglessfinal {

  type Traversable[+A] = scala.collection.immutable.Traversable[A]
  type Iterable[+A]    = scala.collection.immutable.Iterable[A]
  type Seq[+A]         = scala.collection.immutable.Seq[A]
  type IndexedSeq[+A]  = scala.collection.immutable.IndexedSeq[A]

  case class PurchaseOrderId(value: String)
  case class ProductId(value: String)
  case class LocationId(value: String)
  case class Product(id: ProductId, name: String)
}
