package org.Views;
import org.Controllers.LoginPageController;
import org.Domain.User;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GameSelectionPage extends Page {

    private BufferedImage backgroundImage;

    public GameSelectionPage() {
        super();
        this.setOpaque(false);
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
        backButton.addActionListener(e -> Navigator.getInstance().showStartPage());
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
        ArrayList<Document> games = User.getUserInstance().getAllGames();
        System.out.println(games);
        for (int  i = 0; i < games.size(); i++) {
            Document game = games.get(i);
            JButton gameButton = new JButton(String.valueOf(i));
            customizeButton(gameButton);
            gameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO: Create the game according to the specs that comes from the game

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
    }


    private void customizeButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 18)); // Increased font size
        button.setOpaque(false); // Make button transparent so we can custom paint
        button.setContentAreaFilled(false); // Tell Swing to not fill the content area
        button.setBorderPainted(false); // Tell Swing to not paint the border
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Increased padding
        button.setFocusable(false); // Optional: Removes the focus border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Changes the cursor to a hand cursor on hover

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                g.setColor(b.getBackground());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30); // 30, 30 defines the roundness
                super.paint(g, c);
            }
        });
    }

    private void customizeButtonback(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // Set the background color of the button
        button.setForeground(Color.WHITE); // Set text color
        button.setFocusPainted(false); // Remove focus border
        button.setFont(new Font("Tahoma", Font.BOLD, 18)); // Increase font size
        button.setOpaque(true); // Set button opacity
        button.setContentAreaFilled(false); // Set if the content area is filled
        button.setBorderPainted(false); // Remove border painting
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Add padding
        button.setFocusable(false); // Remove focusability
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand cursor

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand cursor
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor()); // Change cursor to default
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                g.setColor(b.getBackground());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30); // Rounded corners
                super.paint(g, c);
            }
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}