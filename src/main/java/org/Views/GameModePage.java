package org.Views;

import org.Utils.ComponentStyling;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameModePage extends Page {
    private BufferedImage backgroundImage;

    public GameModePage() {
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

        JButton singleplayerModeButton = ComponentStyling.createStyledButton("Singleplayer Mode");
        JButton multiplayerModeButton = ComponentStyling.createStyledButton("Multiplayer Mode");
        JButton helpScreenButton = ComponentStyling.createStyledButton("Help Screen");

        singleplayerModeButton.addActionListener(e -> Navigator.getInstance().showStartSingleplayerPage());
        multiplayerModeButton.addActionListener(e -> Navigator.getInstance().showStartMultiplayerPage());
        helpScreenButton.addActionListener(e -> {
            HelpScreenPage helpScreen = new HelpScreenPage();
            helpScreen.setVisible(true);
        });

        add(Box.createVerticalStrut(10));
        add(singleplayerModeButton);
        add(Box.createVerticalStrut(10));
        add(multiplayerModeButton);
        add(Box.createVerticalStrut(10));
        add(helpScreenButton);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
