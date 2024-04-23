package org.Views;

import org.Controllers.BuildingModeController;
import org.Controllers.LoginPageController;
import org.Controllers.RunningModeController;
import org.Domain.*;
import org.Utils.Database;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.Controllers.BuildingModeController.getReady;

public class BuildingModePage extends Page {
    private BuildingModeController buildingModeController;
    private BufferedImage backgroundImage;
    private JPanel buildingPanel;
    private JPanel buildingContainer;
    private JPanel leftSide;
    private JLabel leftBarriers;
    private JButton[] buttons;
    private JLabel simpleAmount;
    private JLabel firmAmount;
    private JLabel explosiveAmount;
    private JLabel rewardingAmount;
    private int selectedButtonIndex = -1;
    private JPanel infoContainer;
    private JButton playButton;
    public int pageNum = 1;
    private JButton saveButton;
    private JLabel templateName;
    private JTextField templateNameInput;

    public BuildingModePage() {
        super();
        this.buildingModeController = new BuildingModeController(this);

        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());

        //JMenuBar menuBar = new JMenuBar();
        //JMenu menu = new JMenu("Menu");
        //menuBar.add(menu);

        infoContainer = new JPanel(new FlowLayout());

        JLabel s = new JLabel("Add simple barrier:");
        s.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(s);
        JTextField inputField1 = new JTextField(4);
        infoContainer.add(inputField1);

        JLabel f = new JLabel("Add firm barrier:");
        f.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(f);
        JTextField inputField2 = new JTextField(4);
        infoContainer.add(inputField2);

        JLabel x = new JLabel("Add explosive barrier:");
        x.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(x);
        JTextField inputField3 = new JTextField(4);
        infoContainer.add(inputField3);

        JLabel g = new JLabel("Add gift barrier:");
        g.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(g);
        JTextField inputField4 = new JTextField(4);
        infoContainer.add(inputField4);

