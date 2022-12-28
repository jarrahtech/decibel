package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Vector2

import com.jarrahtechnology.random.RandomGenerator

// white noise
// seed as powers of 2 causes non-randomness!
sealed case class WhiteNoise(val seed: Int, val granularity: Int = 256) extends NoiseGenerator {

  def noise(p: Vector2): Double = {
    def granulise(v: Double): Int = (v * granularity).toInt
    var n = seed ^ (7411 * granulise(p.x))
    n ^= 1619 * granulise(p.y)
    RandomGenerator.shift01(n * n * 60493)
  }
}
