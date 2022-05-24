package com.gujs.base

class testScala1 {

}

object Hello extends App {
  println("hello world")
  val i = 10
  val r = i match {
    case 1 => "one"
    case 2 => "two"
    case 10 => "ten"
    case 20 => "twenty"
  }
  println(r)
}
object ForHello extends App {
  new BaseTest1() .print1()


}