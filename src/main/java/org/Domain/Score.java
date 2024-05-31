package org.Domain;

import javax.swing.*;
import java.awt.*;

public class Score extends JPanel {
    private int totalScore;
    private JLabel scoreLabel;  // JLabel to display the score
    public Score() {
        this.totalScore = 0;
        setPreferredSize(new Dimension(190, 50));
        setLayout(new GridLayout(1, 1, 0, 0));
        scoreLabel = new JLabel("Score: " + totalScore);
        add(scoreLabel);
        setVisible(true);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void incrementScore(int barrierCount, float time) {
        totalScore = (int) (totalScore + (barrierCount * (300 / time)));
        updateScoreView();
    }

    // Do not call this method directly, instead, call incrementScore.
    private void updateScoreView() {
        scoreLabel.setText("Score: " + totalScore);
    }

}