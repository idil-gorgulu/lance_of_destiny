package org.Domain;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class Sound {
    Clip clip;
    File[] files=new File[20];
    //File file1;

    public Sound(){
        //soundURL[0]=getClass().getResource("assets/sound/mainMenu.wav");
        files[0]= new File("assets/sound/gameTheme.wav");
        files[1]=new File("assets/sound/collision.wav");
        files[2]=new File("assets/sound/explosiveBarrier.wav");
        files[3]=new File("assets/sound/expandBarrier.wav");
        files[4]=new File("assets/sound/fireBullet.wav");


    }

    public void setFile(int i){
        try {
            AudioInputStream ais= AudioSystem.getAudioInputStream(files[i]);
            clip=AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception e){
            System.err.println("!!!!!!!!!!!");
        }
    }
    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
