package com.jarrahtechnology.noise

import javax.imageio.ImageIO
import java.awt.image.BufferedImage // scalastyle:ignore illegal.imports
import java.awt.Color // scalastyle:ignore illegal.imports
import java.io.File
import java.nio.file.Files
import java.awt.image.DataBufferInt // scalastyle:ignore illegal.imports
import java.nio.file.Path
import scala.collection.mutable.ArrayBuffer

import com.jarrahtechnology.util.Math.*

case class NoiseGrid(val elements: Seq[Seq[Double]]) {
  require(elements.length > 0 && elements(0).length > 0, "empty")
  require(isRectangular(elements), "not rectangular")

  def apply(row: Int)(col: Int): Double = elements(row)(col)

  def map[B](f: (Double) => B): Seq[Seq[B]] = elements.map(_.map(f))

  lazy val rows = elements.length
  lazy val cols = elements(0).length

  def write(name: String): Unit = {
    val img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB)
    (0 until rows).map(row =>
      (0 until cols).map(col =>
        img.setRGB(col, row, {
            val c = elements(row)(col).clamp01.toFloat
            new Color(c, c, c).getRGB
          }
        )
      )
    )
    ImageIO.write(img, name.substring(name.length - 3, name.length), new File(name))
  }

  def isRectangular(mask: Seq[Seq[Double]]): Boolean = {
    val columnSizes = mask.map(_.length).distinct
    columnSizes.length == 1 && columnSizes(0) > 0
  }

  def normalised: NoiseGrid = {
    val minMax = elements.map(r => (r.fold(1d)(math.min(_, _)), r.fold(0d)(math.max(_, _))))
                         .fold(0d, 1d)((c, x) => (math.min(c._1, x._1), math.max(c._2, x._2)))
    NoiseGrid(elements.map(_.map(x => (x - minMax._1)/(minMax._2-minMax._1))))
  }
}

object NoiseGrid {
  def from(noise: NoiseGenerator, rows: Int, cols: Int) = {
    val elements = ArrayBuffer.empty[Seq[Double]]
    for (row <- 0 until rows) {
      var elementRow = ArrayBuffer.empty[Double]
      for (col <- 0 until cols) {  
        elementRow += noise.noise(col, row)
      }  
      elements += elementRow.toList
    }
    new NoiseGrid(elements.toSeq)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Null", "org.wartremover.warts.AsInstanceOf")) // Java interaction, null is required
  def fromImage(image: Path): NoiseGrid = {
    val bi = ImageIO.read(Files.newInputStream(image))
    val adjBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB) // adjust color model
    adjBi.getGraphics().drawImage(bi, 0, 0, null) // scalastyle:ignore null
    val rawElements = adjBi.getRaster().getDataBuffer().asInstanceOf[DataBufferInt].getData()
    var elements = ArrayBuffer.empty[Seq[Double]]
    (0 until adjBi.getHeight).map(row => {
      var elementRow = ArrayBuffer.empty[Double]
      (0 until adjBi.getWidth).map(col => {
        elementRow += new Color(rawElements(row * adjBi.getHeight + col)).getRed()/256d
      })
      elements += elementRow.toList
    })
    new NoiseGrid(elements.toList)
  }
}
