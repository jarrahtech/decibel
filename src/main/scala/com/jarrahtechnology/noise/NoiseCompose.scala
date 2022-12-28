package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Math.clamp01
import com.jarrahtechnology.util.Vector2

sealed case class AddNoise(n1: NoiseGenerator, n2: NoiseGenerator, clamp: Boolean = true) extends NoiseGenerator {
  def noise(p: Vector2): Double = {
    val result = n1.noise(p)+n2.noise(p)
    if (clamp) then result.clamp01 else result
  }
}

sealed case class Mult(n1: NoiseGenerator, n2: NoiseGenerator, clamp: Boolean = true) extends NoiseGenerator {
  def noise(p: Vector2): Double = {
    val result = n1.noise(p)+n2.noise(p)
    if (clamp) then result.clamp01 else result
  }
}

sealed case class BinaryPass(value: NoiseGenerator, threshold: NoiseGenerator) extends NoiseGenerator {
  def noise(p: Vector2): Double = if (value.noise(p) < threshold.noise(p)) 0d else 1d
}

sealed case class HighPass(value: NoiseGenerator, threshold: NoiseGenerator) extends NoiseGenerator {
  def noise(p: Vector2): Double = {
    val n = value.noise(p)
    if (n >= threshold.noise(p)) 0d else n
  }
}

sealed case class LowPass(value: NoiseGenerator, threshold: NoiseGenerator) extends NoiseGenerator {
  def noise(p: Vector2): Double = {
    val n = value.noise(p)
    if (n < threshold.noise(p)) 0d else n
  }
}

