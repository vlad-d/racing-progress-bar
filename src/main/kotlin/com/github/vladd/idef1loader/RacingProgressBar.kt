package com.github.vladd.idef1loader

import com.github.vladd.idef1loader.settings.RacingProgressBarSettingsState
import com.intellij.ui.JBColor
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JProgressBar
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicGraphicsUtils
import javax.swing.plaf.basic.BasicProgressBarUI

open class RacingProgressBar : BasicProgressBarUI() {
    private val settings = RacingProgressBarSettingsState.getInstance()

    //    private val TRACK_COLOR = Color(19, 19, 40)
    private val TRACK_COLOR = Color(90, 104, 121)
    private val SIDES_COLOR = Color(170, 170, 170)
    private val SIDES_RED_COLOR = Color(166, 12, 12)
    private val CHECKERED_FLAG_ROWS = 4
    private val CHECKERED_FLAG_COLS = 2
    private var velocity = 1
    private var position = 0

    private lateinit var carIcon: BufferedImage
    private lateinit var scaledCarLtr: ImageIcon
    private lateinit var scaledCarRtl: ImageIcon

    override fun installDefaults() {
        super.installDefaults()
        val path = "/cars/car_${settings.selectedTeam}.png"
        carIcon = ImageIO.read(RacingProgressBar::class.java.getResourceAsStream(path))
    }


    override fun paintDeterminate(g: Graphics?, c: JComponent?) {
        if (!(g is Graphics2D)) {
            return
        }

        if (progressBar.orientation != SwingConstants.HORIZONTAL) {
            super.paintDeterminate(g, c)
            return
        }


        val (barRectWidth, barRectHeight) = computeBarSize()

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }

        val g2 = g.create() as Graphics2D
        val bRect = Rectangle(progressBar.size)
        if (c!!.isOpaque && c.parent != null) {
            g2.color = c.parent.background
            g2.fill(bRect)
        }


        paintTrack(g2, barRectWidth, barRectHeight)
        paintCheckeredFlag(g2, barRectWidth, barRectHeight)

        val amountFull = getAmountFull(progressBar.insets, bRect.width, bRect.height)

        paintCar(g2, c, barRectHeight, amountFull, c.componentOrientation.isLeftToRight)

    }

    override fun paintIndeterminate(g: Graphics, c: JComponent) {
        if (!(g is Graphics2D)) {
            return
        }

        if (progressBar.orientation != SwingConstants.HORIZONTAL) {
            super.paintDeterminate(g, c)
            return
        }

        val (barRectWidth, barRectHeight) = computeBarSize()

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }

        val g2 = g.create() as Graphics2D
        val bRect = Rectangle(progressBar.size)
        if (c!!.isOpaque && c.parent != null) {
            g2.color = c.parent.background
            g2.fill(bRect)
        }


        paintTrack(g2, barRectWidth, barRectHeight)


        paintCar(g2, c, barRectHeight, position, velocity > 0)
        updatePosition(barRectWidth)


    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int {
        return availableLength
    }

    override fun getPreferredSize(c: JComponent?): Dimension {
        val size = super.getPreferredSize(c)
        if (c !is JProgressBar) {
            return size
        }
        if (c.orientation == SwingConstants.VERTICAL) {
            return size
        }

        size.height = 20

        return size
    }

    private fun computeBarSize(): Pair<Int, Int> {
        val b = progressBar.insets // area for border
        val barRectWidth = progressBar.width - (b.right + b.left)
        val barRectHeight = progressBar.height - (b.top + b.bottom)

        return Pair(barRectWidth, barRectHeight)
    }

    private fun paintTrack(g: Graphics2D, barRectWidth: Int, barRectHeight: Int) {
        val lineHeight = 1
        val squareSize = 2
        val width = barRectWidth
        val height = barRectHeight
        g.color = TRACK_COLOR
        g.fillRect(0, 0, width, height)

        // paint a stripe alternating between red and white at the top of the bar
        val squares = barRectWidth / squareSize
        for (i in 0 until squares) {
            if (i % 2 == 0) {
                g.color = SIDES_COLOR
            } else {
                g.color = SIDES_RED_COLOR
            }
            g.fillRect(i * squareSize, 0, squareSize, squareSize)
        }

        // paint 1 horizontal white line at the top of the bar and 1 horizontal white line at the bottom of the bar
        g.color = SIDES_COLOR
        g.fillRect(0, squareSize + lineHeight, width, lineHeight)
        g.fillRect(0, height - 2 * lineHeight - squareSize, width, lineHeight)

        // paint a stripe alternating between red and white at the bottom of the bar
        for (i in 0 until squares) {
            if (i % 2 == 0) {
                g.color = SIDES_COLOR
            } else {
                g.color = SIDES_RED_COLOR
            }
            g.fillRect(i * squareSize, height - 2 * lineHeight, squareSize, squareSize)
        }
    }

    private fun paintCheckeredFlag(g: Graphics2D, barWidth: Int, barHeight: Int) {

        val squareSize = barHeight / CHECKERED_FLAG_ROWS
        val startX = barWidth - squareSize * CHECKERED_FLAG_COLS
        for (row in 0 until CHECKERED_FLAG_ROWS) {
            for (col in 0 until CHECKERED_FLAG_COLS) {
                if ((row + col) % 2 == 0) {
                    g.color = JBColor.BLACK
                } else {
                    g.color = JBColor.WHITE
                }
                g.fillRect(startX + col * squareSize, row * squareSize, squareSize, squareSize)
            }

        }
    }

    private fun paintCar(g2: Graphics2D, c: JComponent, barRectHeight: Int, x: Int, isLtr: Boolean) {
        val scaledIcon = getCar(barRectHeight, isLtr)
        scaledIcon.paintIcon(c, g2, x, 0)
    }

    private fun getCar(height: Int, isLtr: Boolean): ImageIcon {
        if (isLtr) {
            if (!::scaledCarLtr.isInitialized) {
                val ratio = height.toFloat() / carIcon.height.toFloat()
                val newWidth = carIcon.width * ratio
                val scaledImage = carIcon.getScaledInstance(newWidth.toInt(), height, java.awt.Image.SCALE_SMOOTH)
                scaledCarLtr = ImageIcon(scaledImage)
            }

            return scaledCarLtr
        }

        if (!::scaledCarRtl.isInitialized) {
            val ratio = height.toFloat() / carIcon.height.toFloat()
            val newWidth = carIcon.width * ratio
            var tx = AffineTransform()
            tx.quadrantRotate(2, carIcon.width / 2.0, carIcon.height / 2.0)
            var op = AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)
            val rotatedImage = op.filter(carIcon, null)
            val scaledImage =
                rotatedImage.getScaledInstance(newWidth.toInt(), height, java.awt.Image.SCALE_SMOOTH)
            scaledCarRtl = ImageIcon(scaledImage)
        }

        return scaledCarRtl

    }

    private fun getCarWidth(): Int {
        if (::scaledCarLtr.isInitialized) {
            return scaledCarLtr.iconWidth
        }

        if (::scaledCarRtl.isInitialized) {
            return scaledCarRtl.iconWidth
        }

        return 0

    }


    private fun updatePosition(width: Int) {
        val carWidth = getCarWidth()
        val maxWidth = width - carWidth
        position += velocity
        if (velocity > 0) {
            if (position > maxWidth) {
                position = maxWidth
                velocity = -1
            } else if (position < 0) {
                position = 0
                velocity = 1
            }
        } else {
            if (position < 0) {
                position = 0
                velocity = 1
            } else if (position > maxWidth) {
                position = maxWidth
                velocity = -1
            }
        }
    }
}