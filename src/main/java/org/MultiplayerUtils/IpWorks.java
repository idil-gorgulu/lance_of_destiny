package org.MultiplayerUtils;

import java.net.*;
import java.io.*;
import java.util.*;
import java.net.InetAddress;

public class IpWorks {

    public static void main(String[] args) throws Exception {
        // https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java

        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("System IP Address : " +
                (localhost.getHostAddress()).trim());

        ArrayList<String> systemIPAddresses = new ArrayList<>();

        ArrayList<String> systemIpCheckers = new ArrayList<>();


        // I added them in the confidence order
        systemIpCheckers.add("http://checkip.amazonaws.com/");
        systemIpCheckers.add("https://ipv4.icanhazip.com/");
        systemIpCheckers.add("http://myexternalip.com/raw");
        systemIpCheckers.add("http://ipecho.net/plain");
        systemIpCheckers.add("http://www.trackip.net/ip");

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

        System.out.println("Public IP Address: " + systemIPAddress);
    }
}
