package org.Views;

import org.Utils.ComponentStyling;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartPage extends Page {
    private BufferedImage backgroundImage;

    public StartPage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(Box.createVerticalStrut(50));
        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(gameTitle);

        JButton buildingModeButton = ComponentStyling.createStyledButton("Open Building Mode");
        JButton runningModeButton = ComponentStyling.createStyledButton("Open Running Mode");

        buildingModeButton.addActionListener(e -> Navigator.getInstance().showBuildingModePage());
        runningModeButton.addActionListener(e -> Navigator.getInstance().showGameSelectionPage());
        add(Box.createVerticalStrut(10));
        add(buildingModeButton);
        add(Box.createVerticalStrut(10));
        add(runningModeButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

