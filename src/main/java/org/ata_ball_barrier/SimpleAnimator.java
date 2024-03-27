package org.ata_ball_barrier;

import javax.swing.JFrame;

public class SimpleAnimator {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JFrame frame = new JFrame( "Bouncing Ball" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        BallPanel bp = new BallPanel();
        frame.add( bp );
        StaffPanel sp = new StaffPanel();
        frame.add( sp );
        frame.setSize( 300, 300 ); // set frame size
        frame.setVisible( true ); // display frame
    }

}