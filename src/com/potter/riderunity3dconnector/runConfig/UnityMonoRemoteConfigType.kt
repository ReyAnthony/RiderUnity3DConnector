package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.util.IconLoader

/**
 * Created by Ivan.Shakhov on 14.02.2017.
 */
class UnityMonoRemoteConfigType : ConfigurationTypeBase("ConnectRemoteToUnity", "Unity Mono remote", "Attach to Unity Mono VM",
        IconLoader.getIcon("/rider/runConfigurations/monoRemote.png")) {


    val factory: UnityDotNetRemoteConfigurationFactory = UnityDotNetRemoteConfigurationFactory(this)

    init {
        addFactory(factory)
    }
}