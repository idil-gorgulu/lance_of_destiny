package org.Utils;

import org.bson.Document;

public class DatabaseOperations {
    // This class is for single Database operations, such as deleting all the instances, etc.
    // DO NOT USE THIS CLASS TO IMPLEMENT ANY GAME FUNCTION
    Database db = Database.getInstance();

    public void deleteAllInstances() {
        db.getMultiplayerGameCollection().deleteMany(new Document());
    }

    public static void main(String[] args) {
        // Do the operation you want to do on DB
        DatabaseOperations dbOperations = new DatabaseOperations();
        dbOperations.deleteAllInstances();
    }
}
