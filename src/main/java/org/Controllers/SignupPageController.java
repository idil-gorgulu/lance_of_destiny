package org.Controllers;

import org.Utils.Database;
import org.Views.Navigator;
import org.bson.Document;

import java.lang.annotation.Documented;

public class SignupPageController {
    private static SignupPageController instance;

    public static SignupPageController getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new SignupPageController();
            return instance;
        }
    }

    public void createUser(String email, String username, String password) {
        Document user = new Document();
        user.put("email", email);
        user.put("username", username);
        user.put("password", password);
        Database.getInstance().getUserCollection().insertOne(user);
        Navigator.getInstance().showStartPage(); // Added this for now until we change the page structure
    }

}
