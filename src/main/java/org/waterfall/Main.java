package org.waterfall;
import org.Utils.Database;
import org.Views.Navigator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Database initialized in here
        Database dbSingleton = Database.getInstance();
        SwingUtilities.invokeLater(() -> {
            Navigator navigator = Navigator.getInstance();
            navigator.showStartPage();
            navigator.show();
        });
    }

}