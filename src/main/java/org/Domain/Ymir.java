package org.Domain;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;


public class Ymir {
        private Queue<String> lastAbilities = new LinkedList<>();
        private Random random = new Random();

        // Constants for the abilities
        private static final String INFINITE_VOID = "Infinite Void";
        private static final String DOUBLE_ACCEL = "Double Accel";
        private static final String HOLLOW_PURPLE = "Hollow Purple";
        private static final String[] ABILITIES = {INFINITE_VOID, DOUBLE_ACCEL, HOLLOW_PURPLE};

        public Ymir() {
            // Initialize with two random abilities to start
            lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);
            lastAbilities.offer(ABILITIES[random.nextInt(ABILITIES.length)]);
        }

        // Call this method every 30 seconds from the game loop
        public void tryActivateAbility() {
            if (random.nextBoolean()) { // Coin flip
                activateRandomAbility();
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
        private void activateInfiniteVoid() {
            System.out.println("Activating Infinite Void");
            // Code to freeze barriers
        }

        private void activateDoubleAccel() {
            System.out.println("Activating Double Accel");
            // Code to reduce fireball speed
        }

        private void activateHollowPurple() {
            System.out.println("Activating Hollow Purple");
            // Code to add hollow purple barriers
        }
}

