package org.Views;

import org.Controllers.BuildingModeController;
import org.Domain.*;
import org.Utils.ComponentStyling;
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
    private JLabel lowerBoundInfo;
    private int selectedButtonIndex = -1;
    private JPanel infoContainer;
    private JButton playButton;
    public int pageNum = 1;
    private JButton saveButton;
    private JLabel templateGameName;
    private JTextField templateGameNameInput;
    private boolean mpgame = false;

    public BuildingModePage() {
        super();
        this.buildingModeController = new BuildingModeController();
        initUI();
    }

    public BuildingModePage(boolean mpgame) {
        super();
        this.buildingModeController = new BuildingModeController();
        this.mpgame = true;
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout());
        infoContainer = new JPanel(new FlowLayout());
        JButton helpScreenButton = new JButton("Help Screen");
        infoContainer.add(helpScreenButton);

        helpScreenButton.addActionListener(e -> {
            HelpScreenPage helpScreen = new HelpScreenPage();
            helpScreen.setVisible(true);
        });

        JButton backButton = new JButton("Back");
        JButton createButton = new JButton("Create");
        createButton.setPreferredSize(new Dimension(150, 50));
        infoContainer.add(backButton);

        JPanel barrierPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0); // padding
        gbc.anchor = GridBagConstraints.WEST;

        // Add simple barrier
        JLabel s = new JLabel("Add simple barrier:");
        JTextField inputField1 = new JTextField(4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        barrierPanel.add(s, gbc);
        gbc.gridx = 1;
        barrierPanel.add(inputField1, gbc);

        // Add firm barrier
        JLabel f = new JLabel("Add firm barrier:");
        JTextField inputField2 = new JTextField(4);
        gbc.gridx = 0;
        gbc.gridy = 1;
        barrierPanel.add(f, gbc);
        gbc.gridx = 1;
        barrierPanel.add(inputField2, gbc);

        // Add explosive barrier
        JLabel x = new JLabel("Add explosive barrier:");
        JTextField inputField3 = new JTextField(4);
        gbc.gridx = 0;
        gbc.gridy = 2;
        barrierPanel.add(x, gbc);
        gbc.gridx = 1;
        barrierPanel.add(inputField3, gbc);

        // Add gift barrier
        JLabel g = new JLabel("Add gift barrier:");
        JTextField inputField4 = new JTextField(4);
        gbc.gridx = 0;
        gbc.gridy = 3;
        barrierPanel.add(g, gbc);
        gbc.gridx = 1;
        barrierPanel.add(inputField4, gbc);
        infoContainer.add(barrierPanel);

        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());

        createButton.addActionListener(new ActionListener() {
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
                //populating game:
                boolean b = buildingModeController.initialPopulation(num1, num2, num3, num4);
                if(!b){
                    JOptionPane.showMessageDialog(null, "Not enough places!");
                }
                else{
                    regenerate();
                    inputField1.setText("0");
                    inputField2.setText("0");
                    inputField3.setText("0");
                    inputField4.setText("0");
                }
            }
        });
        infoContainer.add(createButton);

        JLabel line = new JLabel("-----------------------------------");
        infoContainer.add(line);
        //displaying current number of barriers:
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

        leftSide.add(infoContainer, BorderLayout.CENTER);

        playButton = new JButton("Play");
        playButton.addActionListener(e -> Navigator.getInstance().showRunningModePage());

        templateGameName = new JLabel("Name your game template:");
        g.setHorizontalAlignment(SwingConstants.CENTER);
        templateGameNameInput = new JTextField(20);

        lowerBoundInfo = new JLabel("<html>Meet the criteria to be <br>able to save and play!</html>");
        lowerBoundInfo.setHorizontalAlignment(SwingConstants.LEFT);
        infoContainer.add(lowerBoundInfo);

        if (!this.mpgame) {
            saveButton = new JButton("Save");
            saveButton.addActionListener(e -> buildingModeController.saveGameToDatabase(templateGameNameInput.getText()));
            add(leftSide, BorderLayout.WEST);
        } else {
            saveButton = new JButton("Multiplayer");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buildingModeController.saveGameToDatabase(templateGameNameInput.getText());
                    User.getUserInstance().setMultiplayerGameName(templateGameNameInput.getText());
                    Navigator.getInstance().showWaitMultiplayerGameAcceptPage();
                }
            });
            add(leftSide, BorderLayout.WEST);
        }
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttons = new JButton[4];
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

                int finalI = i;
                //changing currently selected button index:
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (selectedButtonIndex == finalI) {
                            selectedButtonIndex = -1;
                        } else {
                            selectedButtonIndex = finalI;
                        }
                        updateButtonState();
                        //System.out.println("selected button: "+selectedButtonIndex);
                    }
                });
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                buttonPanel.add(button);
                buttons[i] = button;
            }
            Dimension buttonSize = new Dimension(maxWidth, maxHeight);
            for (JButton button : buttons) {
                button.setPreferredSize(buttonSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(buttonPanel, BorderLayout.NORTH);

        buildingContainer = new JPanel();
        buildingContainer.setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File("assets/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildingPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                // Draw a line at y=400
                g.setColor(Color.BLACK);
                g.drawLine(0, 400, getWidth(), 400);
            }
        };
        buildingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedButtonIndex != -1) {
                    System.out.println("Mouse click coordinates:"+ e.getX()+" "+ e.getY()); //to debug
                    addBarrierImage(new Coordinate(e.getX(), e.getY()));
                }
            }
        });

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        buildingPanel.setPreferredSize(new Dimension(1080, 500));
        buildingPanel.setBorder(border);

        buildingContainer.add(buildingPanel, BorderLayout.CENTER);
        add(buildingContainer, BorderLayout.CENTER);
        JLabel statusLabel = new JLabel("Building Mode", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        buildingContainer.setPreferredSize(new Dimension(1000, 500));
    }

    private void addBarrierImage(Coordinate coordinates) {
        if (selectedButtonIndex == -1  || coordinates.getY()>=399) {
            return;
        }
        Coordinate barrierCoordinates=BuildingModeController.addBarrier(coordinates,BarrierType.values()[selectedButtonIndex] );

        if (barrierCoordinates==null){ //barrier already exists in those coordinates
            regenerate();
            buildingPanel.repaint();
            buildingPanel.revalidate();
            return;
        }
        regenerate();
        buildingPanel.repaint();
        buildingPanel.revalidate();
    }

    private void updateButtonState() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
            buttons[i].setBackground(null);
        }
        if (selectedButtonIndex != -1) {
            buttons[selectedButtonIndex].setBorder(BorderFactory.createLoweredBevelBorder());
            buttons[selectedButtonIndex].setBackground(Color.GREEN);
        }
    }
    public void regenerate(){
        buildingPanel.removeAll();
        simpleAmount.setText("Simple barriers: " + buildingModeController.getGameSession().getNumSimpleBarrier() + "/75");
        firmAmount.setText("Firm barriers: " + buildingModeController.getGameSession().getNumFirmBarrier() + "/10");
        explosiveAmount.setText("Explosive barriers: " + buildingModeController.getGameSession().getNumExplosiveBarrier() + "/5");
        rewardingAmount.setText("Rewarding barriers: " + buildingModeController.getGameSession().getNumrewardingBarrier() + "/10");

        infoContainer.add(templateGameName);
        infoContainer.add(templateGameNameInput);
        infoContainer.add(saveButton, BorderLayout.SOUTH);
        leftSide.add(playButton, BorderLayout.SOUTH);

        if (buildingModeController.getReady()){
            //donot delete these commented out lines
//            infoContainer.add(saveButton, BorderLayout.SOUTH);
//            leftSide.add(playButton, BorderLayout.SOUTH);
            infoContainer.remove(lowerBoundInfo);
        }
        else{
            //donot delete these commented out lines
//            infoContainer.remove(saveButton);
//            leftSide.remove(playButton);
            lowerBoundInfo.setHorizontalAlignment(SwingConstants.LEFT);
            infoContainer.add(lowerBoundInfo); }

        infoContainer.revalidate();
        infoContainer.repaint();

        leftSide.revalidate();
        leftSide.repaint();

        ArrayList<Barrier> barriers;
        barriers = buildingModeController.getGameSession().getBarriers();
        for (Barrier barrier : barriers) {
            barrier.setBounds(barrier.getCoordinate().getX(), barrier.getCoordinate().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
            buildingPanel.add(barrier);
            barrier.setBackground(Color.blue);
            barrier.setOpaque(false);
            buildingPanel.repaint();
            buildingPanel.revalidate();
        }
    }
}
