package org.Views;
import org.Controllers.LoginPageController;
import org.Domain.User;

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
        this.setOpaque(false); // Ana panel için opaklık ayarı
        loadBackgroundImage();
        initUI();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("assets/bckg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        backButtonPanel.setOpaque(false); // Make the panel transparent
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> Navigator.getInstance().getPrevious());

        backButtonPanel.add(backButton);

        backgroundPanel.add(backButtonPanel, BorderLayout.NORTH);

        customizeButtonback(backButton);
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

        // Panel for the rest of the login form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Setup for email text field
        JTextField emailTextField = setupTextField("email");
        formPanel.add(emailTextField);

        // Setup for password field
        JPasswordField passwordField = setupPasswordField("password");
        formPanel.add(passwordField);

        // Login Button setup
        JButton loginButton = new JButton("Login");
        customizeButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kullanıcı bilgilerini al
                String email = emailTextField.getText();
                char[] password = passwordField.getPassword();

                // Kullanıcıyı doğrula
                boolean isAuthorized = LoginPageController.getInstance().authorizeUser(email, String.copyValueOf(password));

                // Doğrulama sonrası işlemler
                if (isAuthorized) {
                    System.out.println("Login successful!"); // Konsola loglama
                    User user = User.getUserInstance();
                    System.out.println(user);
                    user.setEmail(email);
                    System.out.println(User.getUserInstance().getEmail());
                    JOptionPane.showMessageDialog(null, "You are now logged in!"); // Kullanıcıya bilgi mesajı göster
                    Navigator.getInstance().showStartPage();
                    // Başka bir işlem veya ekran açabilirsiniz
                    // Örneğin ana ekranı açabilirsiniz:
                    // mainFrame.setVisible(true);
                } else {
                    System.out.println("Login failed!"); // Konsola loglama
                    JOptionPane.showMessageDialog(null, "Invalid email or password!"); // Kullanıcıya hata mesajı göster
                }
            }
        });

        formPanel.add(loginButton);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, loginButton.getPreferredSize().height));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.VERTICAL;

        // Additional padding to ensure the formPanel is visually centered
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(formPanel, gbc);

        // Adding the centered panel to the center of the border layout
        add(centerPanel, BorderLayout.CENTER);
    }

    private JTextField setupTextField(String placeholder) {
        JTextField textField = new JTextField("", 15) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !(isFocusOwner())) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 5, 20); // position the text
                    g2.dispose();
                }
            }
        };
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().isEmpty() || textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        textField.setForeground(Color.GRAY);
        textField.setFont(new Font("Tahoma", Font.PLAIN, 18)); // Increased font size
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        return textField;
    }


    private JPasswordField setupPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(placeholder, 15) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 5, 20); // position the text
                    g2.dispose();
                }
            }
        };
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                }
            }
        });
        passwordField.setForeground(Color.GRAY);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 18)); // Increased font size
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        return passwordField;
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 18)); // Increased font size
        button.setOpaque(false); // Make button transparent so we can custom paint
        button.setContentAreaFilled(false); // Tell Swing to not fill the content area
        button.setBorderPainted(false); // Tell Swing to not paint the border
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Increased padding
        button.setFocusable(false); // Optional: Removes the focus border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Changes the cursor to a hand cursor on hover

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                g.setColor(b.getBackground());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30); // 30, 30 defines the roundness
                super.paint(g, c);
            }
        });
    }

    private void customizeButtonback(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // Set the background color of the button
        button.setForeground(Color.WHITE); // Set text color
        button.setFocusPainted(false); // Remove focus border
        button.setFont(new Font("Tahoma", Font.BOLD, 18)); // Increase font size
        button.setOpaque(true); // Set button opacity
        button.setContentAreaFilled(false); // Set if the content area is filled
        button.setBorderPainted(false); // Remove border painting
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Add padding
        button.setFocusable(false); // Remove focusability
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand cursor

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand cursor
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor()); // Change cursor to default
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                g.setColor(b.getBackground());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30); // Rounded corners
                super.paint(g, c);
            }
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private boolean authorizeUser() {
        // TODO Kagan: implement authorize user.
        return true;
    }
}