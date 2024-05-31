package org.MultiplayerUtils;

import org.Domain.Game;
import org.Domain.User;
import org.Utils.Database;
import org.bson.Document;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiPortServer {
    public static MultiPortServer instance;
    private ServerSocket serverSocketOut;
    private Socket outputSocket;
    public PrintWriter output;

    private ServerSocket serverSocketIn;
    private Socket inputSocket;
    public BufferedReader input;
    public boolean connected = false;
    public volatile boolean selfReadyClicked = false;
    public boolean opponentReadyClicked = false;
    public boolean startCountdown = false;
    public boolean gameStarted = false;
    private List<ConnectedStateChangeListener> listeners = new ArrayList<>();
    private List<CountdownStateChangeListener> countdownListeners = new ArrayList<>();

    public Game getMultiplayerGame() {
        return multiplayerGame;
    }

    public void setMultiplayerGame(Game multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
    }

    private Game multiplayerGame;


    public static MultiPortServer  getInstance() {
        if (instance == null) {
            instance = new MultiPortServer();
            return instance;
        } else {
            return instance;
        }
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
        notifyAllListeners();
    }

    public void addStateChangeListener(ConnectedStateChangeListener listener) {
        listeners.add(listener);
    }

    public void addCountdownStateChangeListener(CountdownStateChangeListener listener) {
        countdownListeners.add(listener);
    }

    private void notifyAllListeners() {
        for (ConnectedStateChangeListener listener : listeners) {
            listener.onConnectedStateChange();
        }
    }

    private void notifyCountdownListeners() {
        for (CountdownStateChangeListener listener : countdownListeners) {
            listener.onCountdownStateChange();
        }
    }

    public void sendMultiplayerGameInfo(String outPort, String inPort) {
        Document multiplayerGameSession = new Document();
        multiplayerGameSession.put("gameName", User.getUserInstance().getMultiplayerGameName());
        multiplayerGameSession.put("localIP", User.getUserInstance().getMyLocalIP());
        multiplayerGameSession.put("publicIP", User.getUserInstance().getMyPublicIP());
        multiplayerGameSession.put("outPort", outPort);
        multiplayerGameSession.put("inPort", inPort);
        multiplayerGameSession.put("joined", "False");
        Database.getInstance().getMultiplayerGameCollection().insertOne(multiplayerGameSession);
        System.out.println("Saved");
    }

    public void start() {
        try {
            // port set to 0 for finding the available port
            // This is for sending messages to client
            serverSocketOut = new ServerSocket(0);
            int outputPort = serverSocketOut.getLocalPort();
            System.out.println("Server waiting for client on port " + outputPort + " for outgoing messages...");

            // This is for receiving messages from client
            serverSocketIn = new ServerSocket(0);
            int inputPort = serverSocketIn.getLocalPort();
            System.out.println("Server waiting for client on port " + inputPort + " for incoming messages...");

            // Send here the game and wait for the connection
            sendMultiplayerGameInfo(Integer.toString(outputPort), Integer.toString(inputPort));

            outputSocket = serverSocketOut.accept();
            System.out.println("Client connected on port " + outputPort + " for outgoing messages.");
            output = new PrintWriter(outputSocket.getOutputStream(), true);

            inputSocket = serverSocketIn.accept();
            System.out.println("Client connected on port " + inputPort + " for incoming messages.");
            input = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
            output.println("Hello from server");
            // Put ready button when
            // Then when connected first print out 3 2 1
            this.setConnected(true);
            // Make this a function
            while (!selfReadyClicked) {
                Thread.onSpinWait();
                // This will wait
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

            startCountdown = true;
            notifyCountdownListeners();
            // This has listener
            while (!gameStarted) {
                Thread.onSpinWait();
                // It will wait until it is
            }
            // Assure that the game is loaded
            Runnable sendStatisticsRunnable = new Runnable() {
                //
                public void run() {
                    int score = multiplayerGame.getScore().getTotalScore();
                    int barrierCount = multiplayerGame.getBarriers().size();
                    int chance = multiplayerGame.getChance().getRemainingChance();
                    output.println(String.format("GameInformation: {score: %d, barrierCount: %d, chance: %d}", score, barrierCount, chance));
                }
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(sendStatisticsRunnable, 0, 1, TimeUnit.SECONDS);

            Thread sendThread = new Thread(this::handleSending);
            sendThread.start();

            while ((inputLine = input.readLine()) != null) {
                System.out.println("Incoming Message: " + inputLine);
                processInput(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllResources();
        }
    }

    public void processInput(String inputLine) {
        if (inputLine.startsWith("GameInformation")) {
            ArrayList<Integer> gameInformations = parseGameInformation(inputLine);
            System.out.println(gameInformations);
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

    private static void parseSpell(String input) {
        Pattern pattern = Pattern.compile("spellType: (\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            int spellType = Integer.parseInt(matcher.group(1));
            System.out.println("Spell Information:");
            System.out.println("SpellType: " + spellType);
            // TODO: Implement the listener for action the game
        } else {
            System.out.println("No spell information found!");
        }
    }
    private void handleSending() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String message = scanner.nextLine();
                output.println(message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Server initiating shutdown...");
                    break;
                }
            }
        } finally {
            scanner.close();
        }
    }

    public void closeAllResources() {
        try {
            if (input != null) {
                input.close();
            };
            if (output != null) {
                output.close();
            }
            if (outputSocket != null) {
                outputSocket.close();
            }
            if (serverSocketOut != null) {
                serverSocketOut.close();
            }
            if (inputSocket != null) {
                inputSocket.close();
            }
            if (serverSocketIn != null) {
                serverSocketIn.close();
            }
            System.out.println("Closed all resources.");
        } catch (IOException e) {
            System.out.println("Error closing server resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MultiPortServer server = new MultiPortServer();
        server.start();
    }
}
