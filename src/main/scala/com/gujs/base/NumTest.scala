package com.gujs.base

import java.math.RoundingMode
import scala.concurrent.Await
object ObjectTest {
  var ratio = 0.1f
}
case class aaa(a:Int)
object NumTest extends App {
  println(aaa(1)==aaa(1))
  println(Map(aaa(1) -> 2).get(aaa(1)))
//  println((50000 * 1.8f).toLong)
//  println(BigDecimal.decimal(1.8f))
//  println(5000000 * BigDecimal.decimal(1.8f))
  println(BigDecimal.decimal(1.2345f))
  println(BigDecimal.apply(1.2345f))
  println(1.2345f.toDouble)

  println(500 * BigDecimal.decimal(1.8f))

}