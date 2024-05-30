package org.Views;

import org.Domain.MultiPlayerGame;
import org.MultiplayerUtils.MultiPortClient;
import org.MultiplayerUtils.StateChangeListener;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.Utils.ComponentStyling.customizeButton;
import static org.Utils.ComponentStyling.customizeButtonback;

public class JoinMultiplayerGamePage extends Page implements StateChangeListener {
    private BufferedImage backgroundImage;
    private MultiPortClient inClient;
    private JPanel centerPanel;
    private JPanel backButtonPanel;
    private MultiPlayerGame mpgame;

    public JoinMultiplayerGamePage() {
        super();
        loadBackgroundImage();
        initUI();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("assets/bckg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setOpaque(false);
        add(backgroundPanel, BorderLayout.NORTH);

        backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backButtonPanel.add(backButton, BorderLayout.WEST);
        customizeButtonback(backButton);
        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        centerPanel.setOpaque(false);
        centerPanel.setFocusable(true);
        centerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                centerPanel.requestFocusInWindow();
            }
        });

        updateUI();
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void onStateChange() {
        SwingUtilities.invokeLater(this::updateUIView);
    }

    private void updateUIView() {
        centerPanel.removeAll();
        if (inClient != null && inClient.connected) {
            JButton readyButton = new JButton("Ready");
            readyButton.addActionListener(this::ready);
            centerPanel.add(readyButton);
        } else {
            JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            headingPanel.setOpaque(false);
            JLabel headingLabel = new JLabel("Join Multiplayer Game");
            headingLabel.setForeground(Color.WHITE);
            headingLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
            headingPanel.add(headingLabel);
            backButtonPanel.add(headingPanel, BorderLayout.CENTER);



            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setOpaque(false);

            ArrayList<Document> games = new ArrayList<>();
            try {
                games = mpgame.getAllAvailableGames();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Document game : games) {
                JButton gameButton = new JButton(game.getString("gameName"));
                customizeButton(gameButton);
                gameButton.setPreferredSize(new Dimension(180, 80));
                gameButton.setMaximumSize(new Dimension(180, 80));
                gameButton.setMinimumSize(new Dimension(180, 80));
                gameButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MultiPortClient client = new MultiPortClient(game.getString("localIP"), Integer.parseInt(game.getString("inPort")), Integer.parseInt(game.getString("outPort")));
                        inClient = client;
                        Thread comm = new Thread(client::start);
                        comm.start();
                        // Notify the user for connection made
                        if (client.connected) {
                            System.out.println("Communication made");
                        } else {
                            // Lets hope no else
                        }
                        // dataBaseController.openMultiplayerGame(game.getString("gameName"));
                        // Navigator.getInstance().showRunningModePage();
                    }
                });
                formPanel.add(gameButton);
            }

            JScrollPane scrollPane = new JScrollPane(formPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.weightx = 1;
            gbc.weighty = 1;

            centerPanel.add(scrollPane, gbc);
            add(centerPanel, BorderLayout.CENTER);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void ready(ActionEvent e) {
        if (inClient != null) {
            inClient.selfReadyClicked = true;
            if (inClient.opponentReadyClicked) {
                // Load the game
                Navigator.getInstance().showRunningModePage();
            } else {
                // Display a waiting message
                System.out.println("Waiting for opponent...");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
