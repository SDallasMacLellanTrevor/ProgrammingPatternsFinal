package org.example.view;

import org.example.control.DatabaseController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class LoginScreenView {
    JFrame frame = new JFrame("Login");
    JTextField username = new JTextField(1);
    JLabel usernameLabel = new JLabel("Username");
    JPasswordField password = new JPasswordField();
    JLabel passwordLabel = new JLabel("Password");
    JButton login = new JButton("Login");
    JButton createAccount = new JButton("Create Account");
    JLabel errorLabel = new JLabel("", JLabel.CENTER);
    ButtonGroup loginGroup = new ButtonGroup();
    JRadioButton userButton = new JRadioButton("User Login");
    JRadioButton adminButton = new JRadioButton("Admin Login");

    public LoginScreenView() {
        frame.setSize(new Dimension(800, 600));

        usernameLabel.setBounds(320, 150, 150, 20);
        usernameLabel.setText("User ID:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));

        username.setBounds(320, 170, 150, 20);
        username.setFont(new Font("Arial", Font.PLAIN, 12));

        passwordLabel.setBounds(320, 230, 150, 20);
        passwordLabel.setText("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));

        password.setBounds(320, 250, 150, 20);
        password.setEchoChar('*');
        password.setFont(new Font("Arial", Font.BOLD, 12));

        errorLabel.setBounds(0, 315, 800, 20);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setBackground(Color.BLACK);

        login.setBounds(410, 350, 120, 50);
        login.setFont(new Font("Arial", Font.BOLD, 11));
        createAccount.setBounds(260, 350,120, 50);
        createAccount.setFont(new Font("Arial", Font.BOLD, 11));

        loginGroup.add(userButton);
        loginGroup.add(adminButton);
        userButton.setBounds(300, 100, 100, 15);
        adminButton.setBounds(400, 100, 100, 15);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.add(createAccount);
        frame.add(login);
        frame.add(password);
        frame.add(passwordLabel);
        frame.add(username);
        frame.add(usernameLabel);
        frame.add(errorLabel);
        frame.setVisible(true);
        frame.add(userButton);
        frame.add(adminButton);
        userButton.setSelected(true);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyLogin();
            }
        });

        createAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });

        username.addKeyListener(new KeyAdapter() {
           @Override
           public void keyTyped(KeyEvent e) {
               char c = e.getKeyChar();
               if (!Character.isDigit(c)) {
                   e.consume();
               }
           }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DatabaseController.adminExists()) {
                    createAccount.setEnabled(false);
                }
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount.setEnabled(true);
            }
        });
    }

    private void verifyLogin() {
        String usernameText = username.getText();
        String passwordText = password.getText();


        if (usernameText.isBlank() || passwordText.isBlank()) {
            errorLabel.setText("Login Failed: Username and Password cannot be empty");
            username.setText("");
            password.setText("");
            username.requestFocus();
        } else {
            if (userButton.isSelected()) {
                if(DatabaseController.verifyUserLogin(usernameText, passwordText)) {
                    MainWindowView mainWindowView = new MainWindowView(frame.getX(), frame.getY(), usernameText);
                    frame.setEnabled(false);
                    frame.dispose();
                } else {
                    errorLabel.setText("Login Failed: Username and Password doesn't match");
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                }
            } else if (adminButton.isSelected()) {
                if(DatabaseController.verifyAdminLogin(usernameText, passwordText)) {
                    AdminWindowView adminWindowView = new AdminWindowView(frame.getX(), frame.getY(), usernameText);
                    frame.setEnabled(false);
                    frame.dispose();
                } else {
                    errorLabel.setText("Login Failed: Username and Password doesn't match");
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                }
            }
        }
    }

    private void addAccount() {
        String usernameText = username.getText();
        String passwordText = password.getText();

        if (usernameText.isBlank() || passwordText.isBlank()) {
            errorLabel.setText("Creation Failed: Username and Password cannot be empty");
            username.setText("");
            password.setText("");
            username.requestFocus();
        } else {
            if (userButton.isSelected()) {
                try {
                    DatabaseController.insertUserRecord(usernameText, passwordText);
                    MainWindowView mainWindowView = new MainWindowView(frame.getX(), frame.getY(), usernameText);
                    frame.setEnabled(false);
                    frame.dispose();
                } catch (SQLException e) {
                    if (e.getMessage().contains("UNIQUE constraint failed")) {
                        errorLabel.setText("Account with ID already exists");
                        username.setText("");
                        password.setText("");
                        username.requestFocus();
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            } else if (adminButton.isSelected()) {
                try {
                    DatabaseController.insertAdminRecord(usernameText, passwordText);
                    AdminWindowView adminWindowView = new AdminWindowView(frame.getX(), frame.getY(), usernameText);
                    frame.setEnabled(false);
                    frame.dispose();
                } catch (SQLException e) {
                    if (e.getMessage().contains("UNIQUE constraint failed")) {
                        errorLabel.setText("Account with ID already exists");
                        username.setText("");
                        password.setText("");
                        username.requestFocus();
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
