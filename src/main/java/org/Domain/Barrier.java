package org.Domain;


import org.Views.RunningModePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Barrier extends JPanel {
    private Coordinate coordinate;
    private BarrierType type; //will indicate the type of the barrier
    private BufferedImage barrierImage;
    private int nHits; //required for reinforced barrier
    private int velocity; //+-3 for moving, 0 for stiff barriers
    private boolean isMoving;
    private double radius; // Set an appropriate radius for visible movement
    private double centerX; // X center of the circle
    private double centerY;

    private static final int RADIUS_FACTOR = 3; // 1.5 * L
    private static final double ANGULAR_SPEED = Math.PI / 10;

    public Barrier(Coordinate coordinate, BarrierType type) {
        this.coordinate = coordinate;
        this.type = type;
        this.nHits = initializenHits(type);
        this.isMoving=false;
        this.radius = 150; // Example radius, adjust based on your screen size and desired movement scale
        this.centerX = coordinate.getX() + radius; // Center based on initial position
        this.centerY = coordinate.getY();
        //Option 1:
        try {
            this.barrierImage = ImageIO.read(new File(setImageDirectory(type)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Barrier size:" + barrierImage.getWidth() + barrierImage.getHeight());
        //setPreferredSize(new Dimension(barrierImage.getWidth(), barrierImage.getHeight()));

        //Option 2:
        try {
            this.barrierImage = resizeImage(ImageIO.read(new File(setImageDirectory(type))), 50, 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(50, 15));
        System.out.println("Barrier size:" + barrierImage.getWidth() + barrierImage.getHeight());

    }

    private String setImageDirectory(BarrierType type) {
        if(type == BarrierType.SIMPLE) { //Simple barrier
            return "assets/BlueGem.png";
        }else if(type == BarrierType.REINFORCED) { //Reinforced barrier
            return "assets/Firm.png";
        }else if(type == BarrierType.EXPLOSIVE) { //Explosive barrier
            return "assets/RedGem.png";
        }else if (type == BarrierType.REWARDING) {
            return "assets/GreenGem.png";
        }

        // In here return Exception
        return null;
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

            if (type == BarrierType.REINFORCED) {
                String hitsText = Integer.toString(this.getnHits());
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(hitsText);
                int textHeight = fm.getHeight();

                //Adding background to improve the readability of the remaining hits
                int rectX = x + (barrierImage.getWidth() - textWidth) / 2 - 5;
                int rectY = y + (barrierImage.getHeight() - textHeight) / 2 - 5;
                int rectWidth = textWidth + 10;
                int rectHeight = textHeight + 10;

                g2d.setColor(new Color(0, 90, 30));
                g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

                g2d.setColor(Color.WHITE);
                g2d.drawString(hitsText, rectX + 5, rectY + fm.getAscent() + 5);
            }

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
        Random rand = new Random();
        if (type== BarrierType.REINFORCED){
            return rand.nextInt(9) + 2;
        }
        else{
            return 1;
        }
    }

    public int getnHits() {
        return nHits;
    }

    public void destroy() {
        setVisible(false);
        revalidate();
        repaint();
    }

    private double currentAngle = 0; // Maintain the current angle as a member variable

    public void moveCircular() {
        currentAngle += getVelocity() * ANGULAR_SPEED * (Math.PI / 180);

        //new Position Calculation
        int newX = (int) (centerX + radius * Math.cos(currentAngle));
        int newY = (int) (centerY + radius * Math.sin(currentAngle));

        //Boundary check
        if (newX >= 0 && newX + getWidth() <= RunningModePage.SCREENWIDTH &&
                newY >= 0 && newY + getHeight() <= 500) {
            coordinate.setX(newX);
            coordinate.setY(newY);
        } else {
            currentAngle -= getVelocity() * ANGULAR_SPEED * (Math.PI / 180); //TO BE CHANGED
        }
    }

    public void moveBarrier(){
        if (type == BarrierType.EXPLOSIVE && isMoving) {
            moveCircular();
        } else {
            int newPos = velocity + getCoordinate().getX();
            if ((newPos > 0) && (newPos + getPreferredSize().getWidth() < RunningModePage.SCREENWIDTH)) {
                getCoordinate().setX(newPos);
            }
            else
                setVelocity(-1*velocity);
        }
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
