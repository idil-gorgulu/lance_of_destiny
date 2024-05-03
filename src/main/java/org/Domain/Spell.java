package org.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Spell extends JComponent {
    public BufferedImage spellImage;
    private Coordinate coordinate;
    private int yVelocity = 3;
    SpellType spellType;

    public Spell(Coordinate coordinate) {
        this.coordinate = new Coordinate(coordinate.getX(), coordinate.getY());
        this.spellType = initializeSpellType();
        try {
            this.spellImage = ImageIO.read(new File(setImageDirectory()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(spellImage.getWidth(), spellImage.getHeight()));
        setSize(getPreferredSize());
        setLocation(coordinate.getX() - spellImage.getWidth() / 2, coordinate.getY() - spellImage.getHeight() / 2);
    }

    private String setImageDirectory() {
        return "assets/treasure.png";
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spellImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - spellImage.getWidth()) / 2;
            int y = (getHeight() - spellImage.getHeight()) / 2;
            g2d.drawImage(spellImage,x ,y , (ImageObserver) this);
            g2d.dispose();
        }
    }

    public void moveDown() {
        this.coordinate.setY(this.coordinate.getY() + yVelocity);
        this.setBounds(this.coordinate.getX() - spellImage.getWidth() / 2,
                this.coordinate.getY() - spellImage.getHeight() / 2,
                spellImage.getWidth(),
                spellImage.getHeight());
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setSpellImage(BufferedImage spellImage) {
        this.spellImage = spellImage;
    }

    public BufferedImage getSpellImage() {
        return spellImage;
    }
    public SpellType initializeSpellType(){
        Random rand = new Random();
        int randomSpellTypeIndex = rand.nextInt(SpellType.values().length);
        return SpellType.values()[randomSpellTypeIndex];
    }
}
