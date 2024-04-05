package org.Views;

import org.Domain.Barrier;
import org.Domain.Coordinate;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BuildingModePage extends Page {
    private BufferedImage backgroundImage;
    private JPanel gridPanel;
    private JLabel leftBarriers;
    private JButton[] buttons;
    private int selectedButtonIndex = -1;

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

        menuContainer.add(infoContainer, BorderLayout.CENTER);
        add(menuContainer, BorderLayout.WEST);

        // Adding buttons that will be used to choose type of barrier
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttons = new JButton[4]; // List of buttons
        try {
            Image img1 = ImageIO.read(new File("assets/iconbluegem.png"));
            Image img2 = ImageIO.read(new File("assets/Firm.png"));
            Image img3 = ImageIO.read(new File("assets/iconredgem.png"));
            Image img4 = ImageIO.read(new File("assets/GreenGem.png"));
            for (int i = 0; i < buttons.length; i++) {
                JButton button = new JButton();
                switch (i) {
                    case 0:
                        button.setIcon(new ImageIcon(img1));
                        break;
                    case 1:
                        button.setIcon(new ImageIcon(img2));
                        break;
                    case 2:
                        button.setIcon(new ImageIcon(img3));
                        break;
                    case 3:
                        button.setIcon(new ImageIcon(img4));
                        break;
                    default:
                        break;
                }
                int finalI = i; //needed for the mouse listener appearently.
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedButtonIndex == finalI) {
                            selectedButtonIndex = -1; // Deselect the button
                        } else {
                            selectedButtonIndex = finalI; // Select the button
                        }
                        updateButtonState();
                        System.out.println("selected button: "+selectedButtonIndex); //To keep track on the console.
                    }
                });
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                buttonPanel.add(button);
                buttons[i] = button; //Keeping in a list.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(buttonPanel, BorderLayout.NORTH);

        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridPanel = new JPanel(null) { //not initializing with a builtin panel type.
            @Override
            protected void paintComponent(Graphics g) { //to set background for panel.
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedButtonIndex != -1) { //If -1, means a button is not currently selected.
                    System.out.println("Mouse click coordinates:"+ e.getX()+" "+ e.getY());
                    addBarrier(new Coordinate(e.getX(), e.getY()));
                }
            }
        });

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        gridPanel.setBorder(border);
        add(gridPanel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Building Mode", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        gridPanel.setSize(new Dimension(400, 500)); // Adjust dimensions as needed
    }

    private void addBarrier(Coordinate coordinates) {
        int barrierType = selectedButtonIndex;
        //Create a new Barrier object with the specified coordinates and type
        Barrier barrier = new Barrier(coordinates, barrierType);
        barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
        barrier.setBackground(new Color(0, 0, 0, 0)); //background is transparent.
        gridPanel.add(barrier);
        gridPanel.repaint();
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    private void updateButtonState() {
        for (int i = 0; i < buttons.length; i++) { //Initially reset all buttons.
            buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
            buttons[i].setBackground(null);
        }
        if (selectedButtonIndex != -1) {
            buttons[selectedButtonIndex].setBorder(BorderFactory.createLoweredBevelBorder()); // Highlight selected button
            buttons[selectedButtonIndex].setBackground(Color.GREEN); // Make selected button green
        }
    }
}