        // Add button
        JButton submitButton = new JButton("Create");
        submitButton.setPreferredSize(new Dimension(150, 50));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText1 = inputField1.getText();
                int num1 = Integer.parseInt(inputText1);
                String inputText2 = inputField2.getText();
                int num2 = Integer.parseInt(inputText2);
                String inputText3 = inputField3.getText();
                int num3 = Integer.parseInt(inputText3);
                String inputText4 = inputField4.getText();
                int num4 = Integer.parseInt(inputText4);
                boolean b = buildingModeController.initialPopulation(num1, num2, num3, num4);
                if(b==false){
                    JOptionPane.showMessageDialog(null, "Not enough places!");
                }
                else{
                    regenerate();
                    // Clear input fields
                    inputField1.setText("");
                    inputField2.setText("");
                    inputField3.setText("");
                    inputField4.setText("");
                }
            }
        });
        infoContainer.add(submitButton);
        JLabel line = new JLabel("-----------------------------------");
        infoContainer.add(line);
        simpleAmount = new JLabel("Simple barriers: "+ buildingModeController.getGameSession().getNumSimpleBarrier()+ "/75");
        simpleAmount.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(simpleAmount);
        firmAmount = new JLabel("Firm barriers: "+ buildingModeController.getGameSession().getNumFirmBarrier()+ "/10");
        firmAmount.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(firmAmount);
        explosiveAmount = new JLabel("Explosive barriers: "+ buildingModeController.getGameSession().getNumExplosiveBarrier()+ "/5");
        explosiveAmount.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(explosiveAmount);
        rewardingAmount = new JLabel("Rewarding barriers: "+ buildingModeController.getGameSession().getNumrewardingBarrier()+ "/10");
        rewardingAmount.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(rewardingAmount);
        JLabel line2 = new JLabel("-----------------------------------");
        infoContainer.add(line2);
        leftSide = new JPanel(new BorderLayout());
        leftSide.setPreferredSize(new Dimension(200, 500));

        //menuContainer.add(menuBar, BorderLayout.NORTH);

        leftSide.add(infoContainer, BorderLayout.CENTER);


        //JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //backButtonPanel.setOpaque(false); // Make the panel transparent
        playButton = new JButton("Play");
        // Condition to the Play button
        playButton.addActionListener(e -> Navigator.getInstance().showRunningModePage());
        //leftSide.add(playButton, BorderLayout.SOUTH);

        templateName = new JLabel("Name your template:");
        g.setHorizontalAlignment(SwingConstants.LEFT);
        templateNameInput = new JTextField(20);


        saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Barrier> barriers = buildingModeController.getGameSession().getBarriers();
                Document gameSession = new Document();
                gameSession.put("email", User.getUserInstance().getEmail());
                // TODO: Enter the gamename in this page somewhere
                gameSession.put("gameName", templateName);
                for (Barrier barrier : barriers) {
                    gameSession.put(barrier.getCoordinate().toString(), barrier.getType().toString() + barrier.getnHits());
                }

                gameSession.put("played", "False");
                Database.getInstance().getGameCollection().insertOne(gameSession);
            }
        });
        // TODO: This overlaps with Menu, fix it
        // To see it comment out the below line
        //leftSide.add(saveButton, BorderLayout.SOUTH);

        add(leftSide, BorderLayout.WEST);

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
                    //TODO: Check borders of where we can add barriers, we should not below some level.
                    addBarrierImage(new Coordinate(e.getX(), e.getY()));
                }
            }
        });

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        buildingPanel.setPreferredSize(new Dimension(1080, 500)); // Set preferred size of buildingPanel
        int screenWidth = buildingPanel.getWidth();
        //System.out.println("buildingPanel screenWidth"+ screenWidth);
        int screenWidth2 = buildingContainer.getWidth();
        //System.out.println("buildingContainer screenWidth"+ screenWidth2);
        buildingPanel.setBorder(border);
        //regenerate();
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
        System.out.println(selectedButtonIndex);
        BarrierType type = BarrierType.values()[selectedButtonIndex];
        //BuildingModeController.addBarrier(coordinates, type);
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
        MagicalStaff magicalStaff = BuildingModeController.getGameSession().getMagicalStaff();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;

        int magicalStaffPositionX = 480;
        int magicalStaffPositionY = 500;

        magicalStaff.setBounds(magicalStaffPositionX, magicalStaffPositionY, magicalStaffWidth, magicalStaffHeight);
        magicalStaff.setBackground(Color.green);
        buildingPanel.add(magicalStaff);

        simpleAmount.setText("Simple barriers: " + buildingModeController.getGameSession().getNumSimpleBarrier() + "/75");
        firmAmount.setText("Firm barriers: " + buildingModeController.getGameSession().getNumFirmBarrier() + "/10");
        explosiveAmount.setText("Explosive barriers: " + buildingModeController.getGameSession().getNumExplosiveBarrier() + "/5");
        rewardingAmount.setText("Rewarding barriers: " + buildingModeController.getGameSession().getNumrewardingBarrier() + "/10");

        infoContainer.add(templateName);
        infoContainer.add(templateNameInput);
        infoContainer.add(saveButton, BorderLayout.SOUTH);
        leftSide.add(playButton, BorderLayout.SOUTH);

        //TODO: MAKE BELOW CODE ACTIVE BEFORE DEMO.
        /*
        if (getReady()){
            infoContainer.add(templateName);
            infoContainer.add(templateNameInput);
            infoContainer.add(saveButton, BorderLayout.SOUTH);
            leftSide.add(playButton, BorderLayout.SOUTH);
        }
        else{
            infoContainer.remove(saveButton);
            leftSide.remove(playButton);

        }
         */
        infoContainer.revalidate();
        infoContainer.repaint();

        leftSide.revalidate();
        leftSide.repaint();


        ArrayList<Barrier> barriers;
        barriers = buildingModeController.getGameSession().getBarriers();
        for (Barrier barrier : barriers) {
            System.out.println("Building mode barrier putting: "+ barrier.getCoordinate().getX() +","+ barrier.getCoordinate().getY());
            barrier.setBounds(barrier.getCoordinate().getX(), barrier.getCoordinate().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
            buildingPanel.add(barrier);
            barrier.setBackground(Color.blue);
            barrier.setOpaque(true);
            buildingPanel.repaint();
            buildingPanel.revalidate();
        }
    }

}
