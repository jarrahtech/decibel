package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Math.clamp01
import com.jarrahtechnology.util.Vector2

import com.jarrahtechnology.random.Random

// https://en.wikipedia.org/wiki/Voronoi_diagram & https://en.wikipedia.org/wiki/Worley_noise
// density is the number of points per cell, the higher the density the more irregular the result
case class VoronoiNoise(rand: Random = Random.javaRandomSeed(), val order: Int = 8, val density: Int = 1, distanceFn: (Vector2, Vector2) => Double = Vector2.distance)
    extends NoiseGenerator, PermutationsHash(rand, order) {

  require(density > 0, "density must be >0")

  def noise(p: Vector2): Double = {
    def loopPermutations(idx: Int, num: Int): Int = if (num == 1) permutations(idx) else permutations(loopPermutations(idx, num - 1))

    val floor = p.toFloor
    val idx = floor.toInt(_.toInt & size)
    var minDist: Double = 1

    for (u <- -1 to 1) {
      for (v <- -1 to 1) {
        for (i <- 1 to density) {
          val xHash = loopPermutations((idx.x + u) & size, i)
          val yHash = loopPermutations((idx.y + v + xHash) & size, i)
          val cellPt = Vector2(floor.x + u + xHash / size.toDouble, floor.y + v + yHash / size.toDouble)
          minDist = math.min(minDist, distanceFn(p, cellPt))
        }
      }
    }
    minDist
  }
}
