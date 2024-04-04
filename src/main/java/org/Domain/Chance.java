package org.Domain;

import javax.swing.*;
import java.awt.*;


public class Chance extends JPanel {
    private Coordinate coordinate;

    int remainingChances;

    public Chance(int remainingChances){
        this.remainingChances=remainingChances;
        this.coordinate = new Coordinate(850,50);
        setPreferredSize(new Dimension(100, 50));
        setLayout(new BorderLayout());
        add(new JLabel("chances: "+String.valueOf(remainingChances)),BorderLayout.WEST);
        setVisible(true);
    }

    public int getRemainingChances() {
        return remainingChances;
    }

    public void incrementChance(){
        remainingChances+=1;
    }
    public void decrementChance(){
        remainingChances-=1;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

}
