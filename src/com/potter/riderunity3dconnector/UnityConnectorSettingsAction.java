package com.potter.riderunity3dconnector;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

public class UnityConnectorSettingsAction extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);

        SettingsDialog settingsDialog = new SettingsDialog(project, true);
        settingsDialog.show();

        int exitCode = settingsDialog.getExitCode();

        if(exitCode == SettingsDialog.OK_EXIT_CODE) {
        } else {
            settingsDialog.stopListening();
        }
    }
}
