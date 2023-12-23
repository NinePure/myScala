package com.gujs.base

import scala.collection.mutable.ArrayBuffer

class MyCollections {}

case class A(a:Float)
object ArrayBufferTest extends App {
  println(BigDecimal(10000.5f))
  println(BigDecimal(100.05f))
  println(BigDecimal.decimal(100.05f))
  println(BigDecimal(0.0005f))
  println(BigDecimal(0.00005f))
  println(BigDecimal(0.0000005f))
  println(BigDecimal.decimal(100000000.005f))



  println(BigDecimal.decimal(15.6666776).toString())
  println(Seq(1,2,3,4).filter(_<4))
  println( (0 until 3).flatMap(row => (0 until 5).map(col => (row, col))))
  println((50000 * 1.8f).toLong)
  println((9000000f * 155.5f).toLong)
  println((38000000 * BigDecimal(5000f)).toLong)
  private val seq = Seq(2, 3, 4, 5, 6, 7)
  private val i: Int = seq.indexWhere(_ == 4)
  println(i)
  println(seq.patch(i, Seq(14, 15), 1))

  println(Map(1 -> 2, 2 -> 5, 3 -> 3, 4 -> 1).values.product) // 乘积
//  var cycleTimes = 10
//  while(cycleTimes > 0) {
//    println(cycleTimes)
//    cycleTimes-=1
//  }
//  println(BigDecimal(0.0) == 0)
//  println(BigDecimal(0.0).equals(0))
//  println(BigDecimal(0.0).equals(BigDecimal(0)))
//  val ints = ArrayBuffer[Int]()
//  val names = ArrayBuffer[String]()
//  ints += 1
//  ints += 2
//  ints += 2
//  ints += 1
//  ints += 10
//  println(ints)
//  ints -= 1
//  ints --= Array(2,10)
//  println(ints)
//  println(Seq(1,2,3).foldLeft(Seq(0)){
//    (seq,item) =>
//      seq ++ Seq(item)
//  })
//  println(Seq.fill(4)(5))
//  private val value: Seq[(Int, Int)] = Seq(1 -> 100, 2 -> 100, 3 -> 100, 1 -> 200).map(v => v._1 -> v._2)
//  println(value)
//  println(value.toMap)


  var map = Map(1 -> 100,2 -> 300)
  map = map +  (1->500)
  println(map)
  map = map - 1
  println(map)
  map = map + (1->300, 3-> 700)
  println(map)
  map = map ++ Map(4 -> 100,21 -> 300)
  println(map)
}
