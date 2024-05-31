package org.Domain;

import org.Views.RunningModePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;


public class Ymir extends JPanel {

    private Game game;
    private Timer timer;
    private Coordinate coordinate;
    private BufferedImage ymirImage;
    private BufferedImage coin;
    private Queue<String> lastAbilities = new LinkedList<>();
    private boolean isActive = false;

    private static Ymir instance;
    private Random random = new Random();
    private static final String INFINITE_VOID = "Infinite Void";
    private static final String DOUBLE_ACCEL = "Double Accel";
    private static final String HOLLOW_PURPLE = "Hollow Purple";
    private static final String[] ABILITIES = {INFINITE_VOID, DOUBLE_ACCEL, HOLLOW_PURPLE};
    public Ymir(Game game) {
        this.game = game;
        this.coordinate = new Coordinate(890, 430);

        lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);
        lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);

        try {
            ymirImage = ImageIO.read(new File("assets/ymir.png"));
            ymirImage = resizeImage(ymirImage, 116, 176);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(116, 176));
        this.setOpaque(false);
        this.setVisible(false);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ymirImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - ymirImage.getWidth()) / 2;
            int y = (getHeight() - ymirImage.getHeight()) / 2;
            g2d.drawImage(ymirImage, x, y, this);
            g2d.dispose();
        }
    }

    private void activateRandomAbility() {
        String ability;
        do {
            ability = ABILITIES[random.nextInt(ABILITIES.length)];
        } while (isRepeatAbility(ability));

        executeAbility(ability);
        manageAbilityHistory(ability);
    }

    public void stop() {
    timer.cancel();  // Stop the timer when the game ends
}

    private boolean isRepeatAbility(String ability) {
        if (lastAbilities.size() == 2 && lastAbilities.peek().equals(ability)) {
            return true;
        }
        return false;
    }

    public void manageAbilityHistory(String ability) {
        if (lastAbilities.size() >= 2) {
            lastAbilities.poll();
        }
        lastAbilities.offer(ability);
    }

    private void executeAbility(String ability) {
        switch (ability) {
            case INFINITE_VOID:
                activateInfiniteVoid();
                break;
            case DOUBLE_ACCEL:
                activateDoubleAccel();
                break;
            case HOLLOW_PURPLE:
                activateHollowPurple();
                break;
        }
    }
    public void activateInfiniteVoid() {
        System.out.println("Activating Infinite Void");
        List<Barrier> barriers = game.getBarriers();
        Collections.shuffle(barriers);
        barriers.stream()
                .filter(b -> !b.isFrozen())
                .limit(8)
                .forEach(Barrier::freeze);
    }

    public void activateDoubleAccel() {
        System.out.println("Activating Double Accel");
        Fireball fireball = game.getFireball();
        fireball.setxVelocity(fireball.getxVelocity() / 2);
        fireball.setyVelocity(fireball.getyVelocity() / 2);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Restoring original velocities");
                fireball.setxVelocity(fireball.getxVelocity() * 2);
                fireball.setyVelocity(fireball.getyVelocity() * 2);
            }
        }, 15000);
    }

    public void activateHollowPurple() {
        int numPurpleBarrier = 0;
        System.out.println("Activating Hollow Purple");
        Random random = new Random();
        while(numPurpleBarrier < 8) {
            int x = random.nextInt(RunningModePage.SCREENWIDTH - 50);
            int y = random.nextInt(400 - 15);
            int boardX = x / 50;
            int boardY = y / 20;
            if (game.getBarrierBoard()[boardY][boardX]==null){
                game.addBarrier(new Coordinate(x,y),BarrierType.HOLLOW_PURPLE);
                numPurpleBarrier++;
            }
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    private List<Barrier> selectRandomBarriers() {
        Collections.shuffle(game.getBarriers());
        return game.getBarriers().subList(0, Math.min(8, game.getBarriers().size()));
    }

    public Timer getTimer() {
        return timer;
    }

    public static Ymir getInstance(){
        if (instance==null) {
            instance=new Ymir(Game.createNewGame());
            return instance;
        }
        else{
            return instance;
        }
    }
    // Method to get abilities as a list
    public List<String> getLastAbilitiesAsList() {
        return new ArrayList<>(lastAbilities);
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("YMIR Timer task executed");
                if (random.nextBoolean()) {
                    activateRandomAbility();
                }
            }
        }, 0, 30000);
    }
    // Method to set the Ymir object as active or inactive
    // Method to set the Ymir object as active or inactive
    public void setActive(boolean active) {
        if (this.isActive != active) {
            this.isActive = active;
            this.setVisible(active); // Set visibility based on the active state
            if (isActive) {
                startTimer();
            } else {
                stop();
            }
        }
    }
}

