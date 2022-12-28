package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Vector2

trait NoiseGenerator {
  def noise(x: Double, y: Double): Double = noise(Vector2(x, y))
  def noise(p: Vector2): Double
}
