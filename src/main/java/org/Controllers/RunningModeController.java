package org.Controllers;

import org.Domain.*;
import org.Views.RunningModePage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


public class RunningModeController {
    protected RunningModePage runningModePage;
    private Game game;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.game= Game.getInstance();
    }
    //public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {       magicalStaff.rotate(dTheta);    }

    public Game getGameSession() {
        return game;
    }

    public void moveFireball(){
        this.getGameSession().getFireball().moveFireball();
    }
    public void moveStaff(){
        getGameSession().getMagicalStaff().moveMagicalStaff();
        getGameSession().getMagicalStaff().rotateMagicalStaff();}


    public void slideMagicalStaff(int x) {
       getGameSession().getMagicalStaff().setVelocity(x);
    }
    public void rotateMagicalStaff(double dTheta){ // If the speed of rotation also matters, I will move this method to MagicalStaff as well. -Melih
        getGameSession().getMagicalStaff().setAngularVel(dTheta);
    }
    public void stabilizeMagicalStaff(boolean cw){// Work In Progress -Melih
        getGameSession().getMagicalStaff().stabilize(cw);
    }

    public void checkCollision(){
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        ArrayList<Barrier> barriers = game.getBarriers();

        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();

        int magicalStaffX = magicalStaff.getCoordinate().getX();
        int magicalStaffY = magicalStaff.getCoordinate().getY();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;
        int magicalStaffVelocity= magicalStaff.getVelocity();
        double magicalStaffAngle = magicalStaff.getAngle();


        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();
        double normalAngle = (magicalStaffAngle + Math.PI/2) % (Math.PI*2);

        Rectangle staffRect = new Rectangle(magicalStaffX, magicalStaffY, magicalStaffWidth, magicalStaffHeight);
        Rectangle fireballRect = new Rectangle(fireballX - fireballRadius, fireballY - fireballRadius, fireballRadius * 2, fireballRadius * 2);

        if (staffRect.intersects(fireballRect)) {
            // System.out.println("Ball: "+xVelocity+" "+yVelocity+       "\nStaff: "+magicalStaffVelocity+" "+Math.toDegrees(magicalStaffAngle));

            // The collision formula: Vnew = b * (-2*(V dot N)*N + V)
            // b: 1 for elastic collision, 0 for 100% moment loss
            // V: previous velocity vector
            // N: normal vector of the surface collided with

            double b = 1.0; // b = 1 for a perfect elastic collision
            //double normalAngleRadians = Math.toRadians(normalAngle); Already in radians
            Vector normal = new Vector(Math.cos(normalAngle), Math.sin(normalAngle));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);

            System.out.println("\nBall before: "+xVelocity+" "+yVelocity+ " staff: "+magicalStaffVelocity+" normal angle: "+Math.toDegrees(normalAngle));
            if (xVelocity*magicalStaffVelocity>0){ //staff & ball same direction

                System.out.println("same direction");
                //fireball.setxVelocity(vNew.getX()*1.5);
                //fireball.setyVelocity(-vNew.getY());
                fireball.setxVelocity(xVelocity*1.5);
                fireball.setyVelocity(-yVelocity);
            }
            else if (magicalStaffVelocity==0){   // staff stationary
                System.out.println("stationary");
                fireball.setxVelocity(xVelocity); // does absolutely nothing
                fireball.setyVelocity(-yVelocity);
                //fireball.setyVelocity(-vNew.getY());
            }
            else if (xVelocity*magicalStaffVelocity<0){ //opposite direction
                System.out.println("opp direction");
                fireball.setxVelocity(-xVelocity);
                fireball.setyVelocity(-yVelocity);
               // fireball.setxVelocity(-vNew.getX());
                //fireball.setyVelocity(-vNew.getY());
            }
            System.out.println("Ball after: "+fireball.getxVelocity()+" "+fireball.getyVelocity());


            //System.out.println(magicalStaff.getVelocity());

        }
        for(Barrier br: barriers){
            //System.out.println("size"+br.getPreferredSize().getWidth()+ (int) br.getPreferredSize().getHeight());
            Rectangle brRect = new Rectangle(br.getCoordinates().getX(), br.getCoordinates().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRect)) {
                // Barriers are always horizontal
                /*
                double b = 1.0; // b = 1 for a perfect elastic collision
                double normalAngleRadians = Math.toRadians((double) (90%360));
                Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
                Vector velocity = new Vector(xVelocity, yVelocity);
                Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);

                 */
                //fireball.setxVelocity(fireball.getyVelocity()); Ignoring collisions from sides
                fireball.setyVelocity(-fireball.getyVelocity());
                hitBarrier(br);
            }
        }

        int containerWidth = 1000;
        int containerHeight = 600;

        // Check collision with left and right boundaries
        if (fireballX - fireballRadius <= 0 || fireballX + fireballRadius >= containerWidth) {
            xVelocity *= -1; // Reverse X velocity
            fireball.setxVelocity(xVelocity);
        }

        // Check collision with top and bottom boundaries
        //REMOVE bottom boundary and connect it to loseChance
        if (fireballY - fireballRadius <=-10|| fireballY + fireballRadius >= 600) {
            yVelocity *= -1; // Reverse Y velocity
            fireball.setyVelocity(yVelocity);
        }
    }

    public void run(){
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        ArrayList<Barrier> barriers = game.getBarriers();
        Chance chance = getGameSession().getChance();
        Score score= getGameSession().getScore();

        magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);

        fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireball.getWidth(), fireball.getPreferredSize().height);

        for (Barrier barrier : barriers) {
            barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getWidth(), barrier.getHeight());
        }
    }

    public void hitBarrier(Barrier barrier) {
        barrier.setnHits(barrier.getnHits() - 1);
        if (barrier.getnHits() <= 0) {
            barrier.destroy();
            game.getBarriers().remove(barrier);
            if(barrier.getType()==BarrierType.EXPLOSIVE){
                Debris debris = new Debris(barrier.getCoordinates());
                runningModePage.add(debris);

            }
            runningModePage.revalidate();
            runningModePage.repaint();
        }

        /*if(barrier.getType()==BarrierType.REWARDING){
            //DROP SPELL
        }*/
    }

    private void startDebrisAnimation(Debris debris) {
        Timer timer = new Timer(50, e -> {
            debris.moveDebris();
            runningModePage.revalidate();
            runningModePage.repaint();
            checkDebrisCollision(debris);
        });
        timer.start();
    }

    private void checkDebrisCollision(Debris debris) {
        MagicalStaff magicalStaff = game.getMagicalStaff();
        Rectangle staffRect = new Rectangle(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(),
                magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);

        Rectangle debrisRect = new Rectangle(debris.getCoordinate().getX(), debris.getCoordinate().getY(),
                debris.getWidth(), debris.getHeight());

        if (staffRect.intersects(debrisRect)) {
            game.getChance().decrementChance();
            runningModePage.remove(debris);
        }
    }
}
