package com.potter.riderunity3dconnector

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class SettingsDialog(project: Project?, canBeParent: Boolean) : DialogWrapper(project, canBeParent) {

    init {
        init()
        title = "Unity3D Connector Status"
    }

    override fun createCenterPanel(): JComponent? {

        return createSettingsPanel()
    }

    private fun createSettingsPanel(): JPanel {
        val mainPanel = JPanel(BorderLayout())
        val fieldsPanel = JPanel(GridLayout(2, 2))

        val startServerLabel = JLabel()
        startServerLabel.text = "Listening Unity: "

        val cb = JCheckBox()
        cb.isSelected = UnityConnectorServer.instance.isRunning
        cb.addActionListener { e ->
            val cb1 = e.source as JCheckBox
            if (cb1.isSelected) {
                val started = UnityConnectorServer.instance.startServer()
                cb1.isSelected = started
            } else {
                UnityConnectorServer.instance.stopServer()
            }
        }

        fieldsPanel.add(startServerLabel)
        fieldsPanel.add(cb)

        mainPanel.add(fieldsPanel, BorderLayout.CENTER)
        return mainPanel
    }

    fun stopListening() {
        UnityConnectorServer.instance.stopServer()
    }
}
