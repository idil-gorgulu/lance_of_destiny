package org.Views;


import org.Domain.Barrier;
import org.Domain.Coordinate;
import org.Views.Page;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BuildingModePage extends Page {
    private BufferedImage backgroundImage;
    private JPanel gridPanel;
    private JLabel leftBarriers;

    public BuildingModePage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JPanel infoContainer = new JPanel(new FlowLayout());
        leftBarriers = new JLabel("Add at least this many more:");
        leftBarriers.setBounds(450, 50, 70, 20);
        infoContainer.add(leftBarriers);

        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.add(menuBar, BorderLayout.NORTH);

        menuContainer.add(infoContainer, BorderLayout.SOUTH);
        add(menuContainer, BorderLayout.WEST);


        JPanel barrierPanel = new JPanel(new FlowLayout());

        for (int i = 0; i < 4; i++) {
            JButton button = new JButton();

            if (i == 0) {
                try {
                    Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("assets/iconbluegem.png")));
                    button.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                button.setText("Simple Barrier");
            }
            button.addActionListener(e -> showOptionPopup());
            button.setOpaque(true);

            barrierPanel.add(button);
        }
        add(barrierPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                int cellWidth = gridPanel.getWidth() / 20;
//                int cellHeight = gridPanel.getHeight() / 30;
//                int column = e.getX() / cellWidth;
//                int row = e.getY() / cellHeight;
//                System.out.println(column);
//                System.out.println(row);
                // Place a barrier at the clicked cell
                addBarrier(new Coordinate(e.getX(), e.getY()));
            }
        });

        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        gridPanel.setBorder(border);

        add(gridPanel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Building Mode", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        gridPanel.setPreferredSize(new Dimension(600, 400)); // Adjust dimensions as needed
    }

    private void showOptionPopup() {
        String[] options = {"Deneme"};
        int choice = JOptionPane.showOptionDialog(null, "Which barrier",
                "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        if (choice != JOptionPane.CLOSED_OPTION) {

            int barrierType = choice; //should be used to create barrier

        }
    }

    private void addBarrier(Coordinate coordinates) {
        int barrierType = 1;
        // Create a new Barrier object with the specified coordinates and type
        Barrier barrier = new Barrier(coordinates, barrierType);
        System.out.println("barrier x:"+ barrier.getCoordinates().getX()+"barrier y:"+ barrier.getCoordinates().getY() );
        barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
        barrier.setBackground(new Color(0, 0, 0, 0));
        gridPanel.add(barrier);
        gridPanel.repaint();

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
