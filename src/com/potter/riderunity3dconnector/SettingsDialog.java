package com.potter.riderunity3dconnector;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class SettingsDialog extends DialogWrapper {

    private Project project;

    protected SettingsDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;
        init();
        setTitle("Unity3D Connector Status");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        return createSettingsPanel();
    }

    private JPanel createSettingsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridLayout(2,2));

        JLabel startServerLabel = new JLabel();
        startServerLabel.setText("Listening Unity: ");

        JCheckBox cb = new JCheckBox();
        cb.setSelected(UnityConnectorServer.getInstance().isRunning());
        cb.addActionListener(e -> {
            JCheckBox cb1 = (JCheckBox)e.getSource();
            if(cb1.isSelected()) {
                boolean started = UnityConnectorServer.getInstance().startServer();
                cb1.setSelected(started);
            } else {
                UnityConnectorServer.getInstance().stopServer();
            }
        });

        fieldsPanel.add(startServerLabel);
        fieldsPanel.add(cb);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    public void stopListening() {
        UnityConnectorServer.getInstance().stopServer();
    }
}
