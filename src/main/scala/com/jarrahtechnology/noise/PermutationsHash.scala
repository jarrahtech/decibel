package com.jarrahtechnology.noise

import com.jarrahtechnology.random.Random

// order is the number of values in the random hash table (2^order entries), the higher this number the finer grained the resulting noise. 8 is common (meaning 255 options) 
trait PermutationsHash(rand: Random, order: Int) {
  require(order>=4, "order must be at least 4")

  protected lazy val size = (1 << order) - 1
  protected lazy val permutations = {
    val p = rand.shuffle((0 to size).toList)
    p ++ p 
  }  
}
