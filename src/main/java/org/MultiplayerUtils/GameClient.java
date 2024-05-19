package org.MultiplayerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);

            Thread inputHandler = new Thread(this::receiveFromServer);
            inputHandler.start();

            sendToServer();
        } catch (IOException e) {
            System.out.println("Unable to connect to server: " + e.getMessage());
        }
    }

    private void receiveFromServer() {
        try {
            String fromServer;
            while ((fromServer = inputStream.readLine()) != null) {
                System.out.println("Server says: " + fromServer);
            }
            System.out.println("Server has disconnected.");
        } catch (IOException e) {
            System.out.println("Server disconnected: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void sendToServer() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    outputStream.println(userInput);
                    break;
                }
                outputStream.println(userInput);
            }
        } finally {
            scanner.close();
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error when closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Connecting to server at localhost:10000");
        new GameClient("localhost", 10000); // Use localhost for local testing
    }
}
