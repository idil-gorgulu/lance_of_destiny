package org.Domain;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Barrier extends JPanel {
    private Coordinate coordinates;
    private int type; //will indicate the type of the barrier
    private BufferedImage barrierImage;

    public Barrier(Coordinate coordinates, int type) {
        this.coordinates = coordinates;
        this.type = type;
        try {
            this.barrierImage = ImageIO.read(new File(setImageDirectory(type)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(barrierImage.getWidth(), barrierImage.getHeight()));
    }

    private String setImageDirectory(int type) {
        if(type == 0) { //Simple barrier
            return "assets/BlueGem.png";
        }else if(type == 1) { //Reinforced barrier
            return "assets/Firm.png";
        }else if(type == 2) { //Explosive barrier
            return "assets/RedGem.png";
        }else if (type == 3) {
            return "assets/GreenGem.png";
        }
        // In here return Exception
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (barrierImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - barrierImage.getWidth()) / 2;
            int y = (getHeight() - barrierImage.getHeight()) / 2;
            g2d.drawImage(barrierImage,x ,y ,this);
            g2d.dispose();
        }
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
