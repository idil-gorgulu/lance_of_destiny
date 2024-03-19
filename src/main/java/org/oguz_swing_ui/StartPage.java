package org.oguz_swing_ui;
import org.system.Navigator;

import javax.swing.*;

public class StartPage extends Page {
    public StartPage() {
        super();
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel gameTitle = new JLabel("Lance of Destiny");
        gameTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(gameTitle);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> Navigator.getInstance().showLoginPage());

        JButton signupButton = new JButton("Signup");
        signupButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> Navigator.getInstance().showSignupPage());

        add(loginButton);
        add(signupButton);
    }
}
