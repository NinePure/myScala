package com.gujs.base

trait MyTrait {

  def trait1(): Unit
  def trait2(): Unit
}
trait MyTrait2 {

  def trait3(): String
  def trait4(): Int
}

class Cat extends MyTrait with MyTrait2 {
  override def trait1(): Unit = println("cat eat fish")

  override def trait2(): Unit = println("cat 2")

  override def trait3(): String = "trait3"

  override def trait4(): Int = 111
}
class Dog{

}
object TestTrait extends App{
  println("begin")
  val cat = new Cat
  cat.trait2()
  cat.trait1()
  println(cat.trait3())
  println(cat.trait4())
  val dog = new Dog with MyTrait {
    override def trait1(): Unit = println(this)

    override def trait2(): Unit = println(123)
  }
  dog.trait1()
  dog.trait2()
}