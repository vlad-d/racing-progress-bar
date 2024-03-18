package dev.imake.idef1loader.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JPanel

class RacingProgressBarConfigurable : Configurable {
    private lateinit var settingsComponent: RacingProgressBarComponent


    override fun getDisplayName() = "Racing Progress Bar"
    override fun createComponent(): JPanel {
        settingsComponent = RacingProgressBarComponent()

        return settingsComponent.getPanel()
    }

    override fun isModified(): Boolean {
        return RacingProgressBarSettingsState.getInstance().selectedTeam != settingsComponent.getSelectedTeam()
    }

    override fun apply() {
        RacingProgressBarSettingsState.getInstance().selectedTeam = settingsComponent.getSelectedTeam()
    }

    override fun reset() {
        settingsComponent.setSelectedTeam(RacingProgressBarSettingsState.getInstance().selectedTeam)
    }

    override fun getHelpTopic() = "Racing Progress Bar"

}