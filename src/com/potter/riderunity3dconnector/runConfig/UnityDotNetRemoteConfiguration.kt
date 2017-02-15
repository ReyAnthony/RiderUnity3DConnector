package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfiguration

/**
 * Created by Ivan.Shakhov on 14.02.2017.
 */
class UnityDotNetRemoteConfiguration(project: Project, factory: ConfigurationFactory, name: String) : DotNetRemoteConfiguration(project, factory, name) {
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return UnityMonoConnectRemoteProfileState(this, environment)
    }
}