package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.util.io.isFile
import com.jetbrains.resharper.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.resharper.run.configurations.remote.MonoConnectRemoteProfileState
import com.jetbrains.resharper.run.configurations.remote.RemoteConfiguration
import com.jetbrains.resharper.run.configurations.remote.Unity.UnityLocalAttachConfiguration
import com.jetbrains.resharper.util.idea.getLogger
import com.jetbrains.rider.model.debuggerWorker.DebuggerStartInfoBase
import com.jetbrains.rider.model.debuggerWorker.MonoAttachStartInfo
import java.io.File
import java.nio.file.Files
import javax.swing.JOptionPane
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Created by Ivan.Shakhov on 14.02.2017.
 */
class UnityMonoConnectRemoteProfileState(runConfiguration: RemoteConfiguration, executionEnvironment: ExecutionEnvironment)
    : MonoConnectRemoteProfileState(runConfiguration, executionEnvironment) {
    private val logger = getLogger(this)

    override fun execute(executor: Executor?, runner: ProgramRunner<*>): ExecutionResult? {
        // call Unity Run
        logger.info("call Unity Run")
        JOptionPane.showMessageDialog(null, "", "InfoBox: " + "", JOptionPane.INFORMATION_MESSAGE);
        return super.execute(executor, runner)
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>, workerProcessHandler: DebuggerWorkerProcessHandler): ExecutionResult {
        logger.info("call Unity Run2")
        JOptionPane.showMessageDialog(null, "2", "InfoBox2: " + "", JOptionPane.INFORMATION_MESSAGE);
        return super.execute(executor, runner, workerProcessHandler)
    }

    override fun createModelStartInfo(): DebuggerStartInfoBase
    {
        val csprojFile = Files.walk(File(executionEnvironment.project.baseDir.path).toPath()).filter({
            it.isFile() && it.toFile().extension == "csproj"
                    && it.toFile().readText().contains("UnityEngine.dll") && it.toFile().readText().contains("<unityProcessId>")
        }).findFirst()
        val pid =
                XPathFactory.newInstance().newXPath().compile("/Project/PropertyGroup/unityProcessId")
                        .evaluate(DocumentBuilderFactory.newInstance()
                                .newDocumentBuilder().parse(csprojFile.get().toFile()), XPathConstants.NUMBER) as Double?

        val unityConfig = UnityLocalAttachConfiguration("name", pid!!.toInt())
        return MonoAttachStartInfo("localhost", unityConfig.port)
    }
}