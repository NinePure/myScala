package com.gujs.base

import scala.util.Random
object TrianglePlinko {
  def main(args: Array[String]): Unit = {
    var sumWin = 0d
    for(i <- 0 until 10000){
      sumWin += getWorstResult(2, ()=>plinko)
    }
    println(sumWin)
  }
  def getWorstResult(times: Int, spinFunc: () => Double): Double = {
    (0 until times).map{_ =>
      spinFunc.apply()
    }.min
  }
  def plinko = {
    val random = new Random()
    val slotValues = List[Double](11, 3.2, 1.6, 1.2, 1.1, 1, 0.5, 1, 1.1, 1.2, 1.6, 3.2, 11) // 可以获得的奖励金额
    val numSlotsInBottomRow = 12*2 +1// 最底层 13 个槽位
    val numLevels = 12 // 12 层
    val numEntrances = 2 // 两个入口
    val entranceSlots = Seq(12, 14) // 两个入口的槽位编号
    var winnings = 0d // 初始化赢的奖金为 0
    var currentLevel = 1 // 初始化当前层数为 1
    var currentSlot = entranceSlots(random.nextInt(numEntrances)) // 根据随机选取入口的槽位编号为当前槽位赋值
    while (currentLevel <= numLevels) {

      println(s"Coin dropped to level $currentLevel, slot ${new SlotMachine(numLevels, numSlotsInBottomRow).getSlotCoordinates(currentLevel,currentSlot)}.") // 输出掉落信息
      if (currentLevel == numLevels ) {
        winnings += slotValues((currentSlot+1)/2 - 1) // 奖励
        println(s"Congratulations! You won $$$winnings on slot $currentSlot at level $currentLevel!")
      }
      // 随机决定下一步掉落到左侧槽位还是右侧槽位
      if (currentSlot == 0) {
        currentSlot = 1 // 如果当前槽位在最左侧，则只能往右侧走
      } else if (currentSlot == numSlotsInBottomRow) {
        currentSlot = numSlotsInBottomRow - 1 // 如果当前槽位在最右侧，则只能往左侧走
      } else if (entranceSlots.contains(currentSlot)) {
        currentSlot += (if (random.nextBoolean()) -1 else 1) // 如果当前槽位是入口，则随机决定往左侧或右侧走
      } else {
        currentSlot += (if (random.nextBoolean()) -1 else 1) // 如果当前槽位不是入口，则可以随机决定往左侧或右侧走
      }
      currentLevel += 1
    }
    println(s"Total winnings: $$${winnings}") // 输出获得的总奖励
    winnings
  }

}
class SlotMachine(val numRows: Int, val numCols: Int) {
  private val numSlots = numRows * numCols
  private val slotValues = Array.tabulate(numSlots)(_ + 1)

  /**
   * 将槽位编号转换为横纵坐标
   */
  def getSlotCoordinates(row: Int, slot: Int): (Int, Int) = {
    val col = slot % numCols
    (row, col)
  }

  def spin(): List[Int] = {
    val result = collection.mutable.ListBuffer[Int]()
    (0 until 3).foreach { _ =>
      val slot = Random.nextInt(numSlots)
      result += getSlotValue(slot)
    }
    result.toList
  }

  def getSlotValue(slot: Int): Int = {
    slotValues(slot)
  }
}