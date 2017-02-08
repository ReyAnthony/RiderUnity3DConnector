package com.potter.riderunity3dconnector

import com.intellij.execution.RunManagerEx
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.util.io.isFile
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.resharper.run.configurations.remote.DotNetRemoteConfigurationFactory
import com.jetbrains.resharper.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.resharper.run.configurations.remote.Unity.UnityLocalAttachConfiguration
import com.jetbrains.resharper.util.idea.ILifetimedComponent
import com.jetbrains.resharper.util.idea.LifetimedComponent
import com.jetbrains.resharper.util.idea.getLogger
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Created by Ivan.Shakhov on 03.02.2017.
 */


class UnityConnectorProjectComponent(val project: Project) : AbstractProjectComponent(project), ILifetimedComponent by LifetimedComponent(project) {
    companion object {
        val logger = getLogger(this)
    }
    override fun projectOpened() {

        val csprojFile = Files.walk(File(project.baseDir.path).toPath()).filter({
            it.isFile() && it.toFile().extension == "csproj"
                    && it.toFile().readText().contains("UnityEngine.dll") && it.toFile().readText().contains("<unityProcessId>")
        }).findFirst()
        if (csprojFile.isPresent) {
            val pid =
                    XPathFactory.newInstance().newXPath().compile("/Project/PropertyGroup/unityProcessId")
                            .evaluate(DocumentBuilderFactory.newInstance()
                                    .newDocumentBuilder().parse(csprojFile.get().toFile()), XPathConstants.NUMBER) as Double?

            if (pid != null) {
                val unityConfig = UnityLocalAttachConfiguration("temp", pid.toInt())
                var runManager = RunManagerEx.getInstance(project)
                var config = runManager.createRunConfiguration("unity-debug-generated", DotNetRemoteConfigurationFactory(MonoRemoteConfigType()))
                (config.configuration as DotNetRemoteConfiguration).address = unityConfig.address
                (config.configuration as DotNetRemoteConfiguration).port = unityConfig.port
                config.isTemporary = true
                runManager.addConfiguration(config, false)
                runManager.selectedConfiguration = config
            }
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