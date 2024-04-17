package org.Views;

import org.Controllers.BuildingModeController;
import org.Controllers.RunningModeController;
import org.Domain.Barrier;
import org.Domain.BarrierType;
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
import java.util.ArrayList;

public class BuildingModePage extends Page {
    private BuildingModeController buildingModeController;
    private BufferedImage backgroundImage;
    private JPanel buildingPanel;
    private JPanel buildingContainer;
    private JLabel leftBarriers;
    private JButton[] buttons;
    private int selectedButtonIndex = -1;

    public int pageNum = 1;

    public BuildingModePage() {
        super();
        this.buildingModeController = new BuildingModeController(this);
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
        leftBarriers.setBounds(50, 50, 70, 20); //
        infoContainer.add(leftBarriers);

        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.setPreferredSize(new Dimension(200, 500));

        menuContainer.add(menuBar, BorderLayout.NORTH);

        menuContainer.add(infoContainer, BorderLayout.CENTER);


        //JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //backButtonPanel.setOpaque(false); // Make the panel transparent
        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> Navigator.getInstance().showRunningModePage());
        menuContainer.add(playButton, BorderLayout.SOUTH);

        add(menuContainer, BorderLayout.WEST);

        // Adding buttons that will be used to choose type of barrier
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttons = new JButton[4]; // List of buttons
        try {
            Image img1 = ImageIO.read(new File("assets/iconbluegem.png"));
            Image img2 = ImageIO.read(new File("assets/Firm.png"));
            Image img3 = ImageIO.read(new File("assets/iconredgem.png"));
            Image img4 = ImageIO.read(new File("assets/GreenGem.png"));

            int maxWidth = 0;
            int maxHeight = 0;

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
                maxWidth = Math.max(maxWidth, button.getPreferredSize().width);
                maxHeight = Math.max(maxHeight, button.getPreferredSize().height);

                int finalI = i; //needed for the mouse listener appearently.
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
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
            Dimension buttonSize = new Dimension(maxWidth, maxHeight);
            for (JButton button : buttons) {
                button.setPreferredSize(buttonSize);
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

        buildingContainer = new JPanel();
        buildingContainer.setLayout(new BorderLayout());

        buildingPanel = new JPanel(null) { //not initializing with a builtin panel type.
            @Override
            protected void paintComponent(Graphics g) { //to set background for panel.
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        buildingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedButtonIndex != -1) { //If -1, means a button is not currently selected.
                    System.out.println("Mouse click coordinates:"+ e.getX()+" "+ e.getY());
                    addBarrierImage(new Coordinate(e.getX(), e.getY()));
                }
            }
        });


        Border border = BorderFactory.createLineBorder(Color.BLACK);
        buildingPanel.setPreferredSize(new Dimension(1080, 500)); // Set preferred size of buildingPanel
        int screenWidth = buildingPanel.getWidth();
        System.out.println("buildingPanel screenWidth"+ screenWidth);
        int screenWidth2 = buildingContainer.getWidth();
        System.out.println("buildingContainer screenWidth"+ screenWidth2);
        buildingPanel.setBorder(border);
        buildingContainer.add(buildingPanel, BorderLayout.CENTER);
        add(buildingContainer, BorderLayout.CENTER);
        JLabel statusLabel = new JLabel("Building Mode", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Set the preferred size of the buildingContainer
        buildingContainer.setPreferredSize(new Dimension(1000, 500)); // Match the size of buildingPanel


    }

    private void addBarrierImage(Coordinate coordinates) {
        if (selectedButtonIndex == -1) {
            // No barrier type selected, do nothing
            return;
        }
        //

        BarrierType type = BarrierType.values()[selectedButtonIndex];
        BuildingModeController.addBarrier(coordinates, type);
        int x = coordinates.getX();
        int y = coordinates.getY();
        Coordinate barrierCoordinates=BuildingModeController.addBarrier(coordinates,BarrierType.values()[selectedButtonIndex] );

        if (barrierCoordinates==null){
            regenerate();
            buildingPanel.repaint();
            buildingPanel.revalidate();
            return; // Exit the method after removing the barrier

        }

//        int x = barrierCoordinates.getX();
//        int y = barrierCoordinates.getY();
//
//        ImageIcon icon = null;
//        switch (selectedButtonIndex) {
//            case 0:
//                icon = new ImageIcon("assets/iconbluegem.png");
//                break;
//            case 1:
//                icon = new ImageIcon("assets/Firm.png");
//                break;
//            case 2:
//                icon = new ImageIcon("assets/iconredgem.png");
//                break;
//            case 3:
//                icon = new ImageIcon("assets/GreenGem.png");
//                break;
//            default:
//                break;
//        }
//
//
//        if (icon != null) {
//            JLabel barrierLabel = new JLabel(icon);
//            barrierLabel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
//            buildingPanel.add(barrierLabel);
//            buildingPanel.repaint();
//            buildingPanel.revalidate();
//        }
        regenerate();
        buildingPanel.repaint();
        buildingPanel.revalidate();
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
    public void regenerate(){
        buildingPanel.removeAll();
        ArrayList<Barrier> barriers;
        barriers = buildingModeController.getGameSession().getBarriers();
        for (Barrier barrier : barriers) {
            System.out.println("Building mode barrier putting: "+ barrier.getCoordinates().getX()+ barrier.getCoordinates().getY());
            barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
            buildingPanel.add(barrier);
            barrier.setBackground(Color.blue);
            barrier.setOpaque(true);
            buildingPanel.repaint();
            buildingPanel.revalidate();
        }
    }

}
