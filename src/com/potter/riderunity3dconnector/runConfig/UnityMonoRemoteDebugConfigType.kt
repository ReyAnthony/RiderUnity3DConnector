package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

/**
 * Created by Ivan.Shakhov on 14.02.2017.
 */
class UnityMonoRemoteDebugConfigType : ConfigurationTypeBase("ConnectRemoteToUnityDebugAndRun", "Unity Mono remote", "Attach to Unity Mono VM",
        AllIcons.General.Debug) {

    val factory: UnityDotNetRemoteConfigurationFactory = UnityDotNetRemoteConfigurationFactory(this)

    init {
        addFactory(factory)
    }
}