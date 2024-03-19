package org.waterfall;
import org.oguz_swing_ui.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println("Atakan");
        System.out.println("İdil");
        System.out.println("Sebnem");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Window app = new Window();
                app.setVisible(true); // Make the frame visible
            }
        });
    }
}