package org.Domain;

import javax.swing.*;
import java.awt.*;

public class Score extends JPanel{

    private Coordinate coordinate;

    int totalScore;

    public Score(){
        this.totalScore=0;
        this.coordinate = new Coordinate(100,50);
        setPreferredSize(new Dimension(100, 50));
        setLayout(new GridLayout(1,1,0,0));
        add(new JLabel("Score: "+String.valueOf(totalScore)));
        setVisible(true);
    }

    public int getScore() {
        return totalScore;
    }

    public void addScore(int dscore){
        totalScore+=dscore;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

}


