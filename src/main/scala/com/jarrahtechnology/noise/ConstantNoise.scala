package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Vector2

sealed case class ConstantNoise(val constant: Double) extends NoiseGenerator {
  require(constant>=0 && constant <=1, "constant between [0, 1]")

  def noise(p: Vector2): Double = constant
}

object ConstantNoise {
  def zero = ConstantNoise(0)
  def one = ConstantNoise(1)
  def half = ConstantNoise(0.5)
}
