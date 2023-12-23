package com.gujs.base

class tongitsTest {

}
object CardCombination {

  def findCombinations(cards: List[String]): List[List[String]] = {
    // 存储最小点数和对应的组合
    var minPoints = Int.MaxValue
    var minCombination: List[List[String]] = List()

    // 递归函数，查找组合
    def findHelper(currentCombination: List[List[String]], remainingCards: List[String]): Unit = {
      if (remainingCards.isEmpty) {
        // 计算当前组合的点数和
        val currentPoints = currentCombination.flatten.map(getCardValue).sum

        // 更新最小点数和对应的组合
        if (currentPoints < minPoints) {
          minPoints = currentPoints
          minCombination = currentCombination
        }
      } else {
        // 尝试将当前牌加入已有组合中
        val nextCard = remainingCards.head
        val updatedCombination = currentCombination.map { group =>
          if (isValidGroup(group :+ nextCard)) group :+ nextCard else group
        }

        // 尝试将当前牌作为新组合的起始
        val newGroup = List(nextCard)
        findHelper(currentCombination :+ newGroup, remainingCards.tail)
        findHelper(updatedCombination, remainingCards.tail)
      }
    }

    // 启动递归
    findHelper(List(), cards)

    // 返回最小点数对应的组合
    minCombination
  }

  // 辅助函数，检查是否是有效的组合
  def isValidGroup(group: List[String]): Boolean = {
    group.groupBy(getCardValue).forall { case (_, cards) => cards.length <= 4 }
  }

  // 辅助函数，获取牌的数值
  def getCardValue(card: String): Int = {
    val value = card.last.toString // 获取除了花色之外的部分
    value match {
      case "A" => 1
      case "J" => 11
      case "Q" => 12
      case "K" => 13
      case _ => value.toInt
    }
  }

  // 辅助函数，获取牌的花色
  def getCardSuit(card: String): String = {
    card.init // 获取牌的最后一个字符，即花色
  }

  def main(args: Array[String]): Unit = {
    val cards = List("♥️A", "♦️A", "♠️A", "♣️A", "♥️2", "♥️3", "♠️6", "♥️7", "♥️8", "♠️8", "♠️J", "♠️Q", "♠️K")

    val combinations = findCombinations(cards)
    combinations.foreach(println)
  }
}
