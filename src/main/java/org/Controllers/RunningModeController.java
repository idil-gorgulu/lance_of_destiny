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
        Ymir ymir = game.getYmir();
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
            if (transformedRectangle.intersects(spellRectangle)) {
                this.game.getInventory().updateInventory(spell.getSpellType(), +1);
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

    public void updatePurpleBarriers(){
        Iterator<Barrier> iterator = game.getPurpleBarriers().iterator();
        while (iterator.hasNext()) {
            Barrier b = iterator.next();
            runningModePage.getGamePanel().add(b);
            iterator.remove();
        }
    }

    public void volume(int i){
        runningModePage.volume((float) (0.1*i));
    }

    public void checkYmirAbilities() {
        if (new Random().nextBoolean()) { // Simulate the coin flip
            if (new Random().nextBoolean()) {
                game.getYmir().activateInfiniteVoid();
            } else {
                game.getYmir().activateHollowPurple();
            }
        }
    }

    public void saveGameToDatabase() {
        dataBaseController.saveGameToDatabase(game.getGameName(), game,true);
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
