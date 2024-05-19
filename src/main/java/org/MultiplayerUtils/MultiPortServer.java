package org.MultiplayerUtils;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiPortServer {
    private ServerSocket serverSocketOut;
    private Socket outputSocket;
    private PrintWriter output;

    private ServerSocket serverSocketIn;
    private Socket inputSocket;
    private BufferedReader input;

    public void start(int outputPort, int inputPort) {
        try {
            // This is for sending messages to client
            serverSocketOut = new ServerSocket(outputPort);
            System.out.println("Server waiting for client on port " + outputPort + " for outgoing messages...");
            outputSocket = serverSocketOut.accept();
            System.out.println("Client connected on port " + outputPort + " for outgoing messages.");
            output = new PrintWriter(outputSocket.getOutputStream(), true);

            // This is for receiving messages from client
            serverSocketIn = new ServerSocket(inputPort);
            System.out.println("Server waiting for client on port " + inputPort + " for incoming messages...");
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
        server.start(20004, 20005);
    }
}
