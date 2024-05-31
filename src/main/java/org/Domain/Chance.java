package org.Domain;

import javax.swing.*;
import java.awt.*;

public class Chance extends JPanel {
    private ImageIcon heartIcon;

    int remainingChances;

    public Chance() {
        this.remainingChances = 3;
        setPreferredSize(new Dimension(190, 50));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        heartIcon = new ImageIcon("./assets/200Heart.png");

        updateChanceView();
        setVisible(true);
    }

    public void incrementChance() {
        remainingChances += 1;
        updateChanceView();
    }

    public void decrementChance() {
        if (remainingChances > 0) {
            remainingChances -= 1;
            updateChanceView();
        }
    }

    private void updateChanceView() {
        removeAll();

        add(Box.createHorizontalGlue());

        int displayedChances = Math.min(remainingChances, 3);
        for (int i = 0; i < displayedChances; i++) {
            add(new JLabel(heartIcon));
        }

        if (remainingChances > 3) {
            JLabel extraChancesLabel = new JLabel(" +" + (remainingChances - 3));
            extraChancesLabel.setForeground(Color.black);
            add(extraChancesLabel);
        }

        add(Box.createHorizontalGlue());

        repaint();
        revalidate();
    }

    public int getRemainingChance() {
        return remainingChances;
    }

    public void setRemainingChance(int chance) {
        remainingChances = chance;
        updateChanceView();
    }
}