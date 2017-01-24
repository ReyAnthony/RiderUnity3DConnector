package com.potter.riderunity3dconnector;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectBaseDirectory;
import com.jetbrains.resharper.projectView.ideaInterop.RiderProjectOpenProcessor;
import com.jetbrains.resharper.protocol.components.SolutionHost;
import com.jetbrains.rider.model.OpenExistingSolution;
import com.jetbrains.rider.model.SolutionOpenStrategy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnityConnectorServer {

    public static final UnityConnectorServer INSTANCE = new UnityConnectorServer();
    private ServerRunnable serverRunnable;
    private ExecutorService executorService;
    private volatile DatagramSocket datagramSocket;
    private int port = 11234;
    private Project project;

    private UnityConnectorServer() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static UnityConnectorServer getInstance() {
        return INSTANCE;
    }

    public boolean startServer() {
        if (serverRunnable != null && serverRunnable.isRunning()) {
            // Server is already running
            return true;
        }
        try {
            datagramSocket = new DatagramSocket(port);
            serverRunnable = new ServerRunnable();
            executorService.execute(serverRunnable);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopServer() {
        if (serverRunnable == null) {
            // Server is not started
            return;
        }
        serverRunnable.stop();
    }

    public boolean isRunning() {
        if (serverRunnable == null) {
            return false;
        }
        return serverRunnable.isRunning();
    }

    public int getPort() {
        return port;
    }

    private class ServerRunnable implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                try {
                    byte[] data = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                    datagramSocket.receive(datagramPacket);
                    data = datagramPacket.getData();
                    String text = new String(data);
                    final String[] splits = text.split("\r\n");
                    ApplicationManager.getApplication().invokeLater(() -> {

                        String slnFilePath = splits[1];
                        SolutionOpenStrategy strategy = new OpenExistingSolution(slnFilePath, true);
                        project = RiderProjectOpenProcessor.Companion.doOpenSolution(null, false, strategy);

                        VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(splits[2]);

                        int line = Integer.parseInt(splits[0]) - 1;
                        if (line < 0) line = 0;

                        int column = 0;
                        if (splits.length == 4) {
                            column = Integer.parseInt(splits[0]) - 1;
                            if (column < 0) column = 0;
                        }

                        new OpenFileDescriptor(project, vf, line, column).navigate(true);
                    });

                    InetAddress senderAddress = datagramPacket.getAddress();
                    int senderPort = datagramPacket.getPort();
                    String sendDataRaw = "ok";
                    byte[] sendData = sendDataRaw.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, senderAddress, senderPort);
                    datagramSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    stop();
                }
            }
        }

        public synchronized void stop() {
            isRunning = false;
            datagramSocket.close();
        }

        public synchronized boolean isRunning() {
            return isRunning;
        }
    }
}
