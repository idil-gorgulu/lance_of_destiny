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
//                multiplayerGame.getYmir().activateHollowPurple();
                output.println("hp"); //hollow purple
                break;
            case KeyEvent.VK_X:
//                multiplayerGame.getYmir().activateInfiniteVoid();
                output.println("iv"); //hollow purple
                break;
            case KeyEvent.VK_C:
//                multiplayerGame.getYmir().activateDoubleAccel();
                output.println("da"); //hollow purple
                break;
        }
    }

    @Override
    public void run() {
        while(true) {

        }
    }
}
