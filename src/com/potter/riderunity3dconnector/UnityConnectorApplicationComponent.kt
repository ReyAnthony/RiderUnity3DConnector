package com.potter.riderunity3dconnector

import com.intellij.openapi.components.ApplicationComponent

class UnityConnectorApplicationComponent : ApplicationComponent {

    override fun initComponent() {
        UnityConnectorServer.instance.startServer()
    }

    override fun disposeComponent() {
        UnityConnectorServer.instance.stopServer()
    }

    override fun getComponentName(): String {
        return "Rider Unity3D Connector"
    }
}