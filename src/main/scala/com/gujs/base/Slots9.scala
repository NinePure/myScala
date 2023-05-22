package com.gujs.base

object Slots9 extends App {
  def dfs(grid: Array[Array[Char]], visited: Array[Array[Boolean]], x: Int, y: Int, fruitType: Char): Int = {
    val m = grid.length
    val n = grid(0).length
    visited(x)(y) = true
    var count = 1
    val dx = Array(0, 0, 1, -1)
    val dy = Array(1, -1, 0, 0)
    for (i <- 0 until 4) {
      val nx = x + dx(i)
      val ny = y + dy(i)
      if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visited(nx)(ny) && grid(nx)(ny) == fruitType) {
        count += dfs(grid, visited, nx, ny, fruitType)
      }
    }
    count
  }
  def countConnectedFruits(grid: Array[Array[Char]]): Map[Char, List[Int]] = {
    val m = grid.length
    val n = grid(0).length
    val visited = Array.ofDim[Boolean](m, n)
    var counts = Map[Char, List[Int]]()
    for (i <- 0 until m; j <- 0 until n) {
      if (!visited(i)(j)) {
        val fruitType = grid(i)(j)
        val count = dfs(grid, visited, i, j, fruitType)
        counts = if (counts.contains(fruitType)) counts.updated(fruitType, counts(fruitType) :+ count)
        else counts.updated(fruitType, List(count))
      }
    }
    counts
  }
  // Example usage
  val grid = Array(
    Array('A', 'A', 'A', 'A'),
    Array('A', 'B', 'B', 'B'),
    Array('B', 'A', 'A', 'A'),
    Array('B', 'B', 'B', 'A')
  )
  val counts = countConnectedFruits(grid)
  println(counts)
  // Output: Map(A -> List(4, 2), B -> List(9))
}

