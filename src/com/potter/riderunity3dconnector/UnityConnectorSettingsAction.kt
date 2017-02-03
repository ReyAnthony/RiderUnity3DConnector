package com.potter.riderunity3dconnector

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE

class UnityConnectorSettingsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT)

        val settingsDialog = SettingsDialog(project, true)
        settingsDialog.show()

        val exitCode = settingsDialog.exitCode

        if (exitCode == OK_EXIT_CODE) {
        } else {
            settingsDialog.stopListening()
        }
    }
}
