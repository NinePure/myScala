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
    testD(5.0f)
    //    val names = ArrayBuffer[Option[String]]()
    //    names+= Some("money")
    //    names+= Some("max")
    //    println(names)
    //    names.foreach {
    //      case e@Some("money") => println(e.get)
    //      case e@_=>println("2222"+e.get)
    //    }


    //    def test3() = {println(123)}
    //    val template = "hi %s,今天是星期%s，去了%s"
    //    println(template.format("你好,五,动物园".split(",").array: _*))
    //    //    println(template.format("你好","五","动物园"))
    //    test3()
    //    val builder = List.newBuilder[String]
    //    builder.addOne("123")
    //    builder.addOne("345")
    //    println(builder.result())
    //    val filterParams = "profitMin,20;profitMax,800;moneyMin,20;moneyMax,1000;rateLimit,5"
    //    println(filterParams.split(";").apply(0))
    //    println(filterParams.split(";").apply(2))
    //    filterParams.split(";").foreach(e => e.split(",").foreach(a => println(a)))


    //    val tb = currentMirror.mkToolBox()
    //    val template = "${hannah.name} has a score of ${hannah.score}"
    //    val hannah = Student("Hannah",95)
    //    def defFormat(): String = {
    //      val code =
    //        (s"""
    //           |import com.gujs.base._
    //           |(hannah:Student) => hannah.test2(template)""").stripMargin
    //      tb.eval(tb.parse(code)).asInstanceOf[Student => String](hannah)
    //    }
    //    def test(hannah:Student){
    //      hannah.test2(template)
    //    }
    //    println(defFormat())
  }
}

case class Student(name:String,score:Int){
  def test2(a:String){
    return f"${a}"
  }
}