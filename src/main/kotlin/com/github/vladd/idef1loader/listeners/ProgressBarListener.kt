package com.github.vladd.idef1loader.listeners

import com.github.vladd.idef1loader.RacingProgressBar
import com.github.vladd.idef1loader.RacingProgressBarJava
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.wm.IdeFrame
import javax.swing.UIManager

class ProgressBarListener : LafManagerListener, ApplicationActivationListener {
    override fun lookAndFeelChanged(source: LafManager) {
        updateUi()
    }

    override fun applicationActivated(ideFrame: IdeFrame) {
        updateUi()
    }

    private fun updateUi() {
        println("PROGRESSBAR Update UI")

        UIManager.put("ProgressBarUI", RacingProgressBarJava::class.java.name)
        UIManager.getDefaults()[RacingProgressBarJava::class.java.name] = RacingProgressBarJava::class.java
    }
}