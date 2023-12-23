package com.gujs.base
import scala.reflect.runtime._
import scala.tools.reflect._
class BaseTest1 {
  def print1(): Unit = {
    print("111111")
  }
}

object BaseTest1 {
  def testD(d:Double): Unit ={
    print(d)
  }
  def main(args: Array[String]): Unit = {
    println(Option(1).exists(_ == 1))
    println(Option(1).exists(_ == 2))
    println(Option(1).exists(_ => true))
    println(None.exists(_ == 1))
    println(None.exists(_ => true))
//    println(5.555d.toInt)

  }
}

case class Student(name:String,score:Int){
  def test2(a:String){
    return f"${a}"
  }
}