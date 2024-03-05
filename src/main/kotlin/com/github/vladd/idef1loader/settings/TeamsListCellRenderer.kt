package com.github.vladd.idef1loader.settings

import java.awt.Component
import java.awt.FlowLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class ListItem(val key: String, val imagePath: String)

class TeamsListCellRenderer : ListCellRenderer<ListItem> {
    override fun getListCellRendererComponent(
        list: JList<out ListItem>?,
        value: ListItem?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val panel = JPanel(FlowLayout(FlowLayout.TRAILING, 20, 4))
        panel.accessibleContext.accessibleName = "Team ${value?.key}"
        val label = JLabel(value?.key?.uppercase())
        val icon = ImageIcon(javaClass.getResource(value?.imagePath))
        val iconLabel = JLabel(icon)
        if (isSelected) {
            panel.background = list?.selectionBackground
            label.foreground = list?.selectionForeground
        } else {
            panel.background = list?.background
            label.foreground = list?.foreground
        }
        panel.add(label)
        panel.add(iconLabel)

        return panel
    }
}