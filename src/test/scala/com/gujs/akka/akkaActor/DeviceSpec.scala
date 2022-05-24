package com.gujs.akka.akkaActor

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.gujs.akka.akkaActor.part2.Device._
import com.gujs.akka.akkaActor.part2.DeviceGroup.DeviceRestart
import com.gujs.akka.akkaActor.part2.DeviceManager.{apply, _}
import com.gujs.akka.akkaActor.part2.{Device, DeviceGroup, DeviceManager}
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpecLike

class DeviceSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {
  "my Device actor" must {

    "my reply with empty reading if no temperature is known" in {
      val probe = createTestProbe[Device.RespondTemperature]()
      val deviceActor = spawn(Device("group", "device"))
      //      val deviceActor = spawn(Device("group", "device"))

      deviceActor ! Device.ReadTemperature(requestId = 42, probe.ref)
      val response = probe.receiveMessage()
      response.requestId should ===(42)
      response.value should ===(None)
    }

    "reply with latest temperature reading" in {
      val recordProbe = createTestProbe[TemperatureRecorded]()
      val readProbe = createTestProbe[RespondTemperature]()
      val deviceActor = spawn(Device("group2", "device2"),"myActor2")

      deviceActor ! Device.RecordTemperature(requestId = 1, 24.0, recordProbe.ref)
      recordProbe.expectMessage(Device.TemperatureRecorded(requestId = 1))

      deviceActor ! Device.ReadTemperature(requestId = 2, readProbe.ref)
      val response1 = readProbe.receiveMessage()
      response1.requestId should ===(2)
      response1.value should ===(Some(24.0))

      deviceActor ! Device.RecordTemperature(requestId = 3, 55.0, recordProbe.ref)
      recordProbe.expectMessage(Device.TemperatureRecorded(requestId = 3))

      deviceActor ! Device.ReadTemperature(requestId = 4, readProbe.ref)
      val response2 = readProbe.receiveMessage()
      response2.requestId should ===(4)
      response2.value should ===(Some(55.0))
    }

//    group
    "be able to register a device actor" in {
      val probe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", probe.ref)
      val registered1 = probe.receiveMessage()
      val deviceActor1 = registered1.device

      // another deviceId
      groupActor ! RequestTrackDevice("group", "device2", probe.ref)
      val registered2 = probe.receiveMessage()
      val deviceActor2 = registered2.device
      deviceActor1 should !==(deviceActor2)

      // Check that the device actors are working
      val recordProbe = createTestProbe[TemperatureRecorded]()
      deviceActor1 ! RecordTemperature(requestId = 0, 1.0, recordProbe.ref)
      recordProbe.expectMessage(TemperatureRecorded(requestId = 0))
      deviceActor2 ! Device.RecordTemperature(requestId = 1, 2.0, recordProbe.ref)
      recordProbe.expectMessage(Device.TemperatureRecorded(requestId = 1))
    }

    "ignore requests for wrong groupId" in {
      val probe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("wrongGroup", "device1", probe.ref)
      probe.expectNoMessage(500.milliseconds)
    }

    "return same actor for same deviceId" in {
      val probe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", probe.ref)
      val registered1 = probe.receiveMessage()

      // registering same again should be idempotent
      groupActor ! RequestTrackDevice("group", "device1", probe.ref)
      val registered2 = probe.receiveMessage()

      registered1.device should ===(registered2.device)
    }

    "能够列出活动设备 be able to list active devices" in {
      val registeredProbe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
      registeredProbe.receiveMessage()

      groupActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
      registeredProbe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      groupActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))
    }

    "能够在一个关闭后列出活动设备 be able to list active devices after one shuts down" in {
      val registeredProbe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
      val registered1 = registeredProbe.receiveMessage()
      val toShutDown = registered1.device

      groupActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
      registeredProbe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      groupActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))

      toShutDown ! Passivate
      registeredProbe.expectTerminated(toShutDown, registeredProbe.remainingOrDefault)

      // using awaitAssert to retry because it might take longer for the groupActor
      // to see the Terminated, that order is undefined
      registeredProbe.awaitAssert {
        groupActor ! RequestDeviceList(requestId = 1, groupId = "group", deviceListProbe.ref)
        deviceListProbe.expectMessage(ReplyDeviceList(requestId = 1, Set("device2")))
      }
    }
//    manager
    "be able to register a device group actor" in {
      val probe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group1", "device1", probe.ref)
      val registered1 = probe.receiveMessage()
      val deviceActor1 = registered1.device

      // another deviceId
      managerActor ! RequestTrackDevice("group2", "device1", probe.ref)
      val registered2 = probe.receiveMessage()
      val deviceActor2 = registered2.device
      deviceActor1 should !==(deviceActor2)

      // Check that the device actors are working
      val recordProbe = createTestProbe[TemperatureRecorded]()
      deviceActor1 ! RecordTemperature(requestId = 0, 1.0, recordProbe.ref)
      recordProbe.expectMessage(TemperatureRecorded(requestId = 0))
      deviceActor2 ! Device.RecordTemperature(requestId = 1, 2.0, recordProbe.ref)
      recordProbe.expectMessage(Device.TemperatureRecorded(requestId = 1))
    }

    "return same actor group for same deviceId" in {
      val probe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group", "1", probe.ref)
      val registered1 = probe.receiveMessage()

      // registering same again should be idempotent
      managerActor ! RequestTrackDevice("group", "1", probe.ref)
      val registered2 = probe.receiveMessage()

      registered1.device should ===(registered2.device)
    }

    "能够列出活动设备 be able to list active devices1" in {
      val registeredProbe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
      registeredProbe.receiveMessage()
      managerActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
      registeredProbe.receiveMessage()

      managerActor ! RequestTrackDevice("group2", "device2", registeredProbe.ref)
      registeredProbe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      managerActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))
      managerActor ! RequestDeviceList(requestId = 1, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 1, Set("device1", "device2")))
    }

    "能够在一个关闭后列出活动设备 be able to list active devices after one shuts down1" in {
      val registeredProbe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
      val registered1 = registeredProbe.receiveMessage()
      val toShutDown = registered1.device

      managerActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
      registeredProbe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      managerActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))

      toShutDown ! Passivate
      registeredProbe.expectTerminated(toShutDown, registeredProbe.remainingOrDefault)

      // using awaitAssert to retry because it might take longer for the groupActor
      // to see the Terminated, that order is undefined
      registeredProbe.awaitAssert {
        managerActor ! RequestDeviceList(requestId = 1, groupId = "group", deviceListProbe.ref)
        deviceListProbe.expectMessage(ReplyDeviceList(requestId = 1, Set("device2")))
      }
    }
//    查询组
    /*"返回工作设备的温度值 return temperature value for working devices" in {
      val requester = createTestProbe[RespondAllTemperatures]()

      val device1 = createTestProbe[Command]()
      val device2 = createTestProbe[Command]()

      val deviceIdToActor = Map("device1" -> device1.ref, "device2" -> device2.ref)

      val queryActor =
        spawn(DeviceGroupQuery(deviceIdToActor, requestId = 1, requester = requester.ref, timeout = 3.seconds))

      device1.expectMessageType[Device.ReadTemperature]
      device2.expectMessageType[Device.ReadTemperature]

      queryActor ! WrappedRespondTemperature(Device.RespondTemperature(requestId = 0, "device1", Some(1.0)))
      queryActor ! WrappedRespondTemperature(Device.RespondTemperature(requestId = 0, "device2", Some(2.0)))

      requester.expectMessage(
        RespondAllTemperatures(
          requestId = 1,
          temperatures = Map("device1" -> Temperature(1.0), "device2" -> Temperature(2.0))))
    }*/

  }

  "my test scheduler" must {
    "ceshi1" in {
      val probe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", probe.ref)
      val registered1 = probe.receiveMessage()
      val deviceActor1 = registered1.device

      // another deviceId
      groupActor ! RequestTrackDevice("group", "device2", probe.ref)
      val registered2 = probe.receiveMessage()
      val deviceActor2 = registered2.device
      println(deviceActor1)
      println(deviceActor2)
      groupActor ! RequestTrackDevice("group", "device2", probe.ref)
      val registered3 = probe.receiveMessage()
      val deviceActor3 = registered3.device
      println(deviceActor3)

      Thread.sleep(10000L)
      groupActor ! DeviceRestart( "device2", probe.ref)

//      groupActor ! RequestTrackDevice("group", "device2", probe.ref)
      val registered4 = probe.receiveMessage()
      val deviceActor4 = registered4.device
      println(deviceActor4)


    }
  }
}