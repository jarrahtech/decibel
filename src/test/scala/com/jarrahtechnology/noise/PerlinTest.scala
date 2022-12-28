package com.jarrahtechnology.noise

import org.junit.Test
import junit.framework.TestCase
import org.junit.Assert.*

import scala.language.implicitConversions
import scala.language.postfixOps

import com.jarrahtechnology.random.Random

class PerlinTest extends TestCase {
  
  @Test def testOrigin: Unit =  {
    val p3 = new PerlinNoise(Random.javaWithSeed(76857865))
    assertEquals(0.6348149562048512, p3.noise(0.6, 0.6, 0.6), 0.00000000000001)   
    assertEquals(0.5, p3.noise(1, 2, 3), 0.00000000000001)    
    assertEquals(0.26865248285818866, p3.noise(1.6, 2.6, 3.6), 0.00000000000001)    
    assertEquals(0.5509592254656059, p3.noise(1.12, 2.22, 3.92), 0.00000000000001)
  }

  @Test def testOrder: Unit =  {
    new PerlinNoise(order = 4)
    try {
      new PerlinNoise(order = 2)
      fail()
    } catch {
      case e: IllegalArgumentException => // expected
    }
  }
}