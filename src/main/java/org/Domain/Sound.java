package org.Domain;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    Clip clip2;
    File[] files=new File[20];
    //File file1;

    public Sound(){
        files[0]= new File("assets/sound/gameTheme.wav");
        files[1]=new File("assets/sound/collision.wav");
        files[2]=new File("assets/sound/explosiveBarrier.wav");
        files[3]=new File("assets/sound/expandBarrier.wav");
        files[4]=new File("assets/sound/fireBullet.wav");
        files[5]=new File("assets/sound/addChance.wav");


    }

    public void setFile(int i){

        try {
            AudioInputStream ais= AudioSystem.getAudioInputStream(files[i]);
            if (i==0){
                clip=AudioSystem.getClip();
                clip.open(ais);
            }
            else {
                clip2=AudioSystem.getClip();
                clip2.open(ais);
            }

        }catch (Exception e){
            System.err.println("!!!!!!!!!!!");
        }
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f) volume=0;
        else if ( volume > 1f) volume=1;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void playMusic(){
        clip.start();
    }
    public void play(){
        clip2.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.close();
    }
}
