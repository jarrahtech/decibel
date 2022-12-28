package com.jarrahtechnology.noise

import com.jarrahtechnology.util.Math.*
import com.jarrahtechnology.util.Vector2

import com.jarrahtechnology.random.Random

// Improved Perlin noise taken from http://mrl.nyu.edu/~perlin/noise/
// order is the number of values in the random hash table (2^order entries), the higher this number the finer grained the resulting noise 
sealed case class PerlinNoise(rand: Random = Random.javaRandomSeed(), val order: Int = 8) extends NoiseGenerator, PermutationsHash(rand, order) {
  
  def noise(p: Vector2): Double = {
    val idx = p.toInt(math.floor(_).toInt & size)
    val rem = p.subtract(p.toFloor)
    val faded = rem.op(fade)
    val PY = permutations(idx.y)
    val PYY = permutations(idx.y+1)
    val n = faded.y.lerp(
        faded.x.lerp(grad(permutations(idx.x + PY), rem.x, rem.y), grad(permutations(idx.x + 1 + PY), rem.x - 1, rem.y)),
        faded.x.lerp(grad(permutations(idx.x + PYY), rem.x, rem.y - 1), grad(permutations(idx.x + 1 + PYY), rem.x - 1, rem.y - 1))
      )
    (n + 1d) / 2d // Transform the range to [0.0, 1.0]  
  }

  // for reference
  def noise(x: Double, y: Double, z: Double): Double = {
    val X = math.floor(x).toInt & size
    val Y = math.floor(y).toInt & size
    val Z = math.floor(z).toInt & size
    val xr = x - math.floor(x)
    val yr = y - math.floor(y)
    val zr = z - math.floor(z)
    val u = fade(xr)
    val v = fade(yr)
    val A = permutations(X) + Y
    val AA = permutations(A) + Z
    val AB = permutations(A + 1) + Z
    val B = permutations(X + 1) + Y
    val BA = permutations(B) + Z
    val BB = permutations(B + 1) + Z

    val n = fade(zr).lerp(
      v.lerp(
        u.lerp(grad(permutations(AA), xr, yr, zr), grad(permutations(BA), xr - 1, yr, zr)),
        u.lerp(grad(permutations(AB), xr, yr - 1, zr), grad(permutations(BB), xr - 1, yr - 1, zr))
      ),
      v.lerp(
        u.lerp(grad(permutations(AA + 1), xr, yr, zr - 1), grad(permutations(BA + 1), xr - 1, yr, zr - 1)),
        u.lerp(grad(permutations(AB + 1), xr, yr - 1, zr - 1), grad(permutations(BB + 1), xr - 1, yr - 1, zr - 1))
      )
    )
    (n + 1d) / 2d // Transform the range to [0.0, 1.0]
  }

  private def fade(t: Double) = t * t * t * (t * (t * 6 - 15) + 10)

  private def grad(hash: Int, x: Double, y: Double, z: Double) = {
    val h = hash & 15
    val u = if (h < 8) x else y
    val v = if (h < 4) y else if (h == 12 || h == 14) x else z
    (if ((h & 1) == 0) u else -u) + (if ((h & 2) == 0) v else -v)
  }

  private def grad(hash: Int, x: Double, y: Double) = {
    val h = hash & 7
    val u = if (h < 4) x else y
    if ((h & 1) == 0) u else -u
  }
}
