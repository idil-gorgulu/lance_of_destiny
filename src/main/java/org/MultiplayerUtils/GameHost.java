package org.MultiplayerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;


public class GameHost {

    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader inputStreamFromServer;
    private PrintWriter outputStreamToServer;

    public GameHost(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverAddress, serverPort);
            socket.setSoTimeout(10000); // Set timeout as needed
            inputStreamFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStreamToServer = new PrintWriter(socket.getOutputStream(), true);
            handleServerCommunication();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    private void handleServerCommunication() {
        try {
            // Communication loop
            String fromServer;
            while ((fromServer = inputStreamFromServer.readLine()) != null) {
                String response = fromServer;
                processServerResponse(response);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Connection timed out: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void processServerResponse(String response) {
        System.out.println("Received from server: " + response);
        // Implement specific response handling based on server protocol
    }

    private void closeResources() {
        try {
            if (outputStreamToServer != null) {
                outputStreamToServer.close();
            }
            if (inputStreamFromServer != null) {
                inputStreamFromServer.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter server IP:");
        String serverIP = scanner.nextLine();
        System.out.println("Enter server port:");
        int port = scanner.nextInt();
        scanner.close();

        new GameHost(serverIP, port);
    }
}
