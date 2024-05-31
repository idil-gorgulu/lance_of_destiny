package org.Views;

import org.Utils.ComponentStyling;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartSingleplayerPage extends Page {
    private BufferedImage backgroundImage;

    public StartSingleplayerPage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().showGameModePage());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        centerPanel.add(Box.createVerticalStrut(50));
        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        centerPanel.add(gameTitle);

        JButton buildingModeButton = ComponentStyling.createStyledButton("Open Building Mode");
        JButton runningModeButton = ComponentStyling.createStyledButton("Open Running Mode");

        buildingModeButton.addActionListener(e -> Navigator.getInstance().showBuildingModePage());
        runningModeButton.addActionListener(e -> Navigator.getInstance().showGameSelectionPage());

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(buildingModeButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(runningModeButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
