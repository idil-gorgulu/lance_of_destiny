package org.system;

import org.ata_swing_ui.BuildingModePage;
import org.oguz_swing_ui.LoginPage;
import org.oguz_swing_ui.Page;
import org.oguz_swing_ui.SignupPage;
import org.oguz_swing_ui.StartPage;

import javax.swing.*;

public class Navigator {
    private static Navigator instance;
    private JFrame mainFrame;

    private Navigator() {
        mainFrame = new JFrame("Lance of Destiny");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
    }

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    public void showPage(Page page) {
        mainFrame.setContentPane(page);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showStartPage() {
        showPage(new StartPage());
    }

    public void showLoginPage() {
        showPage(new LoginPage());
    }

    public void showSignupPage() {
        showPage(new SignupPage());
    }

    public void showBuildingPage() { showPage(new BuildingModePage()); }

    public void show() {
        mainFrame.setVisible(true);
    }
}
