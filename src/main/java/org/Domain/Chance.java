package org.Domain;

import javax.swing.*;
import java.awt.*;


public class Chance extends JPanel {
    private Coordinate coordinate;

    int remainingChances;

    public Chance(){
        this.remainingChances = 3;
        setPreferredSize(new Dimension(190, 50));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setForeground(Color.WHITE);
        updateChanceView();
        setVisible(true);

    }

    public int getRemainingChances() {
        return remainingChances;
    }

    public void incrementChance(){
        remainingChances+=1;
        updateChanceView();
    }
    public void decrementChance() {
        if (remainingChances > 0) {
            remainingChances = remainingChances-1;
            updateChanceView();
        }
    }

    // Do not call this method directly, instead, call incrementChance or decrementChance.
    private void updateChanceView() {
        removeAll();
        add(Box.createHorizontalGlue());

        ImageIcon heartIcon = new ImageIcon("./assets/200Heart.png");
        for (int i = 0; i < remainingChances; i++) {
            JLabel heartLabel = new JLabel(heartIcon);
            add(heartLabel);
        }

        add(Box.createHorizontalGlue());
        repaint();
        revalidate();
        System.out.println("remaining chances: " + String.valueOf(remainingChances));
    }
    public int getRemainingChance() {
        return remainingChances;
    }
    public void setRemainingChance(int chance) {
        remainingChances = chance;
    }
}
