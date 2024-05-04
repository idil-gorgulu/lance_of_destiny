package org.Controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.Domain.User;
import org.Utils.Database;
import org.Views.Navigator;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

import javax.swing.*;

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
        boolean isAuthorized = false;
        Document userQuery = new Document();
        userQuery.put("email", email);
        userQuery.put("password", password);
        MongoCollection<Document> collection  = Database.getInstance().getUserCollection();

        FindIterable<Document> cursor = collection.find(userQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            if (cursorIterator.hasNext()) {
                isAuthorized = true;
            }
        }

        if (isAuthorized) {
            System.out.println("Login successful!");
            User user = User.getUserInstance();
            user.setEmail(email);
            JOptionPane.showMessageDialog(null, "You are now logged in!");
            Navigator.getInstance().showGameModePage();
        } else {
            System.out.println("Login failed!");
            JOptionPane.showMessageDialog(null, "Invalid email or password!");
        }
    }
}
