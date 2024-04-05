package org.Views;

import javax.swing.*;

public class Navigator {
    private static Navigator instance;
    private JFrame mainFrame;

    private Navigator() {
        mainFrame = new JFrame("Lance of Destiny");
        mainFrame.setSize(1400, 700);
        mainFrame.setResizable(false);
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
    public void showBuildingModePage() { showPage(new BuildingModePage()); }
    public void showRunningModePage() { showPage(new RunningModePage()); }

    public void show() {
        mainFrame.setVisible(true);
    }
}
