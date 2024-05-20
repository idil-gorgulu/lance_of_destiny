package org.Domain;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.Utils.Database;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class User {
    private String email;
    private ArrayList<Document> games;

    private String multiplayerGameName;
    String myLocalIP;
    String myPublicIP;
    private static User userInstance;

    public User() {
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

    public static User getUserInstance() {
        if (userInstance == null) {
            userInstance = new User();
            return userInstance;
        } else {
            return userInstance;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Document> getGames() {
        return games;
    }

    public void setGames(ArrayList<Document> games) {
        this.games = games;
    }

    public ArrayList<Document> getAllGames() {
        ArrayList<Document> games = new ArrayList<>();
        Database.getInstance().getGameCollection();
        Document gameQuery = new Document();
        gameQuery.put("email", email);
        MongoCollection<Document> collection  = Database.getInstance().getGameCollection();
        FindIterable<Document> cursor = collection.find(gameQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                //System.out.println(cursorIterator.next());
                games.add(cursorIterator.next());
            }
        }
        this.setGames(games);
        return games;
    }

    public void sendMultiplayerGameInfo(String gameName) {
        Document multiplayerGameSession = new Document();
        multiplayerGameSession.put("gameName", gameName);
        multiplayerGameSession.put("localIP", this.myLocalIP);
        multiplayerGameSession.put("publicIP", this.myPublicIP);
        // Add the port in here
        multiplayerGameSession.put("joined", "False");
        Database.getInstance().getMultiplayerGameCollection().insertOne(multiplayerGameSession);
        System.out.println("Saved");
    }


    public String getMyLocalIP() {
        return myLocalIP;
    }

    public void setMyLocalIP(String myLocalIP) {
        this.myLocalIP = myLocalIP;
    }

    public String getMyPublicIP() {
        return myPublicIP;
    }

    public void setMyPublicIP(String myPublicIP) {
        this.myPublicIP = myPublicIP;
    }

    public String getMultiplayerGameName() {
        return multiplayerGameName;
    }

    public void setMultiplayerGameName(String multiplayerGameName) {
        this.multiplayerGameName = multiplayerGameName;
    }
}
