package org.MultiplayerUtils;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiPortClient {

    private Socket outputSocket;
    private PrintWriter output;
    private Socket inputSocket;
    private BufferedReader input;
    private String gameHostIpAdress;
    private int outputPort;
    private int inputPort;
    public boolean connected = false;
    public boolean selfReadyClicked = false;
    public boolean opponentReadyClicked = false;

    public MultiPortClient(String gameHostIpAdress, int outputPort, int inputPort) {
        this.gameHostIpAdress = gameHostIpAdress;
        this.outputPort = outputPort;
        this.inputPort = inputPort;
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

            while(!selfReadyClicked){}
            // Make this a function
            output.println("gameReady");
            // Wait other player to click as well
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                if (inputLine.equals("gameReady")) {
                    opponentReadyClicked = true;
                    break;
                }
            }

            // Assure that the game is loaded
            Runnable sendStatisticsRunnable = new Runnable() {
                public void run() {
                    output.println("sending statistics");
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
            }
            System.out.println("Server has disconnected.");
        } catch (IOException e) {
            System.out.println("Server disconnected: " + e.getMessage());
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

    public static void main(String[] args) {
        MultiPortClient client = new MultiPortClient("localhost", 59326, 59325);
        client.start();
    }
}
