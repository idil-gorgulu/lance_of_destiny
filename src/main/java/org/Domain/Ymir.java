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

    private Queue<String> lastAbilities = new LinkedList<>();
    private Random random = new Random();

        // Constants for the abilities
        private static final String INFINITE_VOID = "Infinite Void";
        private static final String DOUBLE_ACCEL = "Double Accel";
        private static final String HOLLOW_PURPLE = "Hollow Purple";
        private static final String[] ABILITIES = {INFINITE_VOID, DOUBLE_ACCEL, HOLLOW_PURPLE};
        public Ymir() {
            this.coordinate = new Coordinate(890,430);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tryActivateAbility();
                }
            }, 0, 30000);
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
            this.setVisible(true);
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
            System.out.println("Ymir paintComponent called");
        }

        //to be called every 30 seconds from the game loop
        private void tryActivateAbility() {
            // Implementation of Ymir's abilities
        }

    public void stop() {
        timer.cancel();  // Stop the timer when the game ends
    }

        private void activateRandomAbility() {
            String ability;
            do {
                ability = ABILITIES[random.nextInt(ABILITIES.length)];
            } while (isRepeatAbility(ability));

            executeAbility(ability);
            manageAbilityHistory(ability);
        }

        private boolean isRepeatAbility(String ability) {
            // Check if this ability was the last two abilities used
            if (lastAbilities.contains(ability) && lastAbilities.peek().equals(ability)) {
                return true;
            }
            return false;
        }

        private void manageAbilityHistory(String ability) {
            if (lastAbilities.size() >= 2) {
                lastAbilities.poll(); // Remove the oldest if we already have two
            }
            lastAbilities.offer(ability); // Add the new one to the history
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
            List<Barrier> barriers = game.getBarriers();
            Collections.shuffle(barriers);
            barriers.stream()
                    .filter(b -> !b.isFrozen())
                    .limit(8)
                    .forEach(Barrier::freeze);
        }

        private void activateDoubleAccel() {
            System.out.println("Activating Double Accel");
            // Code to reduce fireball speed
        }

        public void activateHollowPurple() {
            System.out.println("Activating Hollow Purple");
            Random random = new Random();
            for (int i = 0; i < 8; i++) { // Add 8 new hollow purple barriers
                int x = random.nextInt(RunningModePage.SCREENWIDTH - 50);
                int y = random.nextInt(500 - 15);
                game.addBarrier(new Coordinate(x, y), BarrierType.HOLLOW_PURPLE);
            }
        }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    private List<Barrier> selectRandomBarriers() {
        Collections.shuffle(game.getBarriers());
        return game.getBarriers().subList(0, Math.min(8, game.getBarriers().size()));
    }

}

