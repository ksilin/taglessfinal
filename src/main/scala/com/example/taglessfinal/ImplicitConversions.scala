package com.example.taglessfinal

import cats.{ Id, Monad }

import scala.concurrent.Future

object ImplicitConversions {

  implicit class IdToFuture[A](id: Id[A]) {
    def toFuture: Future[A] = Future.successful(id)
  }

  import cats.~>
  implicit class NaturalTransformation[F[_], A](fa: F[A]) {
    def liftTo[G[_]](implicit transform: F ~> G): G[A] =
      transform(fa)
  }

  implicit def idToMonad[M[_]: Monad] = new (Id ~> M) {
    override def apply[A](a: Id[A]): M[A] = Monad[M].pure(a)
  }

}
