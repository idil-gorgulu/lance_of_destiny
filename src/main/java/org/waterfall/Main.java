package org.waterfall;
import org.Domain.User;
import org.Utils.Database;
import org.Views.Navigator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*
        // COMMENTED OUT FOR ONLY 1 USER PURPOSES
        // Database initialized in here
        Database dbSingleton = Database.getInstance();
        User user = User.getUserInstance();
        SwingUtilities.invokeLater(() -> {
            Navigator navigator = Navigator.getInstance();
            navigator.showEnterPage();
            navigator.show();
/        });
        */

        Database dbSingleton = Database.getInstance();
        User user = User.getUserInstance();
        user.setEmail("deneme");
        SwingUtilities.invokeLater(() -> {
            Navigator navigator = Navigator.getInstance();
            navigator.showGameModePage();
            navigator.show();
        });

    }
}