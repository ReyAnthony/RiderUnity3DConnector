package com.potter.riderunity3dconnector;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class UnityConnectorApplicationComponent implements ApplicationComponent {

    public UnityConnectorApplicationComponent() {
        super();
    }

    @Override
    public void initComponent() {
        UnityConnectorServer.getInstance().startServer();
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