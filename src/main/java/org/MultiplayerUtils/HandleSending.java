package org.MultiplayerUtils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class HandleSending extends KeyAdapter implements Runnable {
    public PrintWriter output;

    public HandleSending(PrintWriter output) {
        this.output = output;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_Z:
                output.println("hp");
                break;
            case KeyEvent.VK_X:
                output.println("iv");
                break;
            case KeyEvent.VK_C:
                output.println("da");
                break;
        }
    }

    @Override
    public void run() {
        while(true) {

        }
    }
}
