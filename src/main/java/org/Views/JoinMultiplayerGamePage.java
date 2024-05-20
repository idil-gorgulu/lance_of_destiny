package org.Views;

import org.Controllers.DataBaseController;
import org.Domain.MultiPlayerGame;
import org.Domain.User;
import org.MultiplayerUtils.MultiPortClient;
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

public class JoinMultiplayerGamePage extends Page {

    public MultiPlayerGame mpgame;

    public BufferedImage backgroundImage;
    public DataBaseController dataBaseController;

    public JoinMultiplayerGamePage(){
        super();
        try {
            mpgame = new MultiPlayerGame();
        } catch (Exception e) {

        }
        initUI();
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

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false); // Make the panel transparent
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backButtonPanel.add(backButton);

        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);

        customizeButtonback(backButton);
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

        // Panel for the rest of the login form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        ArrayList<Document> games =  new ArrayList<>();
        try {
            games = mpgame.getAllAvailableGames();

        } catch (Exception e) {
        }
        System.out.println(games);
        for (int  i = 0; i < games.size(); i++) {
            Document game = games.get(i);
            JButton gameButton = new JButton(game.getString("gameName"));
            customizeButton(gameButton);
            gameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // First create the connection between the host and the client
                    MultiPortClient client = new MultiPortClient(game.getString("localIP"), Integer.parseInt(game.getString("inPort")), Integer.parseInt(game.getString("outPort")));
                    Thread comm = new Thread(client::start);
                    comm.start();

                    dataBaseController.openMultiplayerGame(game.getString("gameName"));
                    // Final statement will be this
                    Navigator.getInstance().showRunningModePage();
                }
            });
            formPanel.add(gameButton);
            gameButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, gameButton.getPreferredSize().height));
            gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.VERTICAL;

        // Additional padding to ensure the formPanel is visually centered
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(formPanel, gbc);

        // Adding the centered panel to the center of the border layout
        add(centerPanel, BorderLayout.CENTER);

        /*
        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(gameTitle);

        JButton joinButton = new JButton("Join");

        //TODO: We need to implement the following action listener.
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostIP = hostIPAddressField.getText();
                String port = hostPortField.getText();
                System.out.println("Host IP Address: " + hostIP);
                System.out.println("Host Port: " + port);
            }
        });

        Dimension textFieldSize = new Dimension(200, 30);
        hostIPAddressField = new JTextField(20);
        hostPortField = new JTextField(10);
        hostIPAddressField.setMaximumSize(textFieldSize);
        hostPortField.setMaximumSize(textFieldSize);

        add(Box.createVerticalStrut(20));
        JLabel hostIPAddressLabel = new JLabel("Host IP Address:");
        hostIPAddressLabel.setForeground(Color.WHITE);
        add(hostIPAddressLabel);
        add(hostIPAddressField);

        JLabel hostPortLabel = new JLabel("Host Port:");
        hostPortLabel.setForeground(Color.WHITE);
        add(hostPortLabel);
        add(hostPortField);
        add(joinButton);
         */
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
