package com.gujs.base

class MyAbstract {

}
abstract class Pet (name: String) {
  def speak: Unit = println(s"My name is $name")
}

class Dog1(name: String) extends Pet(name)

object test extends App{
  val d = new Dog1("Fido")
  d.speak
}
