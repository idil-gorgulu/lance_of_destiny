package org.Views;
import javax.swing.*;

public class LoginPage extends Page {

    public LoginPage() {
        // Merhaba atakan
        super(); // Call the constructor of the abstract Page class
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Email Label and Text Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JTextField emailTextField = new JTextField(15);
        emailTextField.setMaximumSize(emailTextField.getPreferredSize());
        emailTextField.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(emailLabel);
        add(emailTextField);

        // Password Label and Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(passwordLabel);
        add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(loginButton);
    }

    //TODO Kagan: implement authorize user.
    private boolean authorizeUser() {
        return true;
    }
}
