package org.MultiplayerUtils;

import org.Domain.User;
import org.Utils.Database;
import org.bson.Document;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiPortServer {
    public static MultiPortServer instance;
    private ServerSocket serverSocketOut;
    private Socket outputSocket;
    private PrintWriter output;

    private ServerSocket serverSocketIn;
    private Socket inputSocket;
    private BufferedReader input;

    public static MultiPortServer  getInstance() {
        if (instance == null) {
            instance = new MultiPortServer();
            return instance;
        } else {
            return instance;
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

            Thread sendThread = new Thread(this::handleSending);
            sendThread.start();

            String inputLine;
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
