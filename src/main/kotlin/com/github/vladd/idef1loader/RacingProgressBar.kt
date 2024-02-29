package com.github.vladd.idef1loader

import com.github.vladd.idef1loader.settings.RacingProgressBarSettingsState
import com.intellij.ide.ui.laf.darcula.ui.DarculaProgressBarUI
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.JBColor
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JProgressBar
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicProgressBarUI

open class RacingProgressBar : BasicProgressBarUI() {
    private val settings = RacingProgressBarSettingsState.getInstance()
    //    private val TRACK_COLOR = Color(19, 19, 40)
    private val TRACK_COLOR = Color(90, 104, 121)
    private val SIDES_COLOR = Color(170, 170, 170)
    private val CHECKERED_FLAG_ROWS = 4
    private val CHECKERED_FLAG_COLS = 2

    private lateinit var carIcon: BufferedImage

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

        val b = progressBar.insets // area for border
        val barRectWidth = progressBar.width - (b.right + b.left)
        val barRectHeight = progressBar.height - (b.top + b.bottom)

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

        val ratio = barRectHeight.toFloat() / carIcon.height.toFloat()
        val newWidth = carIcon.width * ratio
        val scaledImage = carIcon.getScaledInstance(newWidth.toInt(), barRectHeight, java.awt.Image.SCALE_SMOOTH)
        val scaledIcon = ImageIcon(scaledImage)

        scaledIcon.paintIcon(c, g2, amountFull, 0)


    }

    override fun paintIndeterminate(g: Graphics, c: JComponent) {
        super.paintIndeterminate(g, c)
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

    private fun paintTrack(g: Graphics2D, barRectWidth: Int, barRectHeight: Int) {
        val lineHeight = 1
        val width = barRectWidth - CHECKERED_FLAG_COLS * barRectHeight / CHECKERED_FLAG_ROWS
        val height = barRectHeight
        g.color = TRACK_COLOR
        g.fillRect(0, 0, width, height)

        // paint 1 horizontal white line at the top of the bar and 1 horizontal white line at the bottom of the bar
        g.color = SIDES_COLOR
        g.fillRect(0, 0, width, lineHeight)
        g.fillRect(0, height - lineHeight, width, lineHeight)
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

}