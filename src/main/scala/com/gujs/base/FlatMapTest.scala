package com.gujs.base

object FlatMapTest extends App {
  val a = Seq(Seq(0), Seq(1), Seq(2, 2))
  val b = Seq(a,a,a)
  println(b.flatten.flatten)
  println( b.flatten.flatten.flatMap(row => (0 until 5).map(col => (row, col))))
  println("------------------------")
  println( (0 to 3).flatMap(row => (0 until 5).map(col => (row, col))))
  println("------------------------")
  println("------------------------")
  val seq = Seq(Seq(1,2), Seq(5,6), Seq(11,21))

  val seqResult = seq.foldLeft(Seq(Seq.empty[Int])) { (acc, elem) =>
    acc.flatMap(innerSeq => elem.flatMap(innerElem => Seq(innerSeq.appended(innerElem) )))
  }
//  Seq(Seq(1),Seq(2))
//  Seq(Seq(1, 5),Seq(1, 6),Seq(2,5),Seq(2,6))
//  Seq(Seq(1,5,11),Seq(1,5,21),Seq(1,6,11),Seq(1,6,21),Seq(2,5,11),Seq(2,5,21),Seq(2,6,11),Seq(2,6,21))

  println(seqResult)

}
