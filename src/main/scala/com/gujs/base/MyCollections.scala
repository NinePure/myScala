package com.gujs.base

import scala.collection.mutable.ArrayBuffer

class MyCollections {}

object ArrayBufferTest extends App {
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
}
