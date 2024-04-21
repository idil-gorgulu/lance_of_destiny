package org.Views;

import org.Controllers.MagicalStaffController;
import org.Domain.*;
import org.Controllers.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RunningModePage extends Page{

    private BufferedImage backgroundImage;
    private JPanel gamePanel;
    private JPanel infoContainer;
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Barrier barrier;
    protected RunningModeController runningModeController;
    private MagicalStaffController magicalStaffController;
    private Chance chance;
    private Score score;
    private ArrayList<Barrier> barriers;
    private ArrayList<Debris> activeDebris;
    public static final int SCREENWIDTH =1000; // I wanna reach it from MagicalStaff class
    private int screenHeight;
    public int timeInSeconds = 0;
    private int frameCount = 0;

    private JLabel timeLabel;
    public RunningModePage() {
        super();
        activeDebris = new ArrayList<>();
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.runningModeController = new RunningModeController(this);
        //this.magicalStaffController = new MagicalStaffController(this); //not used. Is necessary? -sebnem
        initUI();
        setFocusable(true);
        requestFocus();
        setupTimer();
    }
    public ArrayList<Debris> getActiveDebris() {
        return activeDebris;
    }
    protected void paintComponent(Graphics g) { //background for the whole frame
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        //g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    private void setupTimer() {
        int delay = 4; // 4 ms delay, approx. 60 FPS

        Timer timer = new Timer(delay, e -> {
            updateGame(); // Perform game updates
            frameCount++; // Increment frame count each time the timer fires
            if (frameCount >= 70) { // This is not exactly 1 second
                timeInSeconds++; // Increment the second counter
                timeLabel.setText("Time: " + timeInSeconds + "s");
                frameCount = 0; // Reset frame count
            }
        });
        timer.start();
    }



    public void updateGame() {
        if (this.runningModeController.getGameSession().getChance().getRemainingChance() == 0) {
            // System.out.println("here");
            // delete this in here
        } else {
            runningModeController.moveFireball();
            runningModeController.moveStaff();
            runningModeController.checkCollision();
            runningModeController.updateDebris(); // Handle debris movement

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    runningModeController.run();
                    gamePanel.repaint();
                    repaint();
                }
            });
        }

    }

    public JPanel getGamePanel() {
        return this.gamePanel;
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        initializeGameObjects();
    }

    private void initializeGameObjects() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // infoContainer is the container that displays chance and score information.
                String hexCode = "#FFFFFF";
                Color color = Color.decode(hexCode);
                infoContainer = new JPanel(new FlowLayout());
                infoContainer.setPreferredSize(new Dimension(190, 500));
                infoContainer.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));

                timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
                infoContainer.add(timeLabel);

                // Adding infoContainer the chance and score instances which are already visual JPanels.
                infoContainer.add(runningModeController.getGameSession().getChance());
                infoContainer.add(runningModeController.getGameSession().getScore());

                add(infoContainer, BorderLayout.WEST);

                JLabel statusLabel = new JLabel("Running Mode", SwingConstants.CENTER);
                add(statusLabel, BorderLayout.SOUTH);
                statusLabel.setBackground(Color.lightGray); // Set background color
                statusLabel.setOpaque(true);

                gamePanel= new JPanel();
                gamePanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)); // Here, 128 represents the alpha value (semi-transparent)
                gamePanel.setLayout(null);
                gamePanel.addKeyListener(new MyKeyListener(runningModeController));
                gamePanel.setFocusable(true);
                gamePanel.requestFocus();
                gamePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Mouse click coordinates:"+ e.getX()+" "+ e.getY());
                    }
                });

                Border border = BorderFactory.createLineBorder(Color.BLACK);
                gamePanel.setPreferredSize(new Dimension(1000, 500)); // Set preferred size of buildingPanel
                gamePanel.addNotify();
                gamePanel.revalidate();
                gamePanel.setBorder(border);
                add(gamePanel, BorderLayout.EAST);
                //screenWidth = 1000; assigned at top and finalized
                //System.out.println("screenWidth"+ screenWidth);
                screenHeight = 500;

                // Initializing Fireball
                fireball = runningModeController.getGameSession().getFireball();
                int fireballWidth = fireball.getPreferredSize().width;
                int fireballPositionX = (SCREENWIDTH - fireballWidth) / 2;
                int fireballHeight = fireball.getPreferredSize().height;
                int fireballPositionY = (screenHeight - fireballHeight - 200);
                fireball.getCoordinate().setX(fireballPositionX);
                fireball.getCoordinate().setY(fireballPositionY);
                fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
                fireball.setBackground(Color.red);
                fireball.setOpaque(true);
                gamePanel.add(fireball);
                gamePanel.revalidate();
                //System.out.println(fireball.getCoordinate().getX());
                //System.out.println(fireball.getCoordinate().getY());

                // Initializing MagicalStaff
                magicalStaff = runningModeController.getGameSession().getMagicalStaff();
                int magicalStaffWidth = magicalStaff.getPreferredSize().width;
                int magicalStaffHeight = magicalStaff.getPreferredSize().height;

                int magicalStaffPositionX = 500;
                int magicalStaffPositionY = 550;
                magicalStaff.getCoordinate().setX(magicalStaffPositionX);
                magicalStaff.getCoordinate().setY(magicalStaffPositionY);

                magicalStaff.setBounds(magicalStaffPositionX, magicalStaffPositionY, magicalStaffWidth, magicalStaffHeight);
                magicalStaff.setBackground(Color.green);

                gamePanel.requestFocus();
                gamePanel.setFocusTraversalKeysEnabled(false);
                gamePanel.add(magicalStaff);
                gamePanel.revalidate();

                //to follow who has focus:
                gamePanel.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        System.out.println("Game panel has gained focus");
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        System.out.println("Game panel has lost focus");
                    }
                });



                // Initialize Barriers
                barriers = runningModeController.getGameSession().getBarriers();
                for (Barrier barrier : barriers) {
                    barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
                    gamePanel.add(barrier);
                    barrier.setBackground(Color.blue);
                    barrier.setOpaque(true);
                    gamePanel.revalidate();
                }

                //screenWidth = gameContainer.getWidth();  sets to 0, pls remove
                System.out.println("screenWidth2: "+ SCREENWIDTH);

            }
        });
    }




}