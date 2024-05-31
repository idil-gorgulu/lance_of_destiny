package org.MultiplayerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameHost {

    private int serverPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inputStreamFromClient;
    private PrintWriter outputStreamToClient;

    public GameHost(int serverPort) {
        this.serverPort = serverPort;
        waitForConnection();
    }

    private void waitForConnection() {
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Waiting for a client to connect...");
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

            inputStreamFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStreamToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            Thread inputHandler = new Thread(this::handleClientInput);
            inputHandler.start();

            handleServerOutput();
        } catch (IOException e) {
            System.out.println("Error setting up the server: " + e.getMessage());
        }
    }

    private void handleClientInput() {
        try {
            String fromClient = inputStreamFromClient.readLine();
            while (fromClient != "exit") {
                while (fromClient != null) {
                    System.out.println("Client says: " + fromClient);
                }
                fromClient = inputStreamFromClient.readLine();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void handleServerOutput() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String serverMessage = null;
                if (scanner.hasNextLine()) {
                    serverMessage = scanner.nextLine();
                }
                if (serverMessage == null || serverMessage.equalsIgnoreCase("exit")) {
                    break;
                }
                outputStreamToClient.println(serverMessage);
            }
        } catch (Exception e) {
            System.out.println("Error reading from console: " + e.getMessage());
        } finally {
            scanner.close();
            closeResources();
        }
    }


    private void closeResources() {
        try {
            if (outputStreamToClient != null) {
                outputStreamToClient.close();
            }
            if (inputStreamFromClient != null) {
                inputStreamFromClient.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter the port number for the server:");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        scanner.close();
        new GameHost(port);
    }
}
