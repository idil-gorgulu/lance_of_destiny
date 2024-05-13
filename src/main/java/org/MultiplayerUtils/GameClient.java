package org.MultiplayerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class GameClient {

    private String serverAdress;
    private Socket socket;
    private int serverPort;

    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public GameClient(String serverAdress, int serverPort) {
        this.serverAdress = serverAdress;
        this.serverPort = serverPort;
        this.connectToServer();
    }
    public void connectToServer() {
        try {
            this.socket = new Socket(this.serverAdress, this.serverPort);
            this.socket.setSoTimeout(10000);
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startExchange() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (!this.socket.isClosed()) {
                String message = scanner.nextLine();
                this.processRequest(message);
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing the socket: " + ex.getMessage());
            }
        }
        System.out.println("Client has disconnected from the server.");
    }

    public void processRequest(String message) throws IOException {
        String response = this.sendRequest(message);
        this.proccesResponse(response);
    }
    public String sendRequest(String message) throws IOException {
        outputStream.println(message);
        String response = new String(inputStream.readLine());
        return response;
    }

    public void proccesResponse(String response) {
        System.out.println(response);
    }




    public static void main(String[] args) {
        GameClient client = new GameClient("0.0.0.0", 10000);
        client.startExchange();
    }


}
