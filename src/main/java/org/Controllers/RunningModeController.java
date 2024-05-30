package org.Controllers;

import org.Domain.*;
import org.Utils.Database;
import org.Views.RunningModePage;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static org.Views.RunningModePage.COLLISION_COOLDOWN;


public class RunningModeController {
    protected RunningModePage runningModePage;
    private DataBaseController dataBaseController;
    private Game game;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.game= Game.getInstance();
        this.dataBaseController=DataBaseController.getInstance();

    }
    public Game getGameSession() {
        return Game.getInstance();
    }

    // These are the functions for updating the views of the game components.
    public void updateFireballView(){
        game.getFireball().updateFireballView();
    }
    public void updateMagicalStaffView(){
        MagicalStaff magicalStaff=game.getMagicalStaff();
        magicalStaff.updateMagicalStaffView();

        // TODO move to cannon
        if (magicalStaff.isShooting() && System.currentTimeMillis()-magicalStaff.getShotTime()>0.6*1000){
            fireBullet();
            magicalStaff.setShotTime(System.currentTimeMillis());
        }
    }

    // These are the functions for updating the position and angle orientation of the magical staff.
    public void slideMagicalStaff(int x){
        game.getMagicalStaff().setVelocity(x);
    }

    public void rotateMagicalStaff(double dTheta){
        game.getMagicalStaff().setAngVelocity(dTheta);
    }

    public void moveBarriers(){
        game.moveBarriers();
    }

    public void checkCollision(){
        game.checkBarrierFireballCollision(runningModePage.timeInSeconds);
        game.checkMagicalStaffFireballCollision();
        game.checkScreenBordersFireballCollision();
    }

    public void run(){
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        ArrayList<Barrier> barriers = game.getBarriers();
        Chance chance = getGameSession().getChance();
        Score score = getGameSession().getScore();

        magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);

        fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireball.getWidth(), fireball.getPreferredSize().height);

        for (Barrier barrier : barriers) {
            barrier.setBounds(barrier.getCoordinate().getX(), barrier.getCoordinate().getY(), barrier.getWidth(), barrier.getHeight());
        }
    }

    private void fireBullet(){
        Bullet bullet = game.createHexBullet()[0];
        Bullet bullet2 = game.createHexBullet()[1];
        runningModePage.getGamePanel().add(bullet);
        runningModePage.getGamePanel().add(bullet2);
    }
    public void updateDebris() {
        Iterator<Debris> iterator = game.getActiveDebris().iterator();
        Shape transformedRectangle=game.getStaffOrientation();
        while (iterator.hasNext()) {
            Debris debris = iterator.next();
            runningModePage.getGamePanel().add(debris);
            debris.moveDown();
            if (debris.getCoordinate().getY() > 600) {
                runningModePage.getGamePanel().remove(debris);
                iterator.remove();
            }

            // TODO move collision logic
            // For debris collision with magical staff
            Rectangle2D.Double debrisRectangle = new Rectangle2D.Double(
                    debris.getCoordinate().getX() - debris.debrisImage.getWidth()/2 ,
                    debris.getCoordinate().getY() - debris.debrisImage.getHeight()/2,
                    debris.debrisImage.getWidth(),
                    debris.debrisImage.getHeight()
            );
            if (transformedRectangle.intersects(debrisRectangle)) {
                game.getChance().decrementChance();
                runningModePage.getGamePanel().remove(debris);
                iterator.remove();
            }
        }
    }
    public void updateDroppingSpells() {
        Iterator<Spell> iterator = game.getSpells().iterator();
        Shape transformedRectangle=game.getStaffOrientation();
        while (iterator.hasNext()) {
            Spell spell = iterator.next();
            runningModePage.getGamePanel().add(spell);
            spell.moveDown();
            if (spell.getCoordinate().getY() > 600) {
                runningModePage.getGamePanel().remove(spell);
                iterator.remove();
            }
            // TODO move collision logic
            // For spell collision with magical staff
            Rectangle2D.Double spellRectangle = new Rectangle2D.Double(
                    spell.getCoordinate().getX() - spell.spellImage.getWidth()/2 ,
                    spell.getCoordinate().getY() - spell.spellImage.getHeight()/2,
                    spell.spellImage.getWidth(),
                    spell.spellImage.getHeight()
            );
            //THIS WILL BE UPDATED SO THAT THE SPELL APPEARS IN THE INVENTORY
            if (transformedRectangle.intersects(spellRectangle)) {
                SpellType type = spell.getSpellType();
                if(getGameInventory().containsKey(type)) {
                    getGameInventory().put(type,getGameInventory().get(type)+1);
                }
                else{
                    getGameInventory().put(type,1);
                }
                runningModePage.getGamePanel().remove(spell);
                iterator.remove();
            }
        }
    }
    public void updateHexBullets(){
        Iterator<Bullet> iterator = game.getActiveBullets().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            runningModePage.getGamePanel().add(bullet);
            bullet.moveUp();
            if (bullet.getCoordinate().getY() < 0) { //Out of Screen Top Border
                runningModePage.getGamePanel().remove(bullet);
                iterator.remove();
            }
           if (game.checkHexBulletCollision(bullet, runningModePage.timeInSeconds)){
               runningModePage.getGamePanel().remove(bullet);
               iterator.remove();
           }

        }

    }

    //Not used yet:
    public void saveGame(String gameName, int timeInSeconds, ArrayList<Debris> activeDebris){
        ArrayList<Barrier> barriers = game.getBarriers();
        Document gameSession = new Document();
        gameSession.put("email", User.getUserInstance().getEmail());
        gameSession.put("gameName", gameName);
        gameSession.put("score", getGameSession().getScore().getScore());
        gameSession.put("timeElapsed", timeInSeconds);

        for(int i=0; i<barriers.size(); i++){
            gameSession.put("barrier_"+i, barriers.get(i).getCoordinate().getX() + "-"+barriers.get(i).getCoordinate().getY() +
                    "-"+ barriers.get(i).getType().toString()+ "-" + barriers.get(i).getnHits());
        }

        ArrayList<Document> debrisList = new ArrayList<>();
        for (Debris debris : activeDebris) {
            Document debrisDoc = new Document();
            debrisDoc.put("x", debris.getCoordinate().getX());
            debrisDoc.put("y", debris.getCoordinate().getY());
            debrisList.add(debrisDoc);
        }

        gameSession.put("debris", debrisList);
        gameSession.put("played", "True");

        Coordinate staffCoord = game.getMagicalStaff().getCoordinate();
        gameSession.put("magicalStaff", new Document("x", staffCoord.getX()).append("y", staffCoord.getY()));

        // Save fireball details
        Fireball fireball = game.getFireball();
        gameSession.put("fireball", new Document("x", fireball.getCoordinate().getX())
                .append("y", fireball.getCoordinate().getY())
                .append("velocityX", fireball.getxVelocity())
                .append("velocityY", fireball.getyVelocity()));

        Database.getInstance().getGameCollection().insertOne(gameSession);
        JOptionPane.showMessageDialog(null, "Game saved successfully!");

    }


    //FELIX_FELICIS
    public void useFelixFelicis(){
        if (game.useFelixFelicis()){ // check if spell was used
            runningModePage.playSoundEffect(5);
        }
    }
    //STAFF_EXPANSION
    public void useMSExpansion(){
        if (game.useStaffExpansion()){
        runningModePage.playSoundEffect(3);
        }
    }
    // Hex
    public void useHex(){
        if(game.useHex()){
            runningModePage.playSoundEffect(3);  // Change SFX
        }
    }
    //Overwhelming Fireball
    public void useOverwhelmingFB(){
        if (game.useOverwhelmingFB()){
            runningModePage.playSoundEffect(3); // Change SFX
        }
    }
    public void volume(int i){
        runningModePage.volume((float) (0.1*i));
    }

    public void checkYmirAbilities() {
        if (new Random().nextBoolean()) { // Simulate the coin flip
            Ymir ymir = new Ymir(game);
            if (new Random().nextBoolean()) {
                ymir.activateInfiniteVoid();
            } else {
                ymir.activateHollowPurple();
            }
        }
    }
    public void saveGameToDatabase() {
        dataBaseController.saveGameToDatabase(game.getGameName(), game,true);
    }
    public HashMap<SpellType, Integer> getGameInventory(){
        return game.getInventory();
    }
    public ArrayList<Spell> getGameSpells(){
        return game.getSpells();
    }
    public ArrayList<Bullet> getGameBullets(){
        return game.getActiveBullets();
    }
    public ArrayList<Debris> getGameDebris(){
        return game.getActiveDebris();
    }
}
