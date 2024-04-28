package org.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.Utils.ComponentStyling;

public class EnterPage extends Page {
    private BufferedImage backgroundImage;

    public EnterPage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        // Setting the layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Setting the background
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Game Title
        add(Box.createVerticalStrut(50));
        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(gameTitle);

        JButton loginButton = ComponentStyling.createStyledButton("Login");
        JButton signupButton = ComponentStyling.createStyledButton("Signup");

        // Action listeners for buttons
        loginButton.addActionListener(e -> Navigator.getInstance().showLoginPage());
        signupButton.addActionListener(e -> Navigator.getInstance().showSignupPage());

        add(Box.createVerticalStrut(10));
        add(loginButton);
        add(Box.createVerticalStrut(10));
        add(signupButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

