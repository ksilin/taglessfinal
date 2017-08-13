package com.example.taglessfinal

object WarehouseTraits {

  trait Stock[F[_]] {
    def purchaseOrderContent(id: PurchaseOrderId): F[Map[ProductId, Int]]
    def createStock(productId: ProductId, localtionId: LocationId, quantity: Int): F[Unit]
    def moveStock(productId: ProductId,
                  source: LocationId,
                  target: LocationId,
                  quantity: Int): F[Unit]
  }

  trait Inventory[F[_]] {
    def increment(locationId: LocationId, productId: ProductId, quantity: Int): F[Unit]
    def decrement(locationId: LocationId, productId: ProductId, quantity: Int): F[Unit]
  }

  trait Storage[F[_]] {
    def get[K, V](get: K): F[Option[V]]
    def put[K, V](k: K, v: V): F[Unit]
  }

}
