package org.Domain;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MagicalStaff extends JPanel {
    private BufferedImage magicalStaffImage;
    private Rectangle2D.Double magicalStaffRectangle;
    private double angle = 0; // degrees
    private double angVelocity=0;
    private int velocity; // to calculate collision
    private Coordinate coordinate;
    private int staffWidth=100;
    private int staffHeight=20;
    private boolean isReleased;
    private long expansionTime=(long)Double.POSITIVE_INFINITY; // last time when staff is expanded

    public MagicalStaff() {
        this.coordinate = new Coordinate(0,450);
        this.magicalStaffRectangle = new Rectangle2D.Double(450, 90, staffWidth, staffHeight);

        try {
            magicalStaffImage = ImageIO.read(new File("assets/200Player.png"));
            // Resize the image to 100x20 pixels
            magicalStaffImage = resizeImage(magicalStaffImage, staffWidth,staffHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(1000, 400));
        this.setOpaque(false);
        this.setVisible(true);
    }

    // Method to resize the image
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform old = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), this.magicalStaffRectangle.getCenterX(), this.magicalStaffRectangle.getCenterY());
        g2d.transform(transform);
        if (magicalStaffImage != null) {
            g2d.drawImage(magicalStaffImage, (int) magicalStaffRectangle.x, (int) magicalStaffRectangle.y, null);
        }
        g2d.setColor(Color.RED);
        g2d.draw(magicalStaffRectangle);
        g2d.setTransform(old);
    }

    public void updateMagicalStaffView(){
        double newX = this.magicalStaffRectangle.x + this.velocity;
        if ((newX>0) && (newX+magicalStaffRectangle.getWidth()<1000)) {// check borders
            this.magicalStaffRectangle.setRect(newX, this.magicalStaffRectangle.y, this.magicalStaffRectangle.width, this.magicalStaffRectangle.height);
        }
        double newAngle = angVelocity + angle;
        if ((newAngle>=-45) && newAngle<=45)//check angle
            this.angle=newAngle;
        if ((isReleased) && (newAngle<3) && (newAngle>-3)){
            this.angle=0;
            this.angVelocity=0;
        }
        long now=System.currentTimeMillis();
        if (now-expansionTime > 30*1000){ // shrink back the staff after 30 sec
            expansionTime=now;
            setStaffWidth(100);
        }
        repaint();
    }

/*
    private Dimension calculateRotatedDimensions(double angle) {
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int newWidth = (int) Math.floor(magicalStaffImage.getWidth() * cos + magicalStaffImage.getHeight() * sin);
        int newHeight = (int) Math.floor(magicalStaffImage.getHeight() * cos + magicalStaffImage.getWidth() * sin);

        //System.out.println(angle + " " + sin + " " + cos); sin and cos are correct now

        //System.out.println(newWidth + " " + newHeight);
        return new Dimension(newWidth, newHeight);
    }
    */


    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {this.coordinate = coordinate;}

    public double getAngle() {
        return angle;
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity){
        this.velocity = velocity;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Coordinate getTopLeftCornerOfMagicalStaff(){
        return new Coordinate(
                        (int) (getCoordinate().getX() + this.magicalStaffRectangle.x),
                        (int) (getCoordinate().getY() + this.magicalStaffRectangle.y));
    }


    public double getAngVelocity() {
        return angVelocity;
    }

    public void setAngVelocity(double angVelocity) {
        this.angVelocity = angVelocity;
    }

    public int getStaffHeight() {
        return staffHeight;
    }

    public int getStaffWidth() {
        return staffWidth;
    }
    public void setReleased(boolean b){
        isReleased=b;
    }
    public void addCannon(){

    }
    public void setExpansionTime(long time){
        this.expansionTime=time;
    }

    public void setStaffWidth(int staffWidth) {
        this.staffWidth = staffWidth;
        this.magicalStaffRectangle.setRect(
                this.magicalStaffRectangle.x, this.magicalStaffRectangle.y,
                this.staffWidth, this.staffHeight);
        if (staffWidth==200){
            if (magicalStaffRectangle.x+200>1000){ //when expanded staff is stuck beyond border
                this.magicalStaffRectangle.setRect
                        (795, this.magicalStaffRectangle.y,
                                this.magicalStaffRectangle.width, this.magicalStaffRectangle.height);
            }
            try {
                magicalStaffImage = ImageIO.read(new File("assets/200Player_extended.png"));
                // Resize the image to 200x20 pixels
                magicalStaffImage = resizeImage(magicalStaffImage, staffWidth,staffHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                magicalStaffImage = ImageIO.read(new File("assets/200Player.png"));
                // Resize the image to 200x20 pixels
                magicalStaffImage = resizeImage(magicalStaffImage, staffWidth,staffHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setPreferredSize(new Dimension(1000, 400));
        this.setOpaque(false);
        this.setVisible(true);
        repaint();
    }

}