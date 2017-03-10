package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.project.Project
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfiguration

/**
 * Created by Ivan.Shakhov on 08.03.2017.
 */
class UnityDotNetRemoteConfiguration(project: Project, factory: ConfigurationFactory, name:String): DotNetRemoteConfiguration(project, factory, name)
{

}