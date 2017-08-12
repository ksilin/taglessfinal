package com.example.taglessfinal

import cats.Id

import scala.concurrent.Future

object ImplicitConversions {

  implicit class IdToFuture[A](id: Id[A]) {
    def toFuture: Future[A] = Future.successful(id)
  }

}
