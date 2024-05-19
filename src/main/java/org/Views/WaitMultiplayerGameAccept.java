package org.Views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaitMultiplayerGameAccept extends Page {
    private BufferedImage backgroundImage;
    private JLabel waitingMessage;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JPanel centerPanel;  // Panel to hold the centered components

    public WaitMultiplayerGameAccept() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());  // Set the layout to BorderLayout for overall frame

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);  // Make the panel transparent

        // Message label
        waitingMessage = new JLabel("Waiting for other player to accept...");
        waitingMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingMessage.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(waitingMessage);
        centerPanel.add(Box.createVerticalStrut(20));  // Add space between components

        // Progress bar as an indeterminate loader
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(20));

        // Cancel button to stop waiting and return
        cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        centerPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);  // Add the center panel to the middle of the BorderLayout

        // Set the background panel to add some padding
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void onCancel() {
        // Handle the cancel action (e.g., close the window or return to the previous screen)
        System.out.println("Waiting cancelled by the user.");
    }
}
