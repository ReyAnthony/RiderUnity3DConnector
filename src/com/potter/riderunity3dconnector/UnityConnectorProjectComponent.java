package com.potter.riderunity3dconnector;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class UnityConnectorProjectComponent implements ProjectComponent {

    final Project project;

    public UnityConnectorProjectComponent(Project project) {
        super();
        this.project = project;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {
        UnityConnectorServer.getInstance().startServer(11234, project);
    }

    @Override
    public void disposeComponent() {
        UnityConnectorServer.getInstance().stopServer();
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Rider Unity3D Connector";
    }
}