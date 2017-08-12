package com.example.taglessfinal

trait Logger[M[_]] {

  def info(msg: => String): M[Unit]

  def error(msg: => String): M[Unit]

}
