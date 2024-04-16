package org.Domain;

import org.Domain.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.Views.BuildingModePage;
import org.Views.RunningModePage;

public class MagicalStaff extends JPanel {
    private BufferedImage magicalStaffImage;
    private double angle;
    private double angularVel;
    private int velocity; // to calculate collision
    private Coordinate coordinate;

    public MagicalStaff() {
        this.coordinate = new Coordinate(500,550);
        this.angle = 0;
        try {
            magicalStaffImage = ImageIO.read(new File("assets/200Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(magicalStaffImage.getWidth(), magicalStaffImage.getHeight()));
        }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (magicalStaffImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform at = AffineTransform.getRotateInstance(angle, getWidth() / 2.0, getHeight() / 2.0);
            g2d.drawImage(magicalStaffImage, at, this);
            g2d.dispose();
        }
    }


    public void rotate(double dTheta) {
        angle += dTheta;
        Dimension newSize = calculateRotatedDimensions(angle);
        setPreferredSize(newSize);
        revalidate();
        repaint();
    }
    public void moveMagicalStaff(){
        int newPos =velocity+getCoordinate().getX();
        if ((newPos>0) && (newPos+getPreferredSize().getWidth()<RunningModePage.SCREENWIDTH)) {
            getCoordinate().setX(newPos);
            //;
        }
    }
    public void rotateMagicalStaff(){

        double newAngle = angularVel + angle;

        if ((newAngle>Math.toRadians(-45)) && newAngle<Math.toRadians(45)){
            if (newAngle==0){
                angularVel=0;
            }
            Dimension newSize = calculateRotatedDimensions(angle);
            setPreferredSize(newSize);
            revalidate();
            repaint();
            angle=newAngle;
        }

    }
    //Work In Progress - Melih
    public void stabilize(boolean cw){
        System.out.println(Math.toDegrees(angle)+" "+Math.toDegrees(angularVel));
        if (cw){
            angularVel=Math.toRadians(-1);
            System.out.println(Math.toDegrees(angle)+" "+Math.toDegrees(angularVel));
            if (angularVel+angle<0){
                angle=0;
                angularVel=0;
            }
        }
        else {
            angularVel=Math.toRadians(1);
            System.out.println(Math.toDegrees(angle)+" "+Math.toDegrees(angularVel));
            if (angularVel+angle>0){
                angle=0;
                angularVel=0;
            }
        }
        revalidate();
        repaint();



    }

    private Dimension calculateRotatedDimensions(double angle) {
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int newWidth = (int) Math.floor(magicalStaffImage.getWidth() * cos + magicalStaffImage.getHeight() * sin);
        int newHeight = (int) Math.floor(magicalStaffImage.getHeight() * cos + magicalStaffImage.getWidth() * sin);

        //System.out.println(angle + " " + sin + " " + cos); sin and cos are correct now

        //System.out.println(newWidth + " " + newHeight);
        return new Dimension(newWidth, newHeight);

    }

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
        this.velocity=velocity;
    }

  //  public double getAngularVel() {       return angularVel;    }

    public void setAngularVel(double angularVel) {
        this.angularVel = angularVel;
    }
}