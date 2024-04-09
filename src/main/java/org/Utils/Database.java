package org.Utils;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    private static Database instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Connection settings

    private static final String CONNECTION_STRING = "mongodb+srv://comp302:comp302lanceofdestiny@comp302.gwbpr53.mongodb.net/?retryWrites=true&w=majority&appName=comp302";

    private static final String DATABASE_NAME = "test";
    // Private constructor to prevent instantiation
    private Database() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(DATABASE_NAME);

    }

    // Public method to get the singleton instance
    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Method to get the database instance
    public MongoDatabase getDatabase() {
        return database;
    }

    // Optional: A method to test the connection
    public void testConnection() {
        try {
            database.runCommand(new Document("ping", 1));
            System.out.println("Connection successful!");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    // Main method to demonstrate the usage
    public static void main(String[] args) {
        Database dbSingleton = Database.getInstance();
        dbSingleton.testConnection(); // Test the connection
    }
}
