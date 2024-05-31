package org.Views;

import org.Utils.ComponentStyling;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartMultiplayerPage extends Page {
    private BufferedImage backgroundImage;

    public StartMultiplayerPage() {
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

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalStrut(50));
        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        centerPanel.add(gameTitle);

        JButton createMultiplayerGameButton = ComponentStyling.createStyledButton("Host a Game");
        JButton joinMultiplayerGameButton = ComponentStyling.createStyledButton("Join a Game");

        createMultiplayerGameButton.addActionListener(e -> Navigator.getInstance().showMultiplayerBuildingModePage());
        joinMultiplayerGameButton.addActionListener(e -> Navigator.getInstance().showJoinMultiplayerGamePage());
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(createMultiplayerGameButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(joinMultiplayerGameButton);

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
