package org.Domain;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.Utils.Database;
import org.bson.Document;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiPlayerGame {
    String myLocalIP;
    String myPublicIP;
    String gameName;

    public MultiPlayerGame() {
        ArrayList<String> ips = getIP();
        this.myLocalIP = ips.get(0);
        this.myPublicIP = ips.get(1);
    }

    private ArrayList<String> getIP()  {
        // https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (Exception e) {

        }
        //System.out.println("System IP Address : " + (localhost.getHostAddress()).trim());

        ArrayList<String> systemIPAddresses = new ArrayList<>();

        ArrayList<String> systemIpCheckers = new ArrayList<>();

        // I added them in the confidence order
        systemIpCheckers.add("http://checkip.amazonaws.com/");
        /*
        systemIpCheckers.add("https://ipv4.icanhazip.com/");
        systemIpCheckers.add("http://myexternalip.com/raw");
        systemIpCheckers.add("http://ipecho.net/plain");
        systemIpCheckers.add("http://www.trackip.net/ip");
         */
        for (String systemIpChecker : systemIpCheckers ) {
            try
            {
                URL url_name = new URL(systemIpChecker);

                BufferedReader sc =
                        new BufferedReader(new InputStreamReader(url_name.openStream()));
                String newSystemIPAdress = sc.readLine().trim();
                // I check wheter they give the same result, if not I add them
                if (systemIPAddresses.isEmpty()) {
                    systemIPAddresses.add(newSystemIPAdress);
                }

                if (systemIPAddresses.get(0).equals(newSystemIPAdress)) {
                    systemIPAddresses.add(newSystemIPAdress);
                }
            }
            catch (Exception e)
            {
            }
        }
        // Checking every site is more safe, but it is very slow, change this later
        String systemIPAddress = "Not Available";
        if (!systemIPAddresses.isEmpty()) {
            systemIPAddress = systemIPAddresses.get(0);
        }

        // System.out.println("Public IP Address: " + systemIPAddress);
        // Make this dict for better understanding
        ArrayList<String> ips = new ArrayList<>();
        ips.add(localhost.getHostAddress());
        ips.add(systemIPAddress);
        return ips;
    }

    public void sendMultiplayerGameInfo() {
        Document multiplayerGameSession = new Document();
        multiplayerGameSession.put("gameName", this.gameName);
        multiplayerGameSession.put("localIP", this.myLocalIP);
        multiplayerGameSession.put("publicIP", this.myPublicIP);
        multiplayerGameSession.put("joined", "False");
        Database.getInstance().getMultiplayerGameCollection().insertOne(multiplayerGameSession);
        System.out.println("Saved");
    }

    public ArrayList<Document> getAllAvailableGames() throws Exception {
        ArrayList<Document> mpgames = new ArrayList<>();
        MongoCollection<Document> multiplayerGamesCollection = Database.getInstance().getMultiplayerGameCollection();
        FindIterable<Document> multiplayerGames = multiplayerGamesCollection.find();
        System.out.println("Game informations are retriving");
        byte[] subnetMask = InetAddress.getByName("255.255.0.0").getAddress();

        for (Document multiplayerGame : multiplayerGames) {
            String joined = multiplayerGame.getString("joined");
            if (joined.equals("False")) {
                String gamePublicIP = multiplayerGame.getString("publicIP");
                if (ipMatchesSubnet(this.myPublicIP, gamePublicIP, subnetMask)) {
                    mpgames.add(multiplayerGame);
                }
            }
        }
        return mpgames;
    }

    private static boolean ipMatchesSubnet(String myPublicIP, String gamePublicIP, byte[] subnetMask) {
        try {
            byte[] myAddress = InetAddress.getByName(myPublicIP).getAddress();
            byte[] gameAddress = InetAddress.getByName(gamePublicIP).getAddress();

            // Debugging output: print byte values of addresses
            System.out.println("Game Address Bytes:");
            for (byte b : gameAddress) {
                System.out.printf("%d ", b);
            }
            System.out.println("\nMy Address Bytes:");
            for (byte b : myAddress) {
                System.out.printf("%d ", b);
            }

            // Applying subnet mask and checking if both IPs are in the same subnet
            for (int i = 0; i < subnetMask.length; i++) {
                if ((myAddress[i] & subnetMask[i]) != (gameAddress[i] & subnetMask[i])) {
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            // It's not clear which IP could be invalid, so better error handling might be necessary
            System.out.println("Invalid IP address provided: " + e.getMessage());
            return false;
        }
    }


    public static void main(String[] args) throws Exception {
        MultiPlayerGame mpgame = new MultiPlayerGame();
        // mpgame.sendMultiplayerGameInfo();
        mpgame.getAllAvailableGames();
    }

}
