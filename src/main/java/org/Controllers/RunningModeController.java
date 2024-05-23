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
    private long lastCollisionTime = 0; // Time of the last collision in milliseconds

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
        this.getGameSession().getFireball().updateFireballView();
    }
    public void updateMagicalStaffView(){
        this.getGameSession().getMagicalStaff().updateMagicalStaffView();
    }

    // These are the functions for updating the position and angle orientation of the magical staff.
    public void slideMagicalStaff(int x){
        this.getGameSession().getMagicalStaff().setVelocity(x);
    }

    public void rotateMagicalStaff(double dTheta){
        this.getGameSession().getMagicalStaff().setAngVelocity(dTheta);
    }

    public void moveBarriers(){
        int newpos;
        boolean isAvailable;
        int width= 10;
        for (Barrier br: getGameSession().getBarriers()){
            if (br.isMoving()) {
                if (br.getType() == BarrierType.EXPLOSIVE) {
                    if (br.getVelocity() != 0) {
                        br.moveBarrier();                   }               }
                else {
                    isAvailable = true;
                    newpos = br.getCoordinate().getX() +  br.getVelocity();
                    for (Barrier br2 : getGameSession().getBarriers()) {
                        if ((!br2.equals(br)) && (br.getCoordinate().getY()==br2.getCoordinate().getY())){
                            if (width*4.5>Math.abs(br2.getCoordinate().getX() - newpos)) {
                                isAvailable = false;
                                br.setVelocity(-1*br.getVelocity());
                                break;
                            }

                        }
                    }
                    if (isAvailable) {
                        br.moveBarrier();
                    }
                }
            }
        }
    }


    /**
     * Checks if the fireball and magical staff collide, and if they do, updates the fireball's velocity accordingly.
     *
     * <p></p>REQUIRES:
     * - The game must have previously set fireball and staff objects.
     * - The fields of fireball and staff must be set correctly.
     * - Both fireball and staff must be within the screen boundaries.
     *
     * <p>MODIFIES: fireball.xVelocity, fireball.yVelocity
     *
     * <p>EFFECTS:
     * - Creates two Rectangle2D objects representing the bounding boxes around the fireball and the magical staff, considering their coordinates and orientation.
     * - Checks if these rectangles intersect.
     * - If they intersect, calculates the new velocity values for the fireball based on the collision angle and updates the fireball's velocity.
     * - Plays a sound effect upon collision.
     * - Ensures a cooldown period between consecutive collision checks to avoid multiple detections of the same collision.
     */
    public void checkMagicalStaffFireballCollision() {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCollisionTime < COLLISION_COOLDOWN) {
            return; // Skip collision check if we are within the cooldown period
        }

        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        int magicalStaffVelocity = magicalStaff.getVelocity();

        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        double msAngle = magicalStaff.getAngle();
        double angleRadians = Math.toRadians(msAngle);

        Rectangle2D.Double fireballRectangle = new Rectangle2D.Double(
                fireball.getCoordinate().getX() - fireball.getFireballRadius(),
                fireball.getCoordinate().getY() - fireball.getFireballRadius(),
                2 * fireball.getFireballRadius(),
                2 * fireball.getFireballRadius()
        );

        Rectangle2D.Double magicalStaffRectangle = new Rectangle2D.Double(
                magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                magicalStaff.getTopLeftCornerOfMagicalStaff().getY(),
                magicalStaff.getStaffWidth(),
                magicalStaff.getStaffHeight()
        );

        AffineTransform transform = new AffineTransform();
        double centerX = magicalStaffRectangle.getCenterX();
        double centerY = magicalStaffRectangle.getCenterY();
        transform.rotate(angleRadians, centerX, centerY);
        Shape transformedRectangle = transform.createTransformedShape(magicalStaffRectangle);

        if (transformedRectangle.intersects(fireballRectangle)) {
            fireball.setLastCollided(null);
            //System.out.println("\nCollision detected");
            runningModePage.playSoundEffect(1);
            lastCollisionTime = currentTime;

            double energy=xVelocity*xVelocity+yVelocity*yVelocity;
            //System.out.println("\nold: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

            double u=xVelocity*Math.cos(angleRadians)+yVelocity*Math.sin(angleRadians);
            double v=xVelocity*Math.sin(angleRadians)-yVelocity*Math.cos(angleRadians);

            double reflectionX=u*Math.cos(angleRadians)-v*Math.sin(angleRadians);
            double reflectionY=u*Math.sin(angleRadians)+v*Math.cos(angleRadians);
            fireball.setxVelocity(reflectionX);
            fireball.setyVelocity(reflectionY);
            energy=reflectionX*reflectionX+reflectionY*reflectionY;
            //System.out.println("new: " + fireball.getxVelocity() + " " + fireball.getyVelocity()+" "+energy);

            /*
            if (Math.abs(msAngle)<1e-5){
                if (xVelocity*magicalStaffVelocity>0){ //staff & ball same direction
                   // System.out.println("same direction");
                    fireball.setxVelocity( xVelocity+  Math.signum(xVelocity) * 0.5);
                }
                else if (xVelocity*magicalStaffVelocity<0){ //opposite direction
                    //System.out.println("opp direction");
                    fireball.setxVelocity(-xVelocity);
                }
                fireball.setyVelocity(-fireball.getyVelocity());

            }
            else {
                System.out.println("Magical Staff angle: " + -msAngle);
                double normalAngle = Math.toRadians((-msAngle + 90));
                System.out.println(normalAngle);
                Vector normal = new Vector(Math.cos(normalAngle), Math.sin(normalAngle));
                System.out.println("Cos and sin " + Math.cos(normalAngle) + " " + Math.sin(normalAngle));
                Vector velocity = new Vector(xVelocity, yVelocity);
                double dProd = normal.dot(velocity);
                double reflectionX = velocity.getX() - 2 * dProd * normal.getX();
                double reflectionY = velocity.getY() - 2 * dProd * normal.getY();
                //Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal)));
                System.out.println("old: " + fireball.getxVelocity() + " " + fireball.getyVelocity());
                fireball.setxVelocity(-reflectionX);
                fireball.setyVelocity(reflectionY);
                System.out.println("new: " + fireball.getxVelocity() + " " + fireball.getyVelocity());
            }

            */
        }

    }

    public void checkScreenBordersFireballCollision(){

        Fireball fireball = game.getFireball();
        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();
        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        int containerWidth = 1000;
        int containerHeight = 600;

        // Check collision with left and right boundaries
        if ((fireballX - fireballRadius <= 0) || (fireballX + fireballRadius > containerWidth - 10)) {
            runningModePage.playSoundEffect(1);
            fireball.setLastCollided(null);
            fireball.setxVelocity(-xVelocity);// Reverse X velocity
        }
        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <= -10)  {
            runningModePage.playSoundEffect(1);
            fireball.setyVelocity(-yVelocity);// TOP
            fireball.setLastCollided(null);
        }

        else if (fireballY + fireballRadius >= containerHeight) {
            // BOTTOM
            fireball.setLastCollided(null);
            this.getGameSession().getChance().decrementChance();
            runningModePage.playSoundEffect(2);
            if (this.getGameSession().getChance().getRemainingChance() == 0) {
                game.started = false;
                System.out.println("Not active");
                runningModePage.stopMusic();
                return;
            }
            int fireballWidth = fireball.getPreferredSize().width;
            int fireballPositionX = (1000 - fireballWidth) / 2; // make these dynamic
            int fireballHeight = fireball.getPreferredSize().height;
            int fireballPositionY = (500 - fireballHeight - 200); // make these dynamic
            fireball.setxVelocity(3);
            fireball.setyVelocity(-3);
            fireball.getCoordinate().setX(fireballPositionX);
            fireball.getCoordinate().setY(fireballPositionY);
            fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
            fireball.setOverwhelming(false);
            //fireball.setBackground(Color.red);
            fireball.setBackground(new Color(0, 0, 0, 0)); // Transparent background
            fireball.setOpaque(true);

        }
    }

    /**
     * Checks for collisions between the fireball and barriers, updates velocities accordingly,
     * removes destroyed barriers, plays sound effects, and updates the score.
     *
     * @requires game != null && game.getFireball() != null && game.getBarriers() != null && runningModePage != null && this.getGameSession() != null
     * @modifies game.getBarriers(), fireball, this.getGameSession().getScore()
     * @effects
     *      - Detects and processes collisions between the fireball and barriers.
     *      - Updates the fireball's velocity based on the collision direction.
     *      - Removes barriers that are hit by the fireball.
     *      - Plays a sound effect when a collision occurs.
     *      - Updates the score based on the number of barriers removed.
     */
    public void checkBarrierFireballCollision(){
        ArrayList<Barrier> barriers = game.getBarriers();
        ArrayList<Barrier> toRemove = new ArrayList<>();

        Fireball fireball = game.getFireball();
        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        Rectangle2D.Double fireballRectangle = new Rectangle2D.Double(
                fireball.getCoordinate().getX() - fireball.getFireballRadius(),
                fireball.getCoordinate().getY() - fireball.getFireballRadius(),
                2 * fireball.getFireballRadius(),
                2 * fireball.getFireballRadius()
        );

        for (Barrier br : barriers) {
            Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRectangle)) {
                runningModePage.playSoundEffect(1);

                if (br==fireball.getLastCollided()) return;

                fireball.setLastCollided(br);
                if (!fireball.isOverwhelming()){ // no collision if it is
                Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 5, 1, 5);
                Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 5, 1, 5);

                if ((sideLRect.intersects(fireballRectangle)) || (sideRRect.intersects(fireballRectangle))) {
                    // System.out.println("side collision");
                    fireball.setxVelocity(-xVelocity);
                } else {
                    if (xVelocity*br.getVelocity()>0){ //barrier & ball same direction
                        fireball.setxVelocity( xVelocity+  Math.signum(xVelocity) * 0.5);
                    }
                    else if (xVelocity*br.getVelocity()<0){ //opposite direction
                        fireball.setxVelocity(-xVelocity);
                    }
                    fireball.setyVelocity(-yVelocity);
                }
                if (hitBarrier(br,1)) {
                    toRemove.add(br);
                }}
                else {
                    if (hitBarrier(br,10)){ //This is always true
                        toRemove.add(br);
                    }
                }
            }
        }
        barriers.removeAll(toRemove);
        // Updating the score.
        this.getGameSession().getScore().incrementScore(toRemove.size(), this.runningModePage.timeInSeconds);
    }


    /*
    public void checkCollision() {
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        ArrayList<Barrier> barriers = game.getBarriers();
        ArrayList<Barrier> toRemove = new ArrayList<>();

        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();
        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        int magicalStaffX = magicalStaff.getCoordinate().getX();
        int magicalStaffY = magicalStaff.getCoordinate().getY();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;
        int magicalStaffVelocity = magicalStaff.getVelocity();
        double magicalStaffAngle = magicalStaff.getAngle();

        double normalAngle = (magicalStaffAngle + Math.PI / 2) % (Math.PI * 2);

        Rectangle staffRect = new Rectangle(magicalStaffX, magicalStaffY, magicalStaffWidth, magicalStaffHeight);
        Rectangle fireballRect = new Rectangle(fireballX - fireballRadius, fireballY - fireballRadius, fireballRadius * 2, fireballRadius * 2);

        if (staffRect.intersects(fireballRect)) {
            // System.out.println("Ball: "+xVelocity+" "+yVelocity+       "\nStaff: "+magicalStaffVelocity+" "+Math.toDegrees(magicalStaffAngle));

            // The collision formula: Vnew = b * (-2*(V dot N)*N + V)
            // b: 1 for elastic collision, 0 for 100% moment loss
            // V: previous velocity vector
            // N: normal vector of the surface collided with

            double b = 1.0; // b = 1 for a perfect elastic collision
            Vector normal = new Vector(Math.cos(normalAngle), Math.sin(normalAngle));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);

            Rectangle sideLRect = new Rectangle(magicalStaffX, magicalStaffY + 1, 1, 18);
            Rectangle sideRRect = new Rectangle(magicalStaffX + 99, magicalStaffY + 1, 1, 18);

            if (sideRRect.intersects(fireballRect) || sideLRect.intersects(fireballRect)) {

                fireball.setyVelocity(-yVelocity);
            } else {
                if (xVelocity * magicalStaffVelocity > 0) { //staff & ball same direction

                    if (xVelocity < 0) fireball.setxVelocity(xVelocity - 1);
                    else fireball.setxVelocity(xVelocity + 1);
                    fireball.setyVelocity(-yVelocity);
                } else if (magicalStaffVelocity == 0) {   // staff stationary
                    fireball.setxVelocity(xVelocity); // does absolutely nothing
                    fireball.setyVelocity(-yVelocity);
                } else if (xVelocity * magicalStaffVelocity < 0) { //opposite direction
                    //System.out.println("opp direction");
                    fireball.setxVelocity(-xVelocity);
                    fireball.setyVelocity(-yVelocity);
                    fireball.getCoordinate().setY(fireball.getCoordinate().getY()-10); //to prevent multiple calls to intersect func.


                }
            }
        }
        for (Barrier br : barriers) {
            //System.out.println("size"+br.getPreferredSize().getWidth()+ (int) br.getPreferredSize().getHeight());
            Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRect)) {
                 Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 1, 1, 13);
                Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 1, 1, 13);

                if ((sideLRect.intersects(fireballRect)) || (sideRRect.intersects(fireballRect))) {
                    fireball.setxVelocity(-xVelocity);
                } else {
                    fireball.setyVelocity(-yVelocity);
                }

               // if (hitBarrier(br)) {
                    toRemove.add(br);
                //}
            }
        }
        barriers.removeAll(toRemove);

        int containerWidth = 1000;
        int containerHeight = 600;

        /// Check collision with left and right boundaries
        if (fireballX - fireballRadius <= 0 ) {
            xVelocity *= -1; // Reverse X velocity
            fireball.setxVelocity(xVelocity);
        }
        if ( fireballX + fireballRadius >= 1000) {
            xVelocity *= -1; // Reverse X velocity
            fireball.getCoordinate().setX(990); //to block getting in this if again
            fireball.setxVelocity(xVelocity);
            System.out.println("leftright boundary");
        }

        // Check collision with top and bottom boundaries

        if (fireballY - fireballRadius <= -10) {
            // TOP
            yVelocity *= -1; // Reverse Y velocity
            fireball.setyVelocity(yVelocity);
        }

        else if (fireballY + fireballRadius >= 600) {
            // BOTTOM
            this.getGameSession().getChance().decrementChance();
            if (this.getGameSession().getChance().getRemainingChance() == 0) {
                game.started = false;
                System.out.println("Not active");
                return;
            }
            int fireballWidth = fireball.getPreferredSize().width;
            int fireballPositionX = (1000 - fireballWidth) / 2; // make these dynamic
            int fireballHeight = fireball.getPreferredSize().height;
            int fireballPositionY = (500 - fireballHeight - 200); // make these dynamic
            fireball.setxVelocity(3);
            fireball.setxVelocity(3);
            fireball.getCoordinate().setX(fireballPositionX);
            fireball.getCoordinate().setY(fireballPositionY);
            fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
            //fireball.setBackground(Color.red);
            fireball.setBackground(new Color(0, 0, 0, 0)); // Transparent background
            fireball.setOpaque(true);

        }

    }
*/
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

    public boolean hitBarrier(Barrier barrier, int hitTimes) {
        barrier.setnHits(barrier.getnHits() - hitTimes);
        //barrier.revalidate();
        //barrier.repaint();
        if (barrier.getnHits() <= 0) {
            barrier.destroy();
            if(barrier.getType()==BarrierType.EXPLOSIVE){
                explodeBarrier(barrier);
            }
            else if(barrier.getType()==BarrierType.REWARDING){
                dropSpell(barrier);
            }
            return true;
        }

        return false;

        /*if(barrier.getType()==BarrierType.REWARDING){
            //DROP SPELL
        }*/
    }


    private void explodeBarrier(Barrier barrier) {
        Debris debris = new Debris(barrier.getCoordinate());
        debris.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        runningModePage.getActiveDebris().add(debris); // Add debris to the list
        runningModePage.getGamePanel().add(debris);
        //runningModePage.repaint();
    }

    public void fireBullet(){
        MagicalStaff magicalStaff=game.getMagicalStaff();
        Bullet bullet=new Bullet(new Coordinate(magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                                                magicalStaff.getTopLeftCornerOfMagicalStaff().getY()));
        bullet.setBackground(new Color(0, 0, 0, 0));

        Bullet bullet2=new Bullet(new Coordinate(magicalStaff.getTopLeftCornerOfMagicalStaff().getX()+magicalStaff.getStaffWidth(),
                                                    magicalStaff.getTopLeftCornerOfMagicalStaff().getY()));
        bullet2.setBackground(new Color(0, 0, 0, 0));

        runningModePage.getGamePanel().add(bullet);
        runningModePage.getGamePanel().add(bullet2);
        runningModePage.getActiveBullets().add(bullet);
        runningModePage.getActiveBullets().add(bullet2);
        runningModePage.playSoundEffect(4);
    }
    private void dropSpell(Barrier barrier){
        Spell spell = new Spell(barrier.getCoordinate());
        spell.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        runningModePage.getDroppingSpells().add(spell); // Add spells to the list
        runningModePage.getGamePanel().add(spell);
    }

    public void updateDebris() {
        Iterator<Debris> iterator = runningModePage.getActiveDebris().iterator();
        while (iterator.hasNext()) {
            Debris debris = iterator.next();
            debris.moveDown();
            if (debris.getCoordinate().getY() > 600) {
                runningModePage.getGamePanel().remove(debris);
                iterator.remove();
            }
            // For debris collision with magical staff

            MagicalStaff magicalStaff = game.getMagicalStaff();

            double msAngle = magicalStaff.getAngle();
            double angleRadians = Math.toRadians(msAngle);

            Rectangle2D.Double magicalStaffRectangle = new Rectangle2D.Double(
                    magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                    magicalStaff.getTopLeftCornerOfMagicalStaff().getY(),
                    magicalStaff.getStaffWidth(),
                    20
            );

            Rectangle2D.Double debrisRectangle = new Rectangle2D.Double(
                    debris.getCoordinate().getX() - debris.debrisImage.getWidth()/2 ,
                    debris.getCoordinate().getY() - debris.debrisImage.getHeight()/2,
                    debris.debrisImage.getWidth(),
                    debris.debrisImage.getHeight()
            );



            AffineTransform transform = new AffineTransform();
            double centerX = magicalStaffRectangle.getCenterX();
            double centerY = magicalStaffRectangle.getCenterY();
            transform.rotate(angleRadians, centerX, centerY);
            Shape transformedRectangle = transform.createTransformedShape(magicalStaffRectangle);

            if (transformedRectangle.intersects(debrisRectangle)) {
                this.getGameSession().getChance().decrementChance();
                runningModePage.getGamePanel().remove(debris);
                iterator.remove();
            }
        }
    }

    public void updateDroppingSpells() {
        Iterator<Spell> iterator = runningModePage.getDroppingSpells().iterator();
        while (iterator.hasNext()) {
            Spell spell = iterator.next();
            spell.moveDown();
            if (spell.getCoordinate().getY() > 600) {
                runningModePage.getGamePanel().remove(spell);
                iterator.remove();
            }
            // For spell collision with magical staff
            MagicalStaff magicalStaff = game.getMagicalStaff();

            double msAngle = magicalStaff.getAngle();
            double angleRadians = Math.toRadians(msAngle);

            Rectangle2D.Double magicalStaffRectangle = new Rectangle2D.Double(
                    magicalStaff.getTopLeftCornerOfMagicalStaff().getX(),
                    magicalStaff.getTopLeftCornerOfMagicalStaff().getY(),
                    magicalStaff.getStaffWidth(),
                    20
            );

            Rectangle2D.Double spellRectangle = new Rectangle2D.Double(
                    spell.getCoordinate().getX() - spell.spellImage.getWidth()/2 ,
                    spell.getCoordinate().getY() - spell.spellImage.getHeight()/2,
                    spell.spellImage.getWidth(),
                    spell.spellImage.getHeight()
            );


            AffineTransform transform = new AffineTransform();
            double centerX = magicalStaffRectangle.getCenterX();
            double centerY = magicalStaffRectangle.getCenterY();
            transform.rotate(angleRadians, centerX, centerY);
            Shape transformedRectangle = transform.createTransformedShape(magicalStaffRectangle);

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
        ArrayList<Barrier> barriers = game.getBarriers();
        ArrayList<Barrier> toRemove = new ArrayList<>();


        Iterator<Bullet> iterator = runningModePage.getActiveBullets().iterator();
        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();
            bullet.moveUp();
            if (bullet.getCoordinate().getY() < 0) { //Out of Screen Top Border
                runningModePage.getGamePanel().remove(bullet);
                iterator.remove();
            }

            Rectangle2D.Double bulletRectangle = new Rectangle2D.Double( // Barrier collision
                    bullet.getCoordinate().getX()  ,  bullet.getCoordinate().getY(),20,20);

            for (Barrier br : barriers) {
                Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(),
                        (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());
                if (brRect.intersects(bulletRectangle)) {
                    if (hitBarrier(br,1))  toRemove.add(br);
                    runningModePage.getGamePanel().remove(bullet);
                    iterator.remove();
                }
            }
            barriers.removeAll(toRemove);
            // Updating the score.
            this.getGameSession().getScore().incrementScore(toRemove.size(), this.runningModePage.timeInSeconds);
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

    //Temporarily here - melih
    //FELIX_FELICIS
    public void useSpell1(){ // I will move these methods to somewhere else later, this is for testing -Melih
        int remaining=getGameInventory().get(SpellType.FELIX_FELICIS);
        if (remaining>0) {
            getGameSession().getChance().incrementChance();
            getGameInventory().put(SpellType.FELIX_FELICIS,remaining -1 );
        }

    }

    public void useSpell2(){ //STAFF_EXPANSION
        int remaining=getGameInventory().get(SpellType.STAFF_EXPANSION);
        if (remaining>0) {
            getGameSession().getMagicalStaff().setStaffWidth(200);
            runningModePage.playSoundEffect(3);
            getGameInventory().put(SpellType.STAFF_EXPANSION,  remaining -1 );

            MagicalStaff magicalStaff= game.getMagicalStaff();
            magicalStaff.setExpansionTime(System.currentTimeMillis());
        }
    }

    public void redoSpell2(){
        getGameSession().getMagicalStaff().setStaffWidth(100);
        runningModePage.playSoundEffect(3);
    }
    public void volume(int i){
        runningModePage.volume((float) (0.1*i));
    }
    //Put for Testing
    public void setLastCollisionTime(long time){
        this.lastCollisionTime=time;
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
}
