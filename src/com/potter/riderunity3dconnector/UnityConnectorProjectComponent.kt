package com.potter.riderunity3dconnector

import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationInfoProvider
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfigurationFactory
import com.jetbrains.resharper.run.configurations.remote.MonoAttachConfiguration
import com.jetbrains.resharper.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.resharper.run.configurations.remote.Unity.UnityLocalAttachConfiguration
import com.jetbrains.resharper.util.idea.ILifetimedComponent
import com.jetbrains.resharper.util.idea.LifetimedComponent
import com.jetbrains.resharper.util.idea.getLogger

/**
 * Created by Ivan.Shakhov on 03.02.2017.
 */


class UnityConnectorProjectComponent(val project: Project) : AbstractProjectComponent(project), ILifetimedComponent by LifetimedComponent(project) {
    companion object {
        val logger = getLogger(this)
    }
    override fun projectOpened() {
        val pid = System.getenv("unityProcessId");
        if (pid!=null) {
            var id = Integer.valueOf(pid)
            val unityConfig = UnityLocalAttachConfiguration("temp", id)
            var runManager = RunManagerEx.getInstance(project)
            var config = runManager.createRunConfiguration("unity-debug-generated", DotNetRemoteConfigurationFactory(MonoRemoteConfigType()))
            (config.configuration as DotNetRemoteConfiguration).address = unityConfig.address
            (config.configuration as DotNetRemoteConfiguration).port = unityConfig.port
            config.isTemporary=true
            runManager.addConfiguration(config, false)
            runManager.selectedConfiguration = config
        }
    }

    override fun getComponentName(): String {
        return "UnityConnectorProjectComponent"
    }

    override fun projectClosed() {

    }

    override fun initComponent() {

    }

    override fun disposeComponent() {

    }
}