package com.potter.riderunity3dconnector

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.jetbrains.resharper.projectView.ideaInterop.RiderProjectOpenProcessor
import com.jetbrains.rider.model.OpenExistingSolution
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UnityConnectorServer private constructor() {
    private var serverRunnable: ServerRunnable? = null
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    @Volatile private var datagramSocket: DatagramSocket? = null
    val port = 11234
    private var project: Project? = null

    fun startServer(): Boolean {
        if (serverRunnable != null && serverRunnable!!.isRunning) {
            // Server is already running
            return true
        }
        try {
            datagramSocket = DatagramSocket(port)
            serverRunnable = ServerRunnable()
            executorService.execute(serverRunnable!!)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    fun stopServer() {
        if (serverRunnable == null) {
            // Server is not started
            return
        }
        serverRunnable!!.stop()
    }

    val isRunning: Boolean
        get() {
            if (serverRunnable == null) {
                return false
            }
            return serverRunnable!!.isRunning
        }

    private inner class ServerRunnable : Runnable {
        var isRunning = true
            private set

        override fun run() {
            while (isRunning) {
                try {
                    var data = ByteArray(1024)
                    val datagramPacket = DatagramPacket(data, data.size)
                    datagramSocket!!.receive(datagramPacket)
                    data = datagramPacket.data
                    val text = String(data)
                    val splits = text.split("\r\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    ApplicationManager.getApplication().invokeLater {

                        val slnFilePath = splits[1]
                        val strategy = OpenExistingSolution(slnFilePath, true)
                        project = RiderProjectOpenProcessor.doOpenSolution(null, false, strategy)

                        val vf = LocalFileSystem.getInstance().findFileByPath(splits[2])

                        var line = Integer.parseInt(splits[0]) - 1
                        if (line < 0) line = 0

                        var column = 0
                        if (splits.size == 4) {
                            column = Integer.parseInt(splits[0]) - 1
                            if (column < 0) column = 0
                        }

                        OpenFileDescriptor(project!!, vf!!, line, column).navigate(true)

                        ProjectUtil.focusProjectWindow(project, true)
                    }

                    val senderAddress = datagramPacket.address
                    val senderPort = datagramPacket.port
                    val sendDataRaw = "ok"
                    val sendData = sendDataRaw.toByteArray()
                    val sendPacket = DatagramPacket(sendData, sendData.size, senderAddress, senderPort)
                    datagramSocket!!.send(sendPacket)
                } catch (e: IOException) {
                    e.printStackTrace()
                    stop()
                }
            }
        }

        @Synchronized fun stop() {
            isRunning = false
            datagramSocket!!.close()
        }
    }

    companion object {

        val instance = UnityConnectorServer()
    }
}
