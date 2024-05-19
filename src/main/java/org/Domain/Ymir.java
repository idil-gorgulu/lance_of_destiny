package org.Domain;

import org.Views.RunningModePage;

import java.util.*;


public class Ymir {

    private Game game;
    private Timer timer;

    private Queue<String> lastAbilities = new LinkedList<>();
    private Random random = new Random();

        // Constants for the abilities
        private static final String INFINITE_VOID = "Infinite Void";
        private static final String DOUBLE_ACCEL = "Double Accel";
        private static final String HOLLOW_PURPLE = "Hollow Purple";
        private static final String[] ABILITIES = {INFINITE_VOID, DOUBLE_ACCEL, HOLLOW_PURPLE};
        public Ymir(Game game) {
            this.game = game;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tryActivateAbility();
                }
            }, 0, 30000);
            lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);
            lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);
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

    private List<Barrier> selectRandomBarriers() {
        Collections.shuffle(game.getBarriers());
        return game.getBarriers().subList(0, Math.min(8, game.getBarriers().size()));
    }

}

