package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Vector2

// http://www.decarpentier.nl/scape-procedural-basics
// lacunarity - factor by which the frequency is multiplied for each successive octave (usually 2)
// gain - the factor by which the sample of an octave is multiplied (squared for each successive octave). Must be >0  (usually 0.5)
// octaves - how many times to sample & sum the perlin function (normalised)
// frequency - scale of the noise, higher frequency noise fluctuates faster
trait NoiseSample(underlying: NoiseGenerator, lacunarity: Double, gain: Double, octaves: Int, frequency: Double) extends NoiseGenerator {
  require(gain > 0, "gain must be >0")
  require(octaves > 0, "gain must be >0")
  require(lacunarity > 0, "lacunarity must be >0")
  require(frequency > 0, "frequency must be >0")

  def noise(p: Vector2): Double = {
    def initial() = (0d, frequency, 1d, 0d)
    def noiseAdjust(freq: Double, amp: Double) = amp * sample(p.multiply(freq))
    def compound(total: Double, frequency: Double, amplitude: Double, max: Double) =
      (total + noiseAdjust(frequency, amplitude), frequency * lacunarity, amplitude * gain, max + amplitude)

    val result = (0 until octaves).foldLeft(initial())((c, i) => (compound).tupled(c))
    result._1 / result._4
  }

  def sample(p: Vector2): Double
}

// Fractional Brownian Motion - a composite of perlin noise
sealed case class FbmNoise(underlying: NoiseGenerator, lacunarity: Double = 2, gain: Double = 0.5, octaves: Int = 8, frequency: Double = 0.01)
    extends NoiseSample(underlying, lacunarity, gain, octaves, frequency) {
  def sample(p: Vector2) = underlying.noise(p)
}

// terrain with sharp ridges instead of creases, higher sharpness makes the ridges more distinct
sealed class RidgedNoise(underlying: NoiseGenerator, lacunarity: Double = 2, gain: Double = 0.5, octaves: Int = 8, frequency: Double = 0.01, val sharpness: Double = 1) 
    extends NoiseSample(underlying, lacunarity, gain, octaves, frequency) {
  require(sharpness > 0, "sharpness must be >0")
  def sample(p: Vector2) = Math.pow(1d - Math.abs(underlying.noise(p)), sharpness)
}

//  a more ‘billowy’ and eroded terrain with sharp creases
sealed class TurbulenceNoise(underlying: NoiseGenerator, lacunarity: Double = 2, gain: Double = 0.5, octaves: Int = 8, frequency: Double = 0.01)
    extends NoiseSample(underlying, lacunarity, gain, octaves, frequency) {
  def sample(p: Vector2) = Math.abs(underlying.noise(p))
}
