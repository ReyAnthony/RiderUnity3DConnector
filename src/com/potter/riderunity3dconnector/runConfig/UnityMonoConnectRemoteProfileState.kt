package com.potter.riderunity3dconnector.runConfig

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.jetbrains.resharper.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.resharper.run.configurations.remote.MonoConnectRemoteProfileState
import com.jetbrains.resharper.run.configurations.remote.RemoteConfiguration
import com.jetbrains.resharper.util.idea.getLogger
import javax.swing.JOptionPane

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
}