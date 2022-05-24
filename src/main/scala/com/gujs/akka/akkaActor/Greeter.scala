package com.gujs.akka.akkaActor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.gujs.akka.akkaActor.GreeterMain.SayHello

object Greeter{
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])

  final case class Greeted(whom: String, from: ActorRef[Greet])

  var num = 3

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    println(context)
    println("Greeter apply" + Thread.currentThread())
    context.log.info("Hello {}!", message.whom)
    println(s"Hello ${message.whom}!" + Thread.currentThread())
    message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}

object GreeterBot {

  def apply(max: Int): Behavior[Greeter.Greeted] = {
    println("bot apply" + Thread.currentThread())
    bot(0, max)
  }

  private def bot(greetingCounter: Int, max: Int): Behavior[Greeter.Greeted] = {
    println("bot begin" + Thread.currentThread())
    Behaviors.receive { (context, message) =>
      println(context)
      val n = greetingCounter + 1
//      context.log.info("Greeting {} for {}", n, message.whom)
      println(s"Greeting ${n} for ${message.whom}" + Thread.currentThread())
      if (n == max) {
        Behaviors.stopped
      } else {
        message.from ! Greeter.Greet(message.whom, context.self)
        bot(n, max)
      }
    }
  }
}

object GreeterMain {
  sealed trait Command
  final case class SayHello(name: String) extends Command
  final case class SayHello1(name: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      println(context)
      val greeter = context.spawn(Greeter(), "greeter")
      Behaviors.receiveMessage {
        case SayHello(message) => {
          val replyTo = context.spawn(GreeterBot(max = 3), message + "0")
          greeter ! Greeter.Greet(message, replyTo)
          Behaviors.same
        }
        case SayHello1(message) => {
          val replyTo = context.spawn(GreeterBot(max = 3), message + "1")
          greeter ! Greeter.Greet(message, replyTo)
          Behaviors.same
        }

      }
    }

  def main(args: Array[String]): Unit = {
    val system: ActorSystem[GreeterMain.Command] =
      ActorSystem(GreeterMain(), "hello")

    system ! GreeterMain.SayHello("World")
    system ! GreeterMain.SayHello1("Akka")
  }
}

object GreetTest extends App {
  val greeterMain: ActorSystem[GreeterMain.SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart")
  greeterMain ! SayHello("Davie")

}