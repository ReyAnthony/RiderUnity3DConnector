package com.potter.riderunity3dconnector;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class SettingsDialog extends DialogWrapper {

    private Project project;

    protected SettingsDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;
        init();
        setTitle("GotoCode Settings");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        return createSettingsPanel();
    }

    private JPanel createSettingsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridLayout(2,2));

        JLabel portLabel = new JLabel();
        portLabel.setText("Port: ");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumIntegerDigits(5);
        numberFormat.setMaximumFractionDigits(5);

        final JFormattedTextField  portField = new JFormattedTextField(numberFormat);
        portField.setText(Integer.toString(UnityConnectorServer.getInstance().getPort()));
        portField.setColumns(5);

        JLabel startServerLabel = new JLabel();
        startServerLabel.setText("Start listening: ");

        JCheckBox cb = new JCheckBox();
        cb.setSelected(UnityConnectorServer.getInstance().isRunning());
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(portField.getText());
                JCheckBox cb = (JCheckBox)e.getSource();
                if(cb.isSelected()) {
                    boolean started = UnityConnectorServer.getInstance().startServer(port, project);
                    cb.setSelected(started);
                } else {
                    UnityConnectorServer.getInstance().stopServer();
                }
            }
        });


        fieldsPanel.add(portLabel);
        fieldsPanel.add(portField);
        fieldsPanel.add(startServerLabel);
        fieldsPanel.add(cb);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    public void stopListening() {
        UnityConnectorServer.getInstance().stopServer();
    }
}
