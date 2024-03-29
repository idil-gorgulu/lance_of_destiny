package org.ata_swing_ui;
import org.oguz_swing_ui.Page;

import javax.swing.*;
import java.awt.*;

public class BuildingModePage extends Page {

    private JPanel gridPanel;
    public BuildingModePage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.add(menuBar, BorderLayout.NORTH);
        add(menuContainer, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(15, 5));
        for (int i = 0; i < 75; i++) {
            JButton button = new JButton();
            button.addActionListener(e -> showOptionPopup());
            gridPanel.add(button);
        }
        add(gridPanel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Building Mode", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void showOptionPopup() {
        String[] options = {"Simple barrier", "Reinforced barrier", "Explosive barrier", "Rewarding barrier"};
        int choice = JOptionPane.showOptionDialog(null, "Which barrier",
                "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        if (choice != JOptionPane.CLOSED_OPTION) {

            // Determine the type of barrier chosen
            int barrierType = choice; //should be used to create barrier

        }
    }
}