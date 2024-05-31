package org.Views;

import org.Controllers.DataBaseController;
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
        backButtonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);
        customizeButtonback(backButton);
        backButtonPanel.add(backButton, BorderLayout.WEST);
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headingPanel.setOpaque(false);
        JLabel headingLabel = new JLabel("Your Saved Games");
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
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

        Map<String, List<Document>> groupedGames = new HashMap<>();
        for (Document game : games) {
            String gameName = game.getString("gameName");
            groupedGames.computeIfAbsent(gameName, k -> new ArrayList<>()).add(game);
        }

        for (Map.Entry<String, List<Document>> entry : groupedGames.entrySet()) {
            JPanel gamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            gamePanel.setOpaque(false);

            for (Document game : entry.getValue()) {
                JButton gameButton = new JButton("<html>" + game.getString("gameName") + "<br><small>" + game.getString("gameDate") + "</small></html>");
                customizeButton(gameButton);
                gameButton.setPreferredSize(new Dimension(180, 80  ));
                gameButton.setMaximumSize(new Dimension(180, 80));
                gameButton.setMinimumSize(new Dimension(180, 80));
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
        button.setFont(new Font("Tahoma", Font.BOLD, 18));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                ((Graphics2D) g).fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);
                super.paint(g, c);
            }
        });
    }

    private void customizeButtonback(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 18));
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
