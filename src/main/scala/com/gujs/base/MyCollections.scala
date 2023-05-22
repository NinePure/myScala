package com.gujs.base

import scala.collection.mutable.ArrayBuffer

class MyCollections {}

object ArrayBufferTest extends App {
  var cycleTimes = 10
  while(cycleTimes > 0) {
    println(cycleTimes)
    cycleTimes-=1
  }
  println(BigDecimal(0.0) == 0)
  println(BigDecimal(0.0).equals(0))
  println(BigDecimal(0.0).equals(BigDecimal(0)))
  val ints = ArrayBuffer[Int]()
  val names = ArrayBuffer[String]()
  ints += 1
  ints += 2
  ints += 2
  ints += 1
  ints += 10
  println(ints)
  ints -= 1
  ints --= Array(2,10)
  println(ints)

  var map = Map(1 -> 100,2 -> 300)
  map = map +  (1->500)
  println(map)
  map = map - 1
  println(map)
  map = map + (1->300, 3-> 700)
  println(map)
}
