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
    private JTextField hostIPAddressField;
    private JTextField hostPortField;

    public WaitMultiplayerGameAccept() {
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


        /*
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

        JButton joinButton = new JButton("Join");

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostIP = hostIPAddressField.getText();
                String port = hostPortField.getText();
                System.out.println("Host IP Address: " + hostIP);
                System.out.println("Host Port: " + port);
            }
        });
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
