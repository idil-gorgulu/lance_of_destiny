package org.Views;

import org.MultiplayerUtils.MultiPortServer;

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
    private JPanel centerPanel;
    private MultiPortServer server;
    public WaitMultiplayerGameAccept() {
        super();
        initUI();
        server = MultiPortServer.getInstance();
        Thread comm = new Thread(server::start);
        comm.start();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        waitingMessage = new JLabel("Waiting for other player to accept...");
        waitingMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingMessage.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(waitingMessage);
        centerPanel.add(Box.createVerticalStrut(20));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(20));

        cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        centerPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);

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
        // Change here later
        System.out.println("Waiting cancelled by the user.");
    }
}
