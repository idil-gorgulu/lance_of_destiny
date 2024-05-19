package org.MultiplayerUtils;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiPortClient {
    private Socket outputSocket;
    private PrintWriter output;

    private Socket inputSocket;
    private BufferedReader input;

    public void start(String gameHostIpAdress, int outputPort, int inputPort) {
        try {
            // This is for receiving messages from the gameHost
            inputSocket = new Socket(gameHostIpAdress, inputPort);
            System.out.println("Connected to gameHost at " + gameHostIpAdress + ":" + inputPort + " for receiving messages at port " + inputSocket.getLocalPort() + ".");
            input = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));

            // This is for sending messages to the gameHost
            outputSocket = new Socket(gameHostIpAdress, outputPort);
            System.out.println("Connected to server at " + gameHostIpAdress + ":" + outputPort + " for sending messages at port " + outputSocket.getLocalPort() + ".");
            output = new PrintWriter(outputSocket.getOutputStream(), true);


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
        MultiPortClient client = new MultiPortClient();
        client.start("localhost", 56054, 56053);
    }
}
