package org.Views;

import javax.swing.*;
import java.util.Stack;

public class Navigator {
    private static Navigator instance;
    private JFrame mainFrame;
    private Stack<Page> pageStack;
    private Navigator() {
        mainFrame = new JFrame("Lance of Destiny");
        mainFrame.setSize(1200, 650);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        pageStack = new Stack<>();

    }

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    public void showPage(Page page) {
        if (pageStack.isEmpty() || !pageStack.peek().getClass().equals(page.getClass())) {
            pageStack.push(page);
        }
        mainFrame.setContentPane(page);
        mainFrame.revalidate();
        mainFrame.repaint();
    }


    public void getPrevious() {
        if (pageStack.size() > 1) {
            pageStack.pop();
            Page previousPage = pageStack.peek();
            showPage(previousPage);
        } else {
            System.out.println("No previous page!");
        }
    }
    public void showEnterPage() {showPage(new EnterPage());}
    public void showStartSingleplayerPage() {
        showPage(new StartSingleplayerPage());
    }
    public void showStartMultiplayerPage() {
        showPage(new StartMultiplayerPage());
    }
    public void showLoginPage() {
        showPage(new LoginPage());
    }
    public void showSignupPage() {
        showPage(new SignupPage());
    }
    public void showBuildingModePage() { showPage(new BuildingModePage()); }
    public void showMultiplayerBuildingModePage() { showPage(new BuildingModePage(true)); }

    public void showRunningModePage() { showPage(new RunningModePage()); }
    public void showGameSelectionPage() {showPage(new GameSelectionPage()); }
    public void showGameModePage() {showPage(new GameModePage()); }
    public void showJoinMultiplayerGamePage() {showPage(new JoinMultiplayerGamePage()); }
    public void showWaitMultiplayerGameAcceptPage() {showPage(new WaitMultiplayerGameAccept());}

    public void show() {
        mainFrame.setVisible(true);
    }


}
