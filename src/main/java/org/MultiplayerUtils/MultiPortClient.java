package org.MultiplayerUtils;

import org.Domain.Game;
import org.Domain.SpellType;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiPortClient implements CommInterface {
    private List<ConnectedStateChangeListener> connectedStateChangeListeners = new ArrayList<>();
    private List<CountdownStateChangeListener> countdownStateChangeListeners = new ArrayList<>();
    private Socket outputSocket;
    public PrintWriter output;
    private Socket inputSocket;
    public BufferedReader input;
    private String gameHostIpAdress;
    private int outputPort;
    private int inputPort;
    public boolean connected = false;
    public volatile boolean selfReadyClicked = false;
    public boolean opponentReadyClicked = false;
    public boolean startCountdown = false;
    public boolean gameStarted = false;

    public Game getMultiplayerGame() {
        return multiplayerGame;
    }

    public void setMultiplayerGame(Game multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
    }

    public Game multiplayerGame;

    public MultiPortClient(String gameHostIpAdress, int outputPort, int inputPort) {
        this.gameHostIpAdress = gameHostIpAdress;
        this.outputPort = outputPort;
        this.inputPort = inputPort;
    }

    public void addConnectedStateChangeListener(ConnectedStateChangeListener listener) {
        connectedStateChangeListeners.add(listener);
    }
    public void addCountdownStateChangeListener(CountdownStateChangeListener listener) {
        countdownStateChangeListeners.add(listener);
    }


    private void notifyAllConnectedStateChangeListeners() {
        for (ConnectedStateChangeListener listener : connectedStateChangeListeners) {
            listener.onConnectedStateChange();
        }
    }
    private void notifyAllCountdownStateChangeListeners() {
        for (CountdownStateChangeListener listener : countdownStateChangeListeners) {
            listener.onCountdownStateChange();
        }
    }




    public void start() {
        try {
            // This is for receiving messages from the gameHost
            inputSocket = new Socket(gameHostIpAdress, inputPort);
            System.out.println("Connected to gameHost at " + gameHostIpAdress + ":" + inputPort + " for receiving messages at port " + inputSocket.getLocalPort() + ".");
            input = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));

            // This is for sending messages to the gameHost
            outputSocket = new Socket(gameHostIpAdress, outputPort);
            System.out.println("Connected to server at " + gameHostIpAdress + ":" + outputPort + " for sending messages at port " + outputSocket.getLocalPort() + ".");
            output = new PrintWriter(outputSocket.getOutputStream(), true);

            connected = true;
            notifyAllConnectedStateChangeListeners();
            // Make this a function

            //  !!! TODO: I need to stop here until the button is pressed
            while (!selfReadyClicked) {
                Thread.onSpinWait();
                // It will wait until it is
            }
            output.println("gameReady");
            // Wait other player to click as well
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.equals("gameReady")) {
                    System.out.println("Opponent is ready");
                    opponentReadyClicked = true;
                    break;
                }
            }

            // In here says 3 2 1 and then open the game
            startCountdown = true;
            notifyAllCountdownStateChangeListeners();
            System.out.println("here");
            // Assure that the game is loaded
            while (!gameStarted) {
                Thread.onSpinWait();
                // It will wait until it is
            }
            System.out.println("asdf");
            Runnable sendStatisticsRunnable = new Runnable() {
                public void run() {
                    int score = multiplayerGame.getScore().getTotalScore();
                    int barrierCount = multiplayerGame.getBarriers().size();
                    int chance = multiplayerGame.getChance().getRemainingChance();
                    output.println(String.format("GameInformation: {score: %d, barrierCount: %d, chance: %d}", score, barrierCount, chance));
                }
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(sendStatisticsRunnable, 0, 1, TimeUnit.SECONDS);

            Thread receiveThread = new Thread(this::receiveFromServer);
            receiveThread.start();

            sendToServer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    private void receiveFromServer() {
        try {
            String fromServer;
            while ((fromServer = input.readLine()) != null) {
                System.out.println("Server says: " + fromServer);
                processInput(fromServer);
            }
            System.out.println("Server has disconnected.");
        } catch (IOException e) {
            System.out.println("Server disconnected: " + e.getMessage());
        }
    }

    public void processInput(String inputLine) {
        if (inputLine.startsWith("GameInformation")) {
            ArrayList<Integer> gameInformations = parseGameInformation(inputLine);
            multiplayerGame.setMpGameInformation(gameInformations);
        }
        // Check for "Spell" identifier
        else if (inputLine.startsWith("Spell")) {
            parseSpell(inputLine);
        } else {
            System.out.println("Unrecognized identifier");
        }
    }

    private static ArrayList<Integer> parseGameInformation(String input) {
        Pattern pattern = Pattern.compile("score: (\\d+), barrierCount: (\\d+), chance: (\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            ArrayList<Integer> gameInformations =  new ArrayList<>();
            int score = Integer.parseInt(matcher.group(1));
            int barrierCount = Integer.parseInt(matcher.group(2));
            int chance = Integer.parseInt(matcher.group(3));

            gameInformations.add(score);
            gameInformations.add(barrierCount);
            gameInformations.add(chance);
            return gameInformations;
        } else {
            System.out.println("No game information found!");
            return null;
        }
    }

    private void parseSpell(String input) {
        Pattern pattern = Pattern.compile("spellType: ([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String spellType = matcher.group(1);
            System.out.println("Spell Information:");
            System.out.println("SpellType: " + spellType);
            getAffectedByMultiplayerSpell(spellType);
        } else {
            System.out.println("No spell information found!");
        }
    }

    private void getAffectedByMultiplayerSpell(String spellType){
        if(Objects.equals(spellType, "iv")){
            this.multiplayerGame.getYmir().activateInfiniteVoid();
            System.out.println("Infinite void activated due to the spell from opponent user.");
        }
        else if(Objects.equals(spellType, "hp")){
            this.multiplayerGame.getYmir().activateHollowPurple();
            System.out.println("Hollow purple activated due to the spell from opponent user.");
        }
        else if(Objects.equals(spellType, "da")){
            this.multiplayerGame.getYmir().activateDoubleAccel();
            System.out.println("Double accel activated due to the spell from opponent user.");
        }
    }

    private void sendToServer() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String message = scanner.nextLine();
                output.println(message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Client initiating shutdown...");
                    break;
                }
            }
        } finally {
            scanner.close();
        }
    }


    public void stop() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (outputSocket != null) {
                outputSocket.close();
            }
            if (inputSocket != null) {
                inputSocket.close();
            }
            System.out.println("Closed all resources.");
        } catch (IOException e) {
            System.out.println("Error when closing client resources: " + e.getMessage());
        }
    }
    @Override
    public void sendSpell(String spell) {
        boolean hasSpell = false;
        if(Objects.equals(spell, "Spell: {spellType: iv}") && this.multiplayerGame.getInventory().checkSpellCount(SpellType.INFINITE_VOID)){
            hasSpell = true;
            this.multiplayerGame.getInventory().updateInventory(SpellType.INFINITE_VOID, -1);
            System.out.println("Infinite void activated due to the spell from opponent user.");
        }
        if(Objects.equals(spell, "Spell: {spellType: hp}") && this.multiplayerGame.getInventory().checkSpellCount(SpellType.HOLLOW_PURPLE)){
            hasSpell = true;
            this.multiplayerGame.getInventory().updateInventory(SpellType.HOLLOW_PURPLE, -1);
            System.out.println("Hollow purple activated due to the spell from opponent user.");
        }
        if(Objects.equals(spell, "Spell: {spellType: da}") && this.multiplayerGame.getInventory().checkSpellCount(SpellType.DOUBLE_ACCEL)){
            hasSpell = true;
            this.multiplayerGame.getInventory().updateInventory(SpellType.DOUBLE_ACCEL, -1);
            System.out.println("Double accel activated due to the spell from opponent user.");
        }
        if(hasSpell) output.println(spell);
    }

    public static void main(String[] args) {
        MultiPortClient client = new MultiPortClient("localhost", 59326, 59325);
        client.start();
    }

}
