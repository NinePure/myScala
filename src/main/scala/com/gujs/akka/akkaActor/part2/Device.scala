package com.gujs.akka.akkaActor.part2

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import com.gujs.akka.akkaActor.Greeter
import com.gujs.akka.akkaActor.part2.DeviceManager.DeviceRegistered

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
 * 设备
 */
object Device {
  def apply(groupId: String, deviceId: String): Behavior[Command] = {
    Behaviors.setup(context => new Device(context, groupId, deviceId))
  }

  sealed trait Command

  final case object ScheduleCreateBotNotice extends Command

  final case class ReadTemperature(requestId: Long, replyTo: ActorRef[RespondTemperature]) extends Command

  final case class RespondTemperature(requestId: Long, deviceId: String, value: Option[Double])

  final case class RecordTemperature(requestId: Long, value: Double, replyTo: ActorRef[TemperatureRecorded])
    extends Command

  final case class TemperatureRecorded(requestId: Long)
  final case class Terminated(replyTo: ActorRef[DeviceRegistered]) extends Command



  case object Passivate extends Command
}

class Device(context: ActorContext[Device.Command], groupId: String, deviceId: String) extends AbstractBehavior[Device.Command](context) {

  import Device._

  var lastTemperatureReading: Option[Double] = None
  context.log.info2("my Device actor {}-{} started", groupId, deviceId)
  test()

  def test(): Behavior[Command] = {
    Behaviors.withTimers{ timer =>
      context.log.info("time {} ", Greeter.num)
      timer.startTimerAtFixedRate(ScheduleCreateBotNotice, ScheduleCreateBotNotice, Greeter.num seconds)
      Behaviors.same
    }
  }
  override def onMessage(msg: Device.Command): Behavior[Device.Command] = msg match {
    case ReadTemperature(id, replyTo) =>
      context.log.info("my Read temperature id {} ", id)
      replyTo ! RespondTemperature(id, deviceId, lastTemperatureReading)
      this
    case RecordTemperature(requestId, value, replyTo) =>
      context.log.info2("my Recorded temperature reading {} with {}", value, requestId)
      lastTemperatureReading = Some(value)
      replyTo ! TemperatureRecorded(requestId)
      this
    case ScheduleCreateBotNotice =>
      context.log.info("deviceId {}",this.deviceId)
      this
    case Terminated(replyTo) =>
      replyTo ! DeviceRegistered(null)
      println("停止123")
      Behaviors.stopped
    case Passivate =>
      println("停止")
      Behaviors.stopped
  }

  override def onSignal: PartialFunction[Signal, Behavior[Command]] = {
    case PostStop =>
      context.log.info2("my Device actor {}-{} stopped", groupId, deviceId)
      this
  }
}

