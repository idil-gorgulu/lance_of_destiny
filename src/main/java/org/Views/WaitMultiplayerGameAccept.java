package org.Views;

import org.MultiplayerUtils.MultiPortServer;
import org.MultiplayerUtils.StateChangeListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaitMultiplayerGameAccept extends Page implements StateChangeListener {
    private BufferedImage backgroundImage;
    private JLabel waitingMessage;
    private JButton cancelButton;
    private JButton readyButton;
    private JProgressBar progressBar;
    private JPanel centerPanel;
    private MultiPortServer server;

    public WaitMultiplayerGameAccept() {
        super();
        server = MultiPortServer.getInstance();
        server.addStateChangeListener(this);
        Thread comm = new Thread(server::start);
        comm.start();
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

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        updateUIView();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void onStateChange() {
        SwingUtilities.invokeLater(this::updateUIView);
    }

    private void updateUIView() {
        centerPanel.removeAll();

        if (server.connected) {
            readyButton = new JButton("Ready");
            readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            readyButton.addActionListener(this::ready);
            centerPanel.add(readyButton);
        } else {
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
            cancelButton.addActionListener(e -> onCancel());
            centerPanel.add(cancelButton);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void onCancel() {
        // TODO: Implement reset logic
        Navigator.getInstance().showGameModePage();
    }

    private void ready(ActionEvent e) {
        server.selfReadyClicked = true;
        if (server.opponentReadyClicked) {
            Navigator.getInstance().showRunningModePage();
        } else {
            // Update UI to show a waiting message or similar
        }
    }
}
