package org.Domain;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.Utils.Database;
import org.bson.Document;

import java.util.ArrayList;

public class User {
    private String email;
    private ArrayList<Document> games;

    private static User userInstance;


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
}
