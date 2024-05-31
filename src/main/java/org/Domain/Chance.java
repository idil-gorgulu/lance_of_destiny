package org.Domain;

import javax.swing.*;
import java.awt.*;

public class Chance extends JPanel {
    private ImageIcon heartIcon; // Icon to display as a visual representation of chances

    int remainingChances;

    public Chance() {
        this.remainingChances = 3; // Initialize with 5 chances
        setPreferredSize(new Dimension(190, 50));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false); // Make the background transparent

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
        removeAll(); // Clear the panel before updating

        // Add horizontal glue before the icons and label to center everything
        add(Box.createHorizontalGlue());

        int displayedChances = Math.min(remainingChances, 3); // Display up to 3 icons
        for (int i = 0; i < displayedChances; i++) {
            add(new JLabel(heartIcon)); // Add heart icon
        }

        if (remainingChances > 3) {
            JLabel extraChancesLabel = new JLabel(" +" + (remainingChances - 3));
            extraChancesLabel.setForeground(Color.black); // Set the text color
            add(extraChancesLabel); // Add the label displaying the number of extra chances
        }

        // Add horizontal glue after the icons and label to maintain center alignment
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