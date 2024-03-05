package com.github.vladd.idef1loader.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil
import javax.annotation.Nullable

@State(name = "RacingProgressBarSettingsState", storages = [(Storage("racingProgressBarSettings.xml"))])
class RacingProgressBarSettingsState : PersistentStateComponent<RacingProgressBarSettingsState> {
    val teams = listOf("rbr", "mrc", "frr", "mcl", "am", "alp", "wil", "rb", "kik", "has")
    var selectedTeam = teams.random()

    @Nullable
    override fun getState() = this

    override fun loadState(state: RacingProgressBarSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        @JvmStatic
        fun getInstance() = service<RacingProgressBarSettingsState>()
    }

}