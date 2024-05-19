package org.Domain;


import org.Views.RunningModePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class HollowPurple extends JPanel {
    private Coordinate coordinate;
    private BarrierType type; //will indicate the type of the barrier
    private BufferedImage barrierImage;
    private int nHits; //required for reinforced barrier

    private static final int RADIUS_FACTOR = 3; // 1.5 * L
    public static final double ANGULAR_SPEED = Math.PI / 10;
    private long lastCollisionTime=-5; //-inf, initially barriers are able to collide, is updated after collision

    public HollowPurple(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.type = BarrierType.SIMPLE;
        this.nHits = initializenHits(type);
        //Option 1:
        try {
            this.barrierImage = ImageIO.read(new File(setImageDirectory()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Barrier size:" + barrierImage.getWidth() + barrierImage.getHeight());
        //setPreferredSize(new Dimension(barrierImage.getWidth(), barrierImage.getHeight()));

        //Option 2:
        try {
            this.barrierImage = resizeImage(ImageIO.read(new File(setImageDirectory())), 50, 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(50, 15));
        System.out.println("Barrier size:" + barrierImage.getWidth() + barrierImage.getHeight());

    }

    private String setImageDirectory() {
            return "assets/PurpleGem.png";
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (barrierImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - barrierImage.getWidth()) / 2;
            int y = (getHeight() - barrierImage.getHeight()) / 2;
            g2d.drawImage(barrierImage, x, y, this);
            g2d.dispose();
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinates(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public BarrierType getType() {
        return type;
    }

    public void setType(BarrierType type) {
        this.type = type;
    }

    public void setnHits(int nHits) {
        this.nHits = nHits;
    }

    public int initializenHits(BarrierType type){
            return 1;
    }

    public int getnHits() {
        return nHits;
    }

    public void destroy() {
        setVisible(false);
        revalidate();
        repaint();
    }

    public double currentAngle = 0; // Maintain the current angle as a member variable
}


