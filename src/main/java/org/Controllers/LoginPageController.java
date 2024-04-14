package org.Controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.Utils.Database;
import org.Views.Navigator;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class LoginPageController {
    private static LoginPageController instance;

    public static LoginPageController getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new LoginPageController();
            return instance;
        }
    }

    public void authorizeUser(String email, String password) {
        Document userQuery = new Document();
        userQuery.put("email", email);
        userQuery.put("password", password);
        MongoCollection<Document> collection  = Database.getInstance().getUserCollection();

        FindIterable<Document> cursor = collection.find(userQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                System.out.println(cursorIterator.next());
                System.out.println("User is authorized");
                Navigator.getInstance().showStartPage();
            }
        }
        Navigator.getInstance().showStartPage(); // Added this for now until we change the page structure
    }
}
