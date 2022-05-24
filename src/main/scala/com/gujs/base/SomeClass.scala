package com.gujs.base

import com.gujs.base.SomeClass.HiddenFilename

class SomeClass {
  var name = "gujs"
  def printFilename() = {
    println(s"$HiddenFilename ,name:${name}")
  }
}

object SomeClass {
  private val HiddenFilename = "/tmp/foo.bar"
  def apply(name:String) : SomeClass = {
    var someClass = new SomeClass
    someClass.name = name
    someClass
  }
}

object testSomClass extends App{
  (new SomeClass).printFilename()
  SomeClass("123111").printFilename()
}