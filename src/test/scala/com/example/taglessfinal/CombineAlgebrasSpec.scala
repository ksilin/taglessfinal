package com.example.taglessfinal

import cats.Id
import org.scalatest.{ FreeSpec, MustMatchers }
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CombineAlgebrasSpec extends FreeSpec with MustMatchers {

  "logger and repo with same monad (Future) - needs only flatMap" in {

    val futureLogger: Logger[Future] =
      new Logger[Future] {
        override def info(msg: => String): Future[Unit] =
          Future.successful(println(s"[INFO ] $msg"))

        override def error(msg: => String): Future[Unit] =
          Future.successful(println(s"[ERROR] $msg"))
      }
    val futureInterpreter = new RepoFutureInterpreter(futureLogger)

    val prog     = new Program[Future](futureInterpreter)
    val doRename = prog.renameProduct(ProductId("ef"), "sdf")
    println(doRename)
  }

  "logger and repo with different monads" in {

    val idLogger: Logger[Id] = new Logger[Id] {
      override def info(msg: => String): Id[Unit]  = println(s"[INFO ] $msg")
      override def error(msg: => String): Id[Unit] = println(s"[ERROR ] $msg")
    }

    // now we need to convert from one monad into an other
    val futureIdInterpreter = new RepoFutureIdInterpreter(idLogger)
    val prog                = new Program[Future](futureIdInterpreter)
    val doRename            = prog.renameProduct(ProductId("ef"), "sdf")
    println(doRename)

  }

}
