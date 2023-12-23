package com.gujs.base

import scala.:+

object CollectionsTest1 extends App {
  val seq = Seq(Seq(1,2), Seq(5,6), Seq(11,21))

  val seqResult = seq.foldLeft(Seq(Seq.empty[Int])) { (acc, elem) =>
    acc.flatMap(innerSeq => elem.flatMap(innerElem => Seq(innerSeq.appended(innerElem) )))
  }

  println(seqResult)

  Seq(Seq(1,5),Seq(2,5),Seq(1,6),Seq(2,6))

  (0 to 30).foreach(n => println(n % 10 + 1))
  val nums1 = List.range(0, 10)
  val nums2 = (1 to 10 by 2).toList
  val letters3 = ('a' to 'f').toList
  val letters4 = ('a' to 'f' by 2).toList
  println(nums1)
  println(nums2)
  println(letters3)
  println(letters4)

  val nums5 = (1 to 10).toList
  val names6 = List("joel", "ed", "chris", "maurice")
  println(nums5)
  println(names6)
  names6.foreach(println)
  nums5.filter(_ < 6).foreach(println)
}

object CollectionsTest2 extends App{
  println(BigDecimal(-1000)* BigDecimal(-200)<0)
  println(BigDecimal(1000)* BigDecimal(200)<0)
  println(BigDecimal(-1000)* BigDecimal(200)<0)
  println(BigDecimal(1000)* BigDecimal(-200)<0)


  def test(): (String,Int,Double,String,String,Char) = {
    ("1",2,3.0,"4","123",'a')
  }
  println(test())
  println(test()._2)
  println(Sunday)

}

sealed trait DayOfWeek
case object Sunday extends DayOfWeek
case object Monday extends DayOfWeek
case object Tuesday extends DayOfWeek
case object Wednesday extends DayOfWeek
case object Thursday extends DayOfWeek
case object Friday extends DayOfWeek
case object Saturday extends DayOfWeek





