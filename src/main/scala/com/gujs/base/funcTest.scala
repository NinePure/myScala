package com.gujs.base

import scala.util.Random

object funcTest extends App {
  private var aaa: Map[Int, Int] = Map(1 -> 1, 2 -> 1, 3 -> 1)
  println(aaa)
  aaa -= 1
  println(aaa)
  aaa -= 1
  aaa -= 1
  println(aaa)
  println(Seq(3,2,3,4,5).drop(1))
  println(Set.empty.subsetOf(Set.empty))
  println(Set(2,3).subsetOf(Set(1,2,3,4,5)))
  val map = Map(1->2)
  println(map.updated(2,3))
  val actionIterator: Iterator[Int] = Seq(1,2,3,4,5,6).iterator
  var action = 0
  var bool = false

  while (!bool && actionIterator.hasNext) {
    action = actionIterator.next()
    bool = action > 30
    println(action)
  }
  println(action + "```````````````")

/*  private val list: List[TestSortClass] =
    List(TestSortClass(5,2,3),TestSortClass(2,1,3),TestSortClass(2,2,1),TestSortClass(2,1,5))

  println(list.sortBy(v=> (-v.usedRowsNumber, v.initReelX, v.initLocation)))*/
/*
  def b(age1:Int,name1:String,score1:Int): Boolean = {
    age1 > 10 && name1 == "xiaoming" && score1 > 60
  }
  val age = 15
  val name = "xiaoming"
  private val intToBoolean: Int => Boolean = score => b(age, name, score)
  funcTest().a(intToBoolean)
*/


}
case class TestSortClass(usedRowsNumber:Int, initReelX:Int, initLocation:Int)
case class funcTest(){
  def a(intToBoolean: Int => Boolean): Unit = {
    println(intToBoolean(79))
    println(intToBoolean(44))
  }
}