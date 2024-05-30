package org.MultiplayerUtils;

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
    private List<ConnectedStateChangeListener> listeners = new ArrayList<>();



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

    private void notifyAllListeners() {
        for (ConnectedStateChangeListener listener : listeners) {
            listener.onConnectedStateChange();
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

            // Assure that the game is loaded
            Runnable sendStatisticsRunnable = new Runnable() {
                public void run() {
                    output.println("sending statistics");
                }
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(sendStatisticsRunnable, 0, 1, TimeUnit.SECONDS);

            Thread sendThread = new Thread(this::handleSending);
            sendThread.start();

            while ((inputLine = input.readLine()) != null) {
                System.out.println("Incoming Message: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllResources();
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
