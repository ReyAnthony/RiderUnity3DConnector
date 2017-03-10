package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.util.io.isFile
import com.jetbrains.resharper.util.idea.ILifetimedComponent
import com.jetbrains.resharper.util.idea.LifetimedComponent
import com.jetbrains.resharper.util.idea.getLogger
import java.io.File
import java.nio.file.Files

class UnityConnectorProjectComponent(val project: Project) : AbstractProjectComponent(project), ILifetimedComponent by LifetimedComponent(project) {
    private val logger = getLogger(this)
//    private var configDebug:RunnerAndConfigurationSettings? = null
    private var configRunDebug:RunnerAndConfigurationSettings? = null

    override fun projectOpened() {
        fun runnerAndConfigurationSettingsRunDebug(name: String, runManager: RunManager, needRun: Boolean): RunnerAndConfigurationSettings {

            var config:RunnerAndConfigurationSettings?
//            if (!needRun)
//                config = runManager.createRunConfiguration(name, DotNetRemoteConfigurationFactory(MonoRemoteConfigType()))
//            else
                config = runManager.createRunConfiguration(name, UnityDotNetRemoteConfigurationFactory(UnityMonoRemoteDebugConfigType()))

            config.isTemporary = true
            runManager.addConfiguration(config, false)
            return config
        }

        val csprojFile = Files.walk(File(project.baseDir.path).toPath()).filter({
            it.isFile() && it.toFile().extension == "csproj"
                    && it.toFile().readText().contains("UnityEngine.dll") && it.toFile().readText().contains("<unityProcessId>")
        }).findFirst()

        if (csprojFile.isPresent) {
                val runManager = RunManagerEx.getInstance(project)

         //       configDebug = runnerAndConfigurationSettingsRunDebug("Debug (unity-generated)", runManager, false)
                configRunDebug = runnerAndConfigurationSettingsRunDebug("Run and Debug (unity-generated)", runManager, true)
//                runManager.selectedConfiguration = configDebug
        }
    }
    override fun getComponentName(): String {
        return "UnityConnectorProjectComponent"
    }

    override fun projectClosed() {
        val runManager = RunManagerEx.getInstanceEx(project)
//        if (configDebug != null)
//            runManager.removeConfiguration(configDebug)
        if (configRunDebug != null)
            runManager.removeConfiguration(configRunDebug)

    }

    override fun initComponent() {

    }

    override fun disposeComponent() {

    }
}