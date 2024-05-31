package org.Views;

import org.Controllers.DataBaseController;
import org.Domain.Game;
import org.Domain.User;
import org.MultiplayerUtils.CountdownStateChangeListener;
import org.MultiplayerUtils.MultiPortServer;
import org.MultiplayerUtils.ConnectedStateChangeListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaitMultiplayerGameAccept extends Page implements ConnectedStateChangeListener, CountdownStateChangeListener {
    private BufferedImage backgroundImage;
    private JLabel waitingMessage;
    private JButton cancelButton;
    private JButton readyButton;
    private JProgressBar progressBar;
    private JPanel centerPanel;
    private MultiPortServer server;
    private int countdownValue = 3; // starting from 3 seconds
    private Game multiplayerGame;

    DataBaseController dataBaseController;


    public WaitMultiplayerGameAccept() {
        super();
        server = MultiPortServer.getInstance();
        server.addStateChangeListener(this);
        server.addCountdownStateChangeListener(this);
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
    public void onConnectedStateChange() {
        SwingUtilities.invokeLater(this::updateUIView);
    }

    @Override
    public void onCountdownStateChange() {
        SwingUtilities.invokeLater(this::startCountdown);
    }

    private void startCountdown() {
        while (countdownValue >= 0) {
            System.out.println(countdownValue); // Print the current countdown value
            countdownValue--; // Decrement the countdown value

            try {
                Thread.sleep(1000); // Wait for 1 second before continuing
            } catch (InterruptedException e) {
                System.err.println("Countdown was interrupted!");
                break;
            }
        }

        System.out.println("Go!"); // Print "Go!" when the countdown finishes
        // Set the game in here
        DataBaseController.getInstance().openMultiplayerGame(User.getUserInstance().getMultiplayerGameName());
        multiplayerGame = Game.getInstance();
        MultiPortServer.getInstance().setMultiplayerGame(multiplayerGame);
        server.gameStarted = true;
        Game.getInstance().comm = server;
        Navigator.getInstance().showMultiplayerRunningModePage();
    }

    private void updateUIView() {
        centerPanel.removeAll();

        if (server.connected) {
            readyButton = new JButton("Ready");
            readyButton.addActionListener(this::ready);
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
