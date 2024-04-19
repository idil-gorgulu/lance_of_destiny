package org.Domain;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Barrier extends JPanel {
    private Coordinate coordinates;
    private BarrierType type; //will indicate the type of the barrier
    private BufferedImage barrierImage;

    private int nHits; //required for reinforced barrier

    public Barrier(Coordinate coordinates, BarrierType type) {
        this.coordinates = coordinates;
        this.type = type;
        this.nHits = initializenHits(type);
        //Option 1:
        try {
            this.barrierImage = ImageIO.read(new File(setImageDirectory(type)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(barrierImage.getWidth(), barrierImage.getHeight()));

        //Option 2:
//        try {
//            this.barrierImage = resizeImage(ImageIO.read(new File(setImageDirectory(type))), 20, 20);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        setPreferredSize(new Dimension(20, 20));
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
                String hitsText = Integer.toString(nHits);
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






    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
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

}
