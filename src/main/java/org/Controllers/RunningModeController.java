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
import java.util.Iterator;

import static org.Views.RunningModePage.COLLISION_COOLDOWN;


public class RunningModeController {
    protected RunningModePage runningModePage;
    private Game game;
    private long lastCollisionTime = 0; // Time of the last collision in milliseconds

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.game= Game.getInstance();
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
                            //System.out.println("Space between : "+ (br2.getCoordinate().getX() - newpos));
                            if (width*4.5>Math.abs(br2.getCoordinate().getX() - newpos)) {
                                isAvailable = false;
                                //br2.setVelocity(-1*br2.getVelocity());
                                br.setVelocity(-1*br.getVelocity());
                                break;
                            }}
                    }
                    if (isAvailable) {
                        br.moveBarrier();
                    }
                }
            }
        }
    }

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
            lastCollisionTime = currentTime;
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
            fireball.setLastCollided(null);
            fireball.setCoordinate(new Coordinate((int) (fireballX+-1*(Math.signum(xVelocity)*10)),fireballY));
            //Shift the ball 10 pixels to prevent additional collisions

            fireball.setxVelocity(-xVelocity);// Reverse X velocity

        }
        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <= -10)  {
            fireball.setyVelocity(-yVelocity);// TOP
            fireball.setLastCollided(null);
        }

        else if (fireballY + fireballRadius >= containerHeight) {
            // BOTTOM
            fireball.setLastCollided(null);
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
            fireball.setyVelocity(-3);
            fireball.getCoordinate().setX(fireballPositionX);
            fireball.getCoordinate().setY(fireballPositionY);
            fireball.setBounds(fireballPositionX, fireballPositionY, fireballWidth, fireballHeight);
            //fireball.setBackground(Color.red);
            fireball.setBackground(new Color(0, 0, 0, 0)); // Transparent background
            fireball.setOpaque(true);

        }
    }

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

                if (fireball.isOverwhelming()) return; // no collision if it is

                if (br==fireball.getLastCollided()) return;

                fireball.setLastCollided(br);

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

                if (hitBarrier(br)) {
                    toRemove.add(br);
                }
            }
        }
        barriers.removeAll(toRemove);
        // Updating the score.
        this.getGameSession().getScore().incrementScore(toRemove.size(), this.runningModePage.timeInSeconds);
    }


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
                // Barriers are always horizontal
                /*
                double b = 1.0; // b = 1 for a perfect elastic collision
                double normalAngleRadians = Math.toRadians((double) (90%360));
                Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
                Vector velocity = new Vector(xVelocity, yVelocity);
                Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);

                 */
                //System.out.println(brRect.getX()+" "+brRect.getY()+" "+brRect.getWidth()+" "+brRect.getHeight());
                Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 1, 1, 13);
                Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 1, 1, 13);

                if ((sideLRect.intersects(fireballRect)) || (sideRRect.intersects(fireballRect))) {
                    fireball.setxVelocity(-xVelocity);
                } else {
                    fireball.setyVelocity(-yVelocity);
                }

                if (hitBarrier(br)) {
                    toRemove.add(br);
                }
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

    public boolean hitBarrier(Barrier barrier) {
        barrier.setnHits(barrier.getnHits() - 1);
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
                runningModePage.getGamePanel().remove(spell);
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

    //Temporarily here - melih
    public void useSpell1(){ // I will move these methods to Inventory later, this is for testing -Melih
        getGameSession().getChance().incrementChance();
    }
    public void useSpell2(){
        getGameSession().getMagicalStaff().setStaffWidth(200);
           }
    public void redoSpell2(){
        getGameSession().getMagicalStaff().setStaffWidth(100);
        System.out.println("check2");
    }
}
