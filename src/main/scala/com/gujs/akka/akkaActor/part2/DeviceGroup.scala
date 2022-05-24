package com.gujs.akka.akkaActor.part2

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import akka.util.Timeout
import com.gujs.akka.akkaActor.Greeter
import com.gujs.akka.akkaActor.part2.DeviceManager.{DeviceRegistered, ReplyDeviceList, RequestDeviceList, RequestTrackDevice}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object DeviceGroup {
  def apply(groupId: String):Behavior[Command] =
    Behaviors.setup(context => new DeviceGroup(context,groupId))

  trait Command

   final case class DeviceTerminated(device: ActorRef[Device.Command], groupId: String, deviceId: String)
    extends Command


   final case class DeviceRestart(deviceId: String,replyTo: ActorRef[DeviceRegistered])
    extends Command
   final case class CreateNew(deviceId: String,replyTo: ActorRef[DeviceRegistered]) extends Command


}
class DeviceGroup(context: ActorContext[DeviceGroup.Command], groupId: String)
  extends AbstractBehavior[DeviceGroup.Command](context) {
  import DeviceGroup._

  private var deviceIdToActor = Map.empty[String, ActorRef[Device.Command]]

  context.log.info("DeviceGroup {} started", groupId)

  def creatNew(deviceId:String): DeviceRegistered ={
    context.log.info("Restart device actor for {}", deviceId)
    val deviceActor = context.spawn(Device(groupId, deviceId), s"device-$deviceId-1231232")
    context.watchWith(deviceActor, DeviceTerminated(deviceActor, groupId, deviceId))
    deviceIdToActor += deviceId -> deviceActor
    DeviceRegistered(deviceActor)
  }
  override def onMessage(msg: Command): Behavior[Command] =
    msg match {
      case trackMsg @ RequestTrackDevice(`groupId`, deviceId, replyTo) =>
        deviceIdToActor.get(deviceId) match {
          case Some(deviceActor) =>
            replyTo ! DeviceRegistered(deviceActor)
          case None =>
            context.log.info("Creating device actor for {}", trackMsg.deviceId)
            val deviceActor = context.spawn(Device(groupId, deviceId), s"device-$deviceId")
            context.watchWith(deviceActor, DeviceTerminated(deviceActor, groupId, deviceId))
            deviceIdToActor += deviceId -> deviceActor
            replyTo ! DeviceRegistered(deviceActor)
        }
        this
      case DeviceRestart(deviceId, replyTo) =>
        Greeter.num = 10
        deviceIdToActor.get(deviceId) match {
          case Some(deviceActor) =>
            implicit val timeout: Timeout = 3.seconds
            context.ask(deviceActor, replyTo => Device.Terminated(replyTo)){
              case Success(a) =>
                CreateNew(deviceId,replyTo)
              case Failure(a) =>
                CreateNew(deviceId,replyTo)
            }
          case None => CreateNew(deviceId,replyTo)
        }
        Behaviors.same
      case CreateNew(deviceId,replyTo) =>
        replyTo ! creatNew(deviceId)
        this
      case RequestTrackDevice(gId, _, _) =>
        context.log.warn2("Ignoring TrackDevice request for {}. This actor is responsible for {}.", gId, groupId)
        this

      case RequestDeviceList(requestId, gId, replyTo) =>
        if (gId == groupId) {
          replyTo ! ReplyDeviceList(requestId, deviceIdToActor.keySet)
          this
        } else
          Behaviors.unhandled

      case DeviceTerminated(_,_,deviceId) =>
        context.log.info("Device actor for {} has been terminated", deviceId)
        deviceIdToActor -= deviceId
        this
    }

  override def onSignal: PartialFunction[Signal, Behavior[Command]] = {
    case PostStop =>
      context.log.info("DeviceGroup {} stopped", groupId)
      this
  }
}

