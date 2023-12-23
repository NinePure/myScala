package com.gujs.base

object CardCombination {

  def findCombinations(cards: List[String]): List[List[String]] = {
    // 存储最小点数和对应的组合
    var minPoints = Int.MaxValue
    var minCombination: List[List[String]] = List()

    // 递归函数，查找组合
    def findHelper(currentCombination: List[List[String]], remainingCards: List[String]): Unit = {
      if (remainingCards.isEmpty) {
        println(currentCombination)
        // 计算当前组合的点数和
        val currentPoints = currentCombination.filterNot(c => c.size>=3 && (isRuns(c) || isSet(c))).flatten.map(getCardValue).sum
        // 更新最小点数和对应的组合
        if (currentPoints < minPoints) {
          minPoints = currentPoints
          minCombination = currentCombination
        }
      } else {
        // 尝试将当前牌加入已有组合中
        val nextCard = remainingCards.head
        currentCombination.foreach { group =>
          if (isValidGroup(group :+ nextCard)) {
            val updatedCombination = currentCombination.map(g =>if(g == group) g :+ nextCard else g)
            findHelper(updatedCombination, remainingCards.tail)
          }
        }

        // 尝试将当前牌作为新组合的起始
        val newGroup = List(nextCard)
        findHelper(currentCombination :+ newGroup, remainingCards.tail)
      }
    }

    // 启动递归
    findHelper(List(), cards)

    // 返回最小点数对应的组合
    minCombination
  }

  // 辅助函数，检查是否是有效的组合
  def isValidGroup(group: List[String]): Boolean = {
    isRuns(group) || isSet(group)
  }
  def isRuns(group: List[String]): Boolean = {
    //todo  group.size < 3最终检查这个
    val suitGroup = group.groupBy(getCardSuit)
    if (suitGroup.size != 1) {
      false
    } else {
      val sortedCards = group.sortBy(getCardValue)
      val firstCard = getCardValue(sortedCards.head)
      val lastCard = getCardValue(sortedCards.last)
       if (lastCard - firstCard == sortedCards.size - 1) {
        sortedCards.sliding(2).forall { cards =>
          getCardValue(cards.head) + 1 == getCardValue(cards.last)
        }
      } else {
        false
      }
    }
  }
  def isSet(group: List[String]): Boolean = {
    group.groupBy(getCardValue).size == 1
  }

  // 辅助函数，获取牌的数值
  def getCardValue(card: String): Int = {
    val value = card.last // 获取除了花色之外的部分
    if (value == 'A') 1
    else if (value == 'J') 11
    else if (value == 'Q') 12
    else if (value == 'K') 13 else value.toString.toInt
  }

  // 辅助函数，获取牌的花色
  def getCardSuit(card: String): String = {
    card.init // 获取牌的最后一个字符，即花色
  }

  def main(args: Array[String]): Unit = {
//    val cards = List("♥️A", "♦️A", "♠️A", "♣️A", "♥️2", "♥️3", "♠️6", "♠️7", "♥️8", "♠️8", "♠️J", "♠️Q", "♠️K")
    val cards = List("♥️A", "♦️A", "♥️2", "♥️3", "♦️3", "♠️3", "♠️6", "♠️7", "♥️8", "♠️8", "♠️J", "♠️Q", "♠️K")
    val combinations = findCombinations(cards)
    println(combinations.filterNot(c => c.size>=3 && (isRuns(c) || isSet(c))).flatten.map(getCardValue).sum)
    combinations.foreach(println)
  }
}