package org.waterfall;
import org.Domain.User;
import org.Utils.Database;
import org.Views.Navigator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database dbSingleton = Database.getInstance();
        User user = User.getUserInstance();
        SwingUtilities.invokeLater(() -> {
            Navigator navigator = Navigator.getInstance();
            navigator.showEnterPage();
            navigator.show();
        });

    }
}