package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.jetbrains.resharper.run.configurations.DotNetConfigurationFactoryBase

/**
 * Created by Ivan.Shakhov on 14.02.2017.
 */
class UnityDotNetRemoteConfigurationFactory(unityMonoRemoteConfigType: UnityMonoRemoteDebugConfigType) : DotNetConfigurationFactoryBase<UnityDotNetRemoteConfiguration>(unityMonoRemoteConfigType){

    override fun createTemplateConfiguration(project: Project): RunConfiguration = UnityDotNetRemoteConfiguration(project, this, "")
}