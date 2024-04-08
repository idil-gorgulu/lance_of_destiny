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

    private Fireball fireball;
    private BufferedImage backgroundImage;
    private JPanel gamePanel;
    private JPanel gameContainer;
    private MagicalStaff magicalStaff;
    private Barrier barrier;
    //I dont think this is a good way
    private RunningModeController runningModeController;

    private MagicalStaffController magicalStaffController;

    private Chance chance;
    private Score score;
    private ArrayList<Barrier> barriers;
    private JPanel infoContainer;

    public RunningModePage() {
        super();
        this.runningModeController = new RunningModeController(this);
        this.magicalStaffController = new MagicalStaffController(this);
        initUI();
        setFocusable(true);
        requestFocus();
        setupTimer();
    }

    private void setupTimer() {
        int delay = 4; // Roughly 60 FPS, adjust as needed
        Timer timer = new Timer(delay, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        checkCollision();
        this.runningModeController.getGameSession().getFireball().moveFireball();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                magicalStaff = runningModeController.getGameSession().getMagicalStaff();
                magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);
                fireball = runningModeController.getGameSession().getFireball();
                fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireball.getWidth(), fireball.getPreferredSize().height);
                barrier = runningModeController.getGameSession().getBarrier();
                barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getWidth(), barrier.getHeight());
                //System.out.println(barrier.getCoordinates().getX());
                //System.out.println(barrier.getCoordinates().getY());
                chance= runningModeController.getGameSession().getChance();
                chance.setBounds(chance.getCoordinate().getX(), chance.getCoordinate().getY(), chance.getWidth(), chance.getHeight());

                score= runningModeController.getGameSession().getScore();
                score.setBounds(score.getCoordinate().getX(), score.getCoordinate().getY(), score.getWidth(), score.getHeight());
                for (Barrier barrier : barriers) {
                    barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getWidth(), barrier.getHeight());
                }

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
                String hexCode = "#1D3986";
                Color color = Color.decode(hexCode);
                infoContainer = new JPanel(new FlowLayout());
                infoContainer.setPreferredSize(new Dimension(200, 500));
                JLabel info1 = new JLabel("Add at least this many more:");
                info1.setBounds(50, 50, 70, 20); //
                infoContainer.add(info1);
                infoContainer.setBackground(color);
                add(infoContainer, BorderLayout.WEST);

                try {
                    backgroundImage = ImageIO.read(new File("assets/Background.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JLabel statusLabel = new JLabel("Running Mode", SwingConstants.CENTER);
                add(statusLabel, BorderLayout.SOUTH);

                gameContainer=new JPanel();
                gameContainer.setLayout(null);

                gamePanel= new JPanel(){
                    @Override
                    protected void paintComponent(Graphics g) { //to set background for panel.
                        super.paintComponent(g);
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
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
                int barrierPositionX =  300;
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


    private void checkCollision() {
        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();

        int magicalStaffX = magicalStaff.getCoordinate().getX();
        int magicalStaffY = magicalStaff.getCoordinate().getY();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;
        double magicalStaffAngle = magicalStaff.getAngle();


        int xVelocity = fireball.getxVelocity();
        int yVelocity = fireball.getyVelocity();
        double normalAngle = (magicalStaffAngle + 90) % 360;

        Rectangle staffRect = new Rectangle(magicalStaffX, magicalStaffY, magicalStaffWidth, magicalStaffHeight);
        Rectangle fireballRect = new Rectangle(fireballX - fireballRadius, fireballY - fireballRadius, fireballRadius * 2, fireballRadius * 2);
        Rectangle barrierRect = new Rectangle(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), (int) barrier.getPreferredSize().getWidth(), (int) barrier.getPreferredSize().getHeight());

        if (staffRect.intersects(fireballRect)) {
            // The collision formula: Vnew = b * (-2*(V dot N)*N + V)
            // b: 1 for elastic collision, 0 for 100% moment loss
            // V: previous velocity vector
            // N: normal vector of the surface collided with
            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians(normalAngle);
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());
        } else if (barrierRect.intersects(fireballRect)) {

            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians((double) (90%360));
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());
        }

        int containerWidth = getWidth();
        int containerHeight = getHeight();

        // Check collision with left and right boundaries
        if (fireballX - fireballRadius <= 0 || fireballX + fireballRadius >= containerWidth-200) {
            xVelocity *= -1; // Reverse X velocity
            fireball.setxVelocity(xVelocity);
        }

        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <=10|| fireballY + fireballRadius >= 600) {
            yVelocity *= -1; // Reverse Y velocity
            fireball.setyVelocity(yVelocity);
        }
    }
    public void startGame() {
        int screenWidth = gamePanel.getWidth();
        System.out.println("screenWidth: " + screenWidth);
    }

}
