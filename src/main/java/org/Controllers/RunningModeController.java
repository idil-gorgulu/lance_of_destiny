package org.Controllers;

import org.Domain.*;
import org.Views.RunningModePage;

import java.awt.*;
import java.util.ArrayList;


public class RunningModeController {
    private RunningModePage runningModePage;
    private Game game;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.game= Game.getInstance();
    }
    public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {
        magicalStaff.rotate(dTheta);
    }

    public Game getGameSession() {
        return game;
    }

    public void moveFireball(){
        this.getGameSession().getFireball().moveFireball();
    }


    public void slideMagicalStaff(int x, int y){
        getGameSession().getMagicalStaff().slideMagicalStaff(x,y); // Move left
    }
    public void rotateMagicalStaff(double dTheta){
        getGameSession().getMagicalStaff().rotate(dTheta); // Rotate left
    }

    public void checkCollision(){
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        Barrier barrier = game.getBarrier();
        ArrayList<Barrier> barriers = game.getBarriers();

        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();

        int magicalStaffX = magicalStaff.getCoordinate().getX();
        int magicalStaffY = magicalStaff.getCoordinate().getY();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;
        double magicalStaffAngle = magicalStaff.getAngle();


        int xVelocity = fireball.getxVelocity();
        int yVelocity = fireball.getyVelocity();
        double normalAngle = (magicalStaffAngle + 90) % 360;

        Rectangle staffRect = new Rectangle(magicalStaffX, magicalStaffY, magicalStaffWidth, magicalStaffHeight);
        Rectangle fireballRect = new Rectangle(fireballX - fireballRadius, fireballY - fireballRadius, fireballRadius * 2, fireballRadius * 2);
        Rectangle barrierRect = new Rectangle(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), (int) barrier.getPreferredSize().getWidth(), (int) barrier.getPreferredSize().getHeight());

        if (staffRect.intersects(fireballRect)) {
            // The collision formula: Vnew = b * (-2*(V dot N)*N + V)
            // b: 1 for elastic collision, 0 for 100% moment loss
            // V: previous velocity vector
            // N: normal vector of the surface collided with
            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians(normalAngle);
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());

        }
        for(Barrier br: barriers){
            System.out.println("size"+br.getPreferredSize().getWidth()+ (int) br.getPreferredSize().getHeight());
            Rectangle brRect = new Rectangle(br.getCoordinates().getX(), br.getCoordinates().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRect)) {

                double b = 1.0; // b = 1 for a perfect elastic collision
                double normalAngleRadians = Math.toRadians((double) (90%360));
                Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
                Vector velocity = new Vector(xVelocity, yVelocity);
                Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
                fireball.setxVelocity((int) vNew.getX());
                fireball.setyVelocity((int) vNew.getY());
            }
        }
        if (barrierRect.intersects(fireballRect)) {

            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians((double) (90%360));
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());
        }

        int containerWidth = 1000;
        int containerHeight = 600;

        // Check collision with left and right boundaries
        if (fireballX - fireballRadius <= 0 || fireballX + fireballRadius >= containerWidth) {
            xVelocity *= -1; // Reverse X velocity
            fireball.setxVelocity(xVelocity);
        }

        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <=-10|| fireballY + fireballRadius >= 600) {
            yVelocity *= -1; // Reverse Y velocity
            fireball.setyVelocity(yVelocity);
        }
    }

    public void run(){
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();
        Barrier barrier1 = game.getBarrier();
        ArrayList<Barrier> barriers = game.getBarriers();
        Chance chance = getGameSession().getChance();
        Score score= getGameSession().getScore();

        magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);

        fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireball.getWidth(), fireball.getPreferredSize().height);

        barrier1.setBounds(barrier1.getCoordinates().getX(), barrier1.getCoordinates().getY(), barrier1.getWidth(), barrier1.getHeight());

        chance.setBounds(chance.getCoordinate().getX(), chance.getCoordinate().getY(), chance.getWidth(), chance.getHeight());
        score.setBounds(score.getCoordinate().getX(), score.getCoordinate().getY(), score.getWidth(), score.getHeight());
        for (Barrier barrier : barriers) {
            barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getWidth(), barrier.getHeight());
        }
    }
}
