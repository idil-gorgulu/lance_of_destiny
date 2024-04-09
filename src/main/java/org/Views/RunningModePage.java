package org.Views;

import org.Controllers.MagicalStaffController;
import org.Domain.*;
import org.Controllers.RunningModeController;

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
    private JPanel gameContainer;

    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Barrier barrier;
    //I dont think this is a good way
    private RunningModeController runningModeController;

    private MagicalStaffController magicalStaffController;

    private Chance chance;
    private Score score;
    private ArrayList<Barrier> barriers;
    private JPanel infoContainer;

    private int screenWidth;
    private int screenHeight;

    public RunningModePage() {
        super();
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
    protected void paintComponent(Graphics g) { //background for the whole frame
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    private void setupTimer() {
        int delay = 4; // Roughly 60 FPS, adjust as needed
        Timer timer = new Timer(delay, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        runningModeController.checkCollision();
        runningModeController.moveFireball();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runningModeController.run();
                gamePanel.repaint();
                repaint();
            }
        });
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
                String hexCode = "#FFFFFF";
                Color color = Color.decode(hexCode);
                infoContainer = new JPanel(new FlowLayout());
                infoContainer.setPreferredSize(new Dimension(190, 500));
                JLabel info1 = new JLabel("<html>add labels and <br>buttons here</html>");
                info1.setBounds(50, 50, 70, 20); //
                info1.setForeground(Color.WHITE);
                infoContainer.add(info1);
                //infoContainer.setBackground(color);
                infoContainer.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50)); // Here, 128 represents the alpha value (semi-transparent)
                //We can add the buttons and labels in this infoContainer. I made its background a transparent white. -sebnem
                add(infoContainer, BorderLayout.WEST);
//                try {
//                    backgroundImage = ImageIO.read(new File("assets/Background.png"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                JLabel statusLabel = new JLabel("Running Mode", SwingConstants.CENTER);
                add(statusLabel, BorderLayout.SOUTH);
                statusLabel.setBackground(Color.lightGray); // Set background color
                statusLabel.setOpaque(true);

                gameContainer=new JPanel();
                gameContainer.setLayout(null);

                gamePanel= new JPanel();
                //{
//                    @Override
//                    protected void paintComponent(Graphics g) { //to set background for panel.
//                        super.paintComponent(g);
//                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
//                    }
//                };
                gamePanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)); // Here, 128 represents the alpha value (semi-transparent)

                gamePanel.setLayout(null);
                //gamePanel.setPreferredSize(new Dimension(400, 500)); // Set preferred size of gamePanel
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
                int screenWidth = 1000;
                System.out.println("screenWidth"+ screenWidth);
                int screenHeight = 500;

                // Initializing Fireball
                fireball = runningModeController.getGameSession().getFireball();
                int fireballWidth = fireball.getPreferredSize().width;
                int fireballPositionX = (screenWidth - fireballWidth) / 2;
                int fireballHeight = fireball.getPreferredSize().height;
                int fireballPositionY = (screenHeight - fireballHeight - 200);
                fireball.getCoordinate().setX(fireballPositionX);
                fireball.getCoordinate().setY(fireballPositionY);
                fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
                //fireball.setBackground(Color.red);
                fireball.setOpaque(false);
                gamePanel.add(fireball);
                gamePanel.repaint();
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
                gamePanel.repaint();
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

                barrier = runningModeController.getGameSession().getBarrier();
                int barrierWidth = barrier.getPreferredSize().width;
                int barrierHeight = barrier.getPreferredSize().height;
                int barrierPositionX =  400;
                int barrierPositionY =  300;
                barrier.getCoordinates().setX(barrierPositionX);
                barrier.getCoordinates().setY(barrierPositionY);
                barrier.setBounds(barrierPositionX, barrierPositionY, barrierWidth, barrierHeight);
                barrier.setBackground(Color.blue);
                gamePanel.add(barrier);
                gamePanel.repaint();
                gamePanel.revalidate();

                chance= runningModeController.getGameSession().getChance();
                chance.setBounds(chance.getCoordinate().getX(), chance.getCoordinate().getY(), chance.getPreferredSize().width, chance.getPreferredSize().height);
                chance.setBackground(Color.lightGray);
                infoContainer.add(chance).setVisible(true);
                infoContainer.repaint();
                infoContainer.revalidate();

                score= runningModeController.getGameSession().getScore();
                score.setBounds(score.getCoordinate().getX(), score.getCoordinate().getY(), score.getPreferredSize().width, score.getPreferredSize().height);
                score.setBackground(Color.lightGray);
                infoContainer.setVisible(true);
                infoContainer.repaint();
                infoContainer.revalidate();

                // Initialize Barriers
                barriers = runningModeController.getGameSession().getBarriers();
                for (Barrier barrier : barriers) {
                    barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
                    gamePanel.add(barrier);
                    barrier.setOpaque(false);
                    gamePanel.repaint();
                    gamePanel.revalidate();
                }

                gamePanel.repaint();
                repaint();
                screenWidth = gameContainer.getWidth();
                System.out.println("screenWidth2"+ screenWidth);
            }
        });
    }


}