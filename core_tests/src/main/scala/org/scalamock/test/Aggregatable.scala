package org.scalamock.test

trait Aggregatable[+A] {

  def aggregate[B](z: B)(seqop: (B, A) => B, combop: (B, B) => B): B
}

object Test {
  import org.scalamock.Implement._
  trait Foo { def m[A](x: A, y: Double): String }
  implement[Foo]
}