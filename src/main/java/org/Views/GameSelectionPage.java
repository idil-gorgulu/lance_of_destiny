package org.Views;

import org.Controllers.DataBaseController;
import org.Domain.Game;
import org.Domain.User;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSelectionPage extends Page {

    private BufferedImage backgroundImage;
    private DataBaseController dataBaseController;

    public GameSelectionPage() {
        super();
        this.setOpaque(false);
        loadBackgroundImage();
        initUI();
        this.dataBaseController = DataBaseController.getInstance();
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


        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setOpaque(false); // Make the panel transparent
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);
        customizeButtonback(backButton);
        backButtonPanel.add(backButton, BorderLayout.WEST);
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headingPanel.setOpaque(false); // Make the panel transparent
        JLabel headingLabel = new JLabel("Your Saved Games");
        headingLabel.setForeground(Color.WHITE); // Set text color
        headingLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Set font and size
        headingPanel.add(headingLabel);
        backButtonPanel.add(headingPanel, BorderLayout.CENTER);

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

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        ArrayList<Document> games = User.getUserInstance().getAllGames();

        // Group games by name
        Map<String, List<Document>> groupedGames = new HashMap<>();
        for (Document game : games) {
            String gameName = game.getString("gameName");
            groupedGames.computeIfAbsent(gameName, k -> new ArrayList<>()).add(game);
        }

        // Using groups to show.
        for (Map.Entry<String, List<Document>> entry : groupedGames.entrySet()) {
            JPanel gamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout for left alignment
            gamePanel.setOpaque(false);

            for (Document game : entry.getValue()) { //creating buttons
                JButton gameButton = new JButton("<html>" + game.getString("gameName") + "<br><small>" + game.getString("gameDate") + "</small></html>");
                customizeButton(gameButton);
                gameButton.setPreferredSize(new Dimension(180, 80  )); // Set preferred size for uniformity
                gameButton.setMaximumSize(new Dimension(180, 80)); // Set maximum size for uniformity
                gameButton.setMinimumSize(new Dimension(180, 80)); // Set minimum size for uniformity
                gameButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dataBaseController.openFromDatabase(game);
                        Navigator.getInstance().showRunningModePage();

                    }
                });
                gamePanel.add(gameButton);
            }

            formPanel.add(gamePanel);
        }

        // formPanel is scrollable
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //dont show if not needed
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

    private void customizeButton(JButton button) {
        button.setBackground(new Color(100, 149, 237));
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
