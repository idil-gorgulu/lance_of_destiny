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
        setLayout(new BorderLayout());  // Ana panelin düzenini BorderLayout olarak değiştirin
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Back Button Paneli oluşturuluyor ve BorderLayout.NORTH'a ekleniyor
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);  // Şeffaf yapılıyor, arka plan gözüksün diye
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

        // Merkez panel, içeriklerin düzenlendiği yer
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);  // Şeffaf yapılıyor
        centerPanel.add(Box.createVerticalStrut(50));  // İçeriği biraz aşağı indirmek için ayarlama
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

        add(centerPanel, BorderLayout.CENTER);  // Merkez paneli ekleniyor
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
