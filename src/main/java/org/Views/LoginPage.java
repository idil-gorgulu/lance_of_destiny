package org.Views;
import org.Controllers.LoginPageController;
import org.Domain.User;
import org.Utils.ComponentStyling;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginPage extends Page {

    private BufferedImage backgroundImage;

    public LoginPage() {
        super();
        this.setOpaque(false);
        try {
            backgroundImage = ImageIO.read(new File("assets/bckg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initUI();
    }

    protected void initUI() {
        setLayout(new BorderLayout());
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setOpaque(false);
        add(backgroundPanel, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());
        backButtonPanel.add(backButton);
        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);
        ComponentStyling.customizeButtonback(backButton);
        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        centerPanel.setOpaque(false);
        centerPanel.setFocusable(true);
        centerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                centerPanel.requestFocusInWindow();
            }
        });

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JTextField emailTextField = ComponentStyling.setupTextField("email");
        formPanel.add(emailTextField);

        JPasswordField passwordField = ComponentStyling.setupPasswordField("password");
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        ComponentStyling.customizeButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                char[] password = passwordField.getPassword();
                LoginPageController.getInstance().authorizeUser(email, String.copyValueOf(password));
            }
        });

        formPanel.add(loginButton);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, loginButton.getPreferredSize().height));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(formPanel, gbc);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}