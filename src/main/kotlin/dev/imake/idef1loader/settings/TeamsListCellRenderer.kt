package dev.imake.idef1loader.settings

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

        // Load the icon safely, handling null resources
        val iconLabel = value?.imagePath?.let { imagePath ->
            val iconUrl = javaClass.getResource(imagePath)
            if (iconUrl != null) {
                JLabel(ImageIcon(iconUrl))
            } else {
                JLabel() // Empty label if resource not found
            }
        } ?: JLabel()

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