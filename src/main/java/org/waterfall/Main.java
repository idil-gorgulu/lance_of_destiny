package org.waterfall;
import org.Views.Navigator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Navigator navigator = Navigator.getInstance();
            navigator.showStartPage();
            navigator.show();
        });
    }

}