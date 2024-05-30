package org.Views;
import org.Domain.*;
import org.Controllers.*;
import org.Listeners.MyKeyListener;
import org.Listeners.InventoryListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class RunningModePage extends Page implements InventoryListener{
    private JLabel hexCount = new JLabel("0");
    private JLabel overwhelmingFireballCount = new JLabel("0");
    private JLabel magicalStaffExpansionCount = new JLabel("0");
    private JLabel felixFelicisCount = new JLabel("0");
    private BufferedImage hexImage;
    private BufferedImage felixFelicisImage;
    private BufferedImage magicalStaffExpansionImage;
    private BufferedImage overwhelmingFireballImage;
    private BufferedImage backgroundImage;
    private JPanel gamePanel =  new JPanel();
    private JPanel infoContainer =  new JPanel();
    private JPanel inventoryContainer = new JPanel();
    protected RunningModeController runningModeController;
    public boolean pause = false;
    private ArrayList<Barrier> barriers;
    private ArrayList<Debris> activeDebris;
    private ArrayList<Spell> droppingSpells;
    public HashMap<SpellType,Integer> inventory;
    private ArrayList<Bullet> activeBullets;
    public static final int SCREENWIDTH =1000;
    public int screenHeight;
    public int timeInSeconds = 0;
    private int frameCount = 0;
    private Timer gameTimer =  new Timer();
    private Sound sound = new Sound();
    public static final long COLLISION_COOLDOWN = 1000; // Cooldown period in milliseconds
    private JLabel timeLabel;
    public RunningModePage() {
        super();
        this.setDoubleBuffered(true);
        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
            hexImage = ImageIO.read(new File("assets/spells/hex.png"));
            felixFelicisImage = ImageIO.read(new File("assets/spells/felix_felicis.png"));
            magicalStaffExpansionImage = ImageIO.read(new File("assets/spells/magical_staff_expansion.png"));
            overwhelmingFireballImage = ImageIO.read(new File("assets/spells/overwhelmingfb.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.runningModeController = new RunningModeController(this);
        this.runningModeController.getGameSession().started = true;
        activeDebris = runningModeController.getGameDebris();
        droppingSpells = runningModeController.getGameSpells();
        activeBullets = runningModeController.getGameBullets();
        initUI();
        setFocusable(true);
        requestFocus();
        setupTimer();
        this.runningModeController.getGameSession().getInventory().addInventoryListener(this);
    }

    protected void paintComponent(Graphics g) { //background for the whole frame
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
    /*
    TimerTask task = new TimerTask() {
            public void run() {
                // For Pausing the game
                if (pause) {
                    Object[] options = {"Continue", "Quit", "Save"};
                    int choice = JOptionPane.showOptionDialog(null,
                            "You paused",
                            "Game Paused",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (choice == JOptionPane.YES_OPTION) {
                        pause = false;
                        gamePanel.requestFocus();
                    } else if (choice == JOptionPane.NO_OPTION) {
                        pause = false;
                        runningModeController = null;
                        Navigator.getInstance().showStartSingleplayerPage();
                    } else if(choice == JOptionPane.CANCEL_OPTION) {
                        runningModeController.saveGameToDatabase();
                    }
                }
                else if (runningModeController.getGameSession().ended) {

                    if(runningModeController.getGameSession().getBarriers().isEmpty()) JOptionPane.showMessageDialog(null, "You won!");
                    else JOptionPane.showMessageDialog(null, "You lost!");
                    runningModeController = null;
                    Navigator.getInstance().showStartSingleplayerPage();
                }
                else {
                    // Update the game frame
                    SwingUtilities.invokeLater(() -> updateGameFrame());
                    // Manage time and frames
                    frameCount++;
                    if (frameCount >= 70) {
                        timeInSeconds++;
                        SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + timeInSeconds + "s"));
                        frameCount = 0;
                    }
                }
            }
        };
        gameTimer.scheduleAtFixedRate(task, delay, period);
    }
     */
    private void setupTimer() {
        int delay = 0;  // start immediately
        int period = 16; // 16 ms period for approx. 60 FPS

        TimerTask task = new TimerTask() {
            public void run() {
                // Check if the game is paused
                if (pause) {

                    return; // Skip any updates if the game is paused
                }

                // Handle end of game session
                if (runningModeController.getGameSession().ended) {
                    if (runningModeController.getGameSession().getBarriers().isEmpty())
                        JOptionPane.showMessageDialog(null, "You won!");
                    else
                        JOptionPane.showMessageDialog(null, "You lost!");
                    runningModeController = null;
                    Navigator.getInstance().showStartSingleplayerPage();
                } else {
                    // Update the game frame
                    SwingUtilities.invokeLater(() -> updateGameFrame());
                    // Manage time and frames
                    frameCount++;
                    if (frameCount >= 70) {
                        timeInSeconds++;
                        SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + timeInSeconds + "s"));
                        frameCount = 0;
                    }
                }
            }
        };



        gameTimer.scheduleAtFixedRate(task, delay, period);
    }

    public void updateGameFrame() {
        runningModeController.updateFireballView();
        runningModeController.updateMagicalStaffView();
        runningModeController.checkCollision();
        runningModeController.moveBarriers();
        runningModeController.updateDebris();
        runningModeController.updateDroppingSpells();
        runningModeController.updateHexBullets();
        runningModeController.updatePurpleBarriers();
        repaint();
        if (this.runningModeController.getGameSession().getChance().getRemainingChance() == 0 || runningModeController.getGameSession().getBarriers().isEmpty()) {
            this.runningModeController.getGameSession().getYmir().getTimer().cancel();
            this.runningModeController.getGameSession().ended = true;
        }

        SwingUtilities.invokeLater(() -> {
                runningModeController.run();
                repaint();
        });
    }

    public JPanel getGamePanel() {
        return this.gamePanel;
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        initializeGameObjects();
        //playMusic(0); TODO
    }

    private void initializeGameObjects() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // infoContainer is the container that displays chance and score information.
                String hexCode = "#FFFFFF";
                Color color = Color.decode(hexCode);
                infoContainer = new JPanel(new FlowLayout());
                infoContainer.setPreferredSize(new Dimension(190, 200));
                infoContainer.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));

                infoContainer.setOpaque(true);
                timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
                infoContainer.add(timeLabel);

                JButton pauseButton = new JButton("Pause");
                pauseButton.addActionListener(e -> {
                    // Toggle pause state
                    pause = !pause;

                    if (pause) {
                        Object[] options = {"Continue", "Save Game", "Quit"};
                        int choice = JOptionPane.showOptionDialog(null,
                                "Game is paused. What would you like to do?",
                                "Game Paused",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        switch (choice) {
                            case JOptionPane.YES_OPTION: // Continue
                                pause = false; // Unpause the game
                                gamePanel.requestFocus(); // Ensure focus is returned to the game panel on resume
                                break;
                            case JOptionPane.NO_OPTION: // Save Game
                                runningModeController.saveGameToDatabase();
                                pause = false; // Optionally unpause the game after saving
                                gamePanel.requestFocus(); // Ensure focus is returned to the game panel on resume
                                break;
                            case JOptionPane.CANCEL_OPTION: // Quit
                                pause = false; // Reset pause state
                                runningModeController = null;
                                Navigator.getInstance().showStartSingleplayerPage(); // Navigate away from the game page
                                break;
                            default:
                                // Handle closing the JOptionPane without making a selection
                                pause = true; // Ensure game remains paused if no valid option is chosen
                                break;
                        }
                    }
                });



                infoContainer.add(pauseButton);
                JButton helpScreenButton = new JButton("Help Screen");
                infoContainer.add(helpScreenButton);

                helpScreenButton.addActionListener(e -> {
                    HelpScreenPage helpScreen = new HelpScreenPage();
                    helpScreen.setVisible(true);
                });
                // Adding infoContainer the chance and score instances which are already visual JPanels.
                infoContainer.add(runningModeController.getGameSession().getChance());
                infoContainer.add(runningModeController.getGameSession().getScore());

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

                mainPanel.add(infoContainer);
                //add(infoContainer, BorderLayout.WEST);

                JPanel separatorPanel = new JPanel();
                separatorPanel.setPreferredSize(new Dimension(190, 10));
                mainPanel.add(separatorPanel);

                inventoryContainer = new JPanel();
                inventoryContainer.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridwidth = 2;

                inventoryContainer.setPreferredSize(new Dimension(190, 150));
                initializeInventory(gbc);
                mainPanel.add(inventoryContainer);
                add(mainPanel, BorderLayout.WEST);

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
                        runningModeController.getGameSession().triggerBall();
                    }
                });

                Border border = BorderFactory.createLineBorder(Color.BLACK);
                gamePanel.setPreferredSize(new Dimension(1000, 500)); // Set preferred size of buildingPanel
                gamePanel.addNotify();
                //gamePanel.revalidate();
                gamePanel.setBorder(border);
                add(gamePanel, BorderLayout.EAST);
                //screenWidth = 1000; assigned at top and finalized
                //System.out.println("screenWidth"+ screenWidth);
                screenHeight = 500;

                // Initializing Fireball
                int fireballWidth = runningModeController.getGameSession().getFireball().getPreferredSize().width;
                //int fireballPositionX = (SCREENWIDTH - fireballWidth) / 2;
                int fireballHeight = runningModeController.getGameSession().getFireball().getPreferredSize().height;
                //int fireballPositionY = (screenHeight - fireballHeight - 200);
                //runningModeController.getGameSession().getFireball().getCoordinate().setX(fireballPositionX);
                //runningModeController.getGameSession().getFireball().getCoordinate().setY(fireballPositionY);
                runningModeController.getGameSession().getFireball().setBounds(runningModeController.getGameSession().getFireball().getX(), runningModeController.getGameSession().getFireball().getY(), fireballWidth, fireballHeight);
                gamePanel.add(runningModeController.getGameSession().getFireball());

                // Initializing MagicalStaff
                int magicalStaffWidth = runningModeController.getGameSession().getMagicalStaff().getPreferredSize().width;
                int magicalStaffHeight = runningModeController.getGameSession().getMagicalStaff().getPreferredSize().height;
                int magicalStaffPositionX = runningModeController.getGameSession().getMagicalStaff().getCoordinate().getX();
                int magicalStaffPositionY = runningModeController.getGameSession().getMagicalStaff().getCoordinate().getY();
                runningModeController.getGameSession().getMagicalStaff().setBounds(magicalStaffPositionX, magicalStaffPositionY, magicalStaffWidth, magicalStaffHeight);
                gamePanel.add(runningModeController.getGameSession().getMagicalStaff());

                //Initializing Ymir
                int ymirWidth = runningModeController.getGameSession().getYmir().getPreferredSize().width;
                int ymirHeight = runningModeController.getGameSession().getYmir().getPreferredSize().height;
                int ymirPositionX = runningModeController.getGameSession().getYmir().getCoordinate().getX();
                int ymirPositionY = runningModeController.getGameSession().getYmir().getCoordinate().getY();
                runningModeController.getGameSession().getYmir().setBounds(ymirPositionX, ymirPositionY, ymirWidth, ymirHeight);
                gamePanel.add(runningModeController.getGameSession().getYmir());
                System.out.println("YMIR ADDED TO THE GAME");

                gamePanel.requestFocus();
                gamePanel.setFocusTraversalKeysEnabled(false);

                //to follow if view has focus:
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
                    barrier.setBounds(barrier.getCoordinate().getX(), barrier.getCoordinate().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
                    barrier.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
                    gamePanel.add(barrier);
                }
            }
        });
    }

    private void initializeInventory(GridBagConstraints gbc) {
        ArrayList<JLabel> countLabels = new ArrayList<>(Arrays.asList(hexCount, felixFelicisCount, magicalStaffExpansionCount, overwhelmingFireballCount));
        ArrayList<BufferedImage> images = new ArrayList<>(Arrays.asList(hexImage, felixFelicisImage, magicalStaffExpansionImage, overwhelmingFireballImage));
        ArrayList<JLabel> activationKeys = new ArrayList<>(Arrays.asList(new JLabel("H"),new JLabel("Q"),new JLabel("T"),new JLabel("E")));
        int imageWidth = 40;  // Resimlerin istenen genişliği
        int imageHeight = 40; // Resimlerin istenen yüksekliği

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel inventoryTitleLabel = new JLabel("Inventory");
        inventoryContainer.add(inventoryTitleLabel, gbc);

        Insets commonPadding = new Insets(5, 5, 5, 5);  // Ortak padding

        for (int i = 0; i < images.size(); i++) {
            Image scaledImage = images.get(i).getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = commonPadding;
            inventoryContainer.add(new JLabel(scaledIcon), gbc);

            // Sayı etiketlerini ayarlayın
            gbc.gridx = 1;
            gbc.insets = commonPadding;
            inventoryContainer.add(countLabels.get(i), gbc);

            // Butonları ayarlayın
            gbc.gridx = 2;
            gbc.insets = commonPadding;
            inventoryContainer.add(activationKeys.get(i), gbc);
        }
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.playMusic();
        sound.loop();
    }
    public void stopMusic(){
        sound.stop();
    }
    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }
    public void volume(float i){
        sound.setVolume(sound.getVolume()+i);
    }

    @Override
    public void onInventoryUpdate(SpellType spellType, int newCount) {
        SwingUtilities.invokeLater(() -> {
            switch (spellType) {
                case HEX:
                    hexCount.setText(String.valueOf(newCount));
                    break;
                case FELIX_FELICIS:
                    felixFelicisCount.setText(String.valueOf(newCount));
                    break;
                case STAFF_EXPANSION:
                    magicalStaffExpansionCount.setText(String.valueOf(newCount));
                    break;
                case OVERWHELMING_FIREBALL:
                    overwhelmingFireballCount.setText(String.valueOf(newCount));
                    break;
            }
        });
        inventoryContainer.repaint();
        inventoryContainer.revalidate();
    }
}