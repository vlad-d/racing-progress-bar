package dev.imake.idef1loader.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.util.ui.FormBuilder
import java.awt.FlowLayout
import javax.swing.JPanel

class RacingProgressBarComponent {
    private var mainPanel: JPanel
    private var teamDropdown = JBList<ListItem>()

    init {
        val settings = RacingProgressBarSettingsState.getInstance()
        val listItems = settings.teams.map { ListItem(it, "/cars/car_$it.png") }.toTypedArray()
        val selectedIndex = listItems.indexOfFirst { it.key == settings.selectedTeam }

        teamDropdown.cellRenderer = TeamsListCellRenderer()
        teamDropdown.setListData(listItems)
        if (selectedIndex >= 0) {
            teamDropdown.selectedIndex = selectedIndex
        }

        val dropdownContainer = JPanel(FlowLayout(FlowLayout.LEADING, 0, 4))
        dropdownContainer.add(teamDropdown)

        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Select team"), dropdownContainer, 1, true)
            .addComponentFillVertically( JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return mainPanel
    }

    fun getSelectedTeam(): String {
        return teamDropdown.selectedValue?.key ?: RacingProgressBarSettingsState.getInstance().selectedTeam
    }

    fun setSelectedTeam(team: String) {
        val model = teamDropdown.model
        for (i in 0 until model.size) {
            val item = model.getElementAt(i)
            if (item.key == team) {
                teamDropdown.selectedIndex = i
                break
            }
        }
    }
}