package org.example.view;

import org.example.control.DatabaseController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class AdminWindowView {

    private int userID;
    private String selectedUser;
    private String selectedAdmin;
    JFrame frame = new JFrame();
    JPanel userPanel = new JPanel();
    JPanel userAccountPanel = new JPanel();
    JScrollPane userAccountScrollPane = new JScrollPane(userAccountPanel);
    JPanel adminPanel = new JPanel();
    JPanel adminAccountPanel = new JPanel();
    JScrollPane adminAccountScrollPane = new JScrollPane(adminAccountPanel);

    JLabel signedIn = new JLabel("", JLabel.CENTER);
    JButton signOut = new JButton("Sign Out");

    JButton newAccount = new JButton("Create a new user account");
    JButton deleteAccount = new JButton("Delete selected account");
    JButton newAdmin = new JButton("Create new admin account");
    JButton deleteAdmin = new JButton("Delete selected admin");

    public AdminWindowView(int x, int y, String userID) {
        this.userID = Integer.parseInt(userID);
        frame.setLayout(null);
        frame.setSize(new Dimension(800, 600));
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        signedIn.setFont(new Font("Arial", Font.PLAIN, 40));
        signedIn.setBounds(200, 20, 400, 60);
        signedIn.setText("Signed in as Admin: " + userID);
        frame.add(signedIn);

        signOut.setBounds(300, 500, 200, 50);
        signOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        frame.add(signOut);

        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userAccountScrollPane.setBounds(200,100,400, 100);
        userAccountPanel.setLayout(new BoxLayout(userAccountPanel, BoxLayout.Y_AXIS));
        userAccountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userAccountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(userAccountScrollPane);

        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminAccountScrollPane.setBounds(200,300,400, 100);
        adminAccountPanel.setLayout(new BoxLayout(adminAccountPanel, BoxLayout.Y_AXIS));
        adminAccountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        adminAccountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(adminAccountScrollPane);

        newAccount.setBounds(399,200,200, 20);
        deleteAccount.setBounds(200,200,199, 20);
        frame.add(newAccount);
        frame.add(deleteAccount);

        newAdmin.setBounds(399, 400, 200, 20);
        deleteAdmin.setBounds(200, 400, 199, 20);
        frame.add(newAdmin);
        frame.add(deleteAdmin);

        initAccounts();
        initAdminAccounts();
        frame.setResizable(false);
        frame.setVisible(true);

        signOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginScreenView loginScreenView = new LoginScreenView();
                frame.setEnabled(false);
                frame.dispose();
            }
        });

        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });

        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseController.deleteUserRecord(selectedUser);
                initAccounts();
            }
        });

        newAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdmin();
            }
        });

        deleteAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectedAdmin.equals(userID)) {
                    DatabaseController.deleteAdminRecord(selectedAdmin);
                    initAdminAccounts();
                }
            }
        });
    }

    private void initAccounts() {
        List<String> users = DatabaseController.getBankAccounts();
        userAccountPanel.removeAll();
        userAccountPanel.setLayout(new BoxLayout(userAccountPanel, BoxLayout.Y_AXIS));

        for (String user : users) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel accountLabel = new JLabel("Account: " + user);
            panel.add(accountLabel);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            accountLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedUser = user;
                    updateLabels();
                }
            });

            userAccountPanel.add(panel);
        }

        userAccountPanel.revalidate();
        userAccountPanel.repaint();
    }

    private void initAdminAccounts() {
        List<String> admins = DatabaseController.getAdminAccounts();
        adminAccountPanel.removeAll();
        adminAccountPanel.setLayout(new BoxLayout(adminAccountPanel, BoxLayout.Y_AXIS));

        for (String admin : admins) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel accountLabel = new JLabel("Admin: " + admin);
            panel.add(accountLabel);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            accountLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedAdmin = admin;
                    updateAdminLabels();
                }
            });

            adminAccountPanel.add(panel);
        }

        adminAccountPanel.revalidate();
        adminAccountPanel.repaint();
    }

    private void updateLabels() {
        for (Component component : userAccountPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component label : panel.getComponents()) {
                    if (label instanceof JLabel) {
                        JLabel accountLabel = (JLabel) label;
                        String user = accountLabel.getText().substring(9);
                        if (user.equals(selectedUser)) {
                            accountLabel.setBackground(Color.BLUE);
                            accountLabel.setForeground(Color.WHITE);
                            accountLabel.setOpaque(true);
                        } else {
                            accountLabel.setForeground(Color.BLACK);
                            accountLabel.setOpaque(false);
                        }
                    }
                }
            }
        }
    }

    private void updateAdminLabels() {
        for (Component component : adminAccountPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component label : panel.getComponents()) {
                    if (label instanceof JLabel) {
                        JLabel accountLabel = (JLabel) label;
                        String user = accountLabel.getText().substring(7);
                        if (user.equals(selectedAdmin)) {
                            accountLabel.setBackground(Color.BLUE);
                            accountLabel.setForeground(Color.WHITE);
                            accountLabel.setOpaque(true);
                        } else {
                            accountLabel.setForeground(Color.BLACK);
                            accountLabel.setOpaque(false);
                        }
                    }
                }
            }
        }
    }

    private void addAccount() {
        JFrame addFrame = new JFrame();
        addFrame.setLayout(null);
        addFrame.setSize(new Dimension(300, 258));
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel amount = new JLabel("New User ID:");
        amount.setBounds(100,0, 150, 50);
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 50, 284, 20);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JLabel password = new JLabel("User Password:");
        password.setBounds(100,80, 150, 50);
        JTextArea passwordArea = new JTextArea();
        passwordArea.setBounds(0, 130, 284, 20);
        passwordArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JButton addAccount = new JButton("Submit");
        addAccount.setBounds(0, 170, 150, 50);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(149, 170, 150, 50);

        addAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DatabaseController.accountExists(textArea.getText())) {
                    addFrame.dispose();
                } else {
                    try {
                        DatabaseController.insertUserRecord(textArea.getText().trim(), passwordArea.getText());
                        initAccounts();
                        addFrame.dispose();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFrame.dispose();
            }
        });

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });

        addFrame.add(amount);
        addFrame.add(textArea);
        addFrame.add(password);
        addFrame.add(passwordArea);
        addFrame.add(addAccount);
        addFrame.add(cancelButton);
        addFrame.setResizable(false);
        addFrame.setLocation(frame.getWidth()/3, frame.getHeight()/3);
        addFrame.setVisible(true);
    }

    private void addAdmin() {
        JFrame addFrame = new JFrame();
        addFrame.setLayout(null);
        addFrame.setSize(new Dimension(300, 258));
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel amount = new JLabel("New Admin ID:");
        amount.setBounds(100,0, 150, 50);
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 50, 284, 20);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JLabel password = new JLabel("Admin Password:");
        password.setBounds(100,80, 150, 50);
        JTextArea passwordArea = new JTextArea();
        passwordArea.setBounds(0, 130, 284, 20);
        passwordArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JButton addAccount = new JButton("Submit");
        addAccount.setBounds(0, 170, 150, 50);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(149, 170, 150, 50);

        addAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DatabaseController.adminExists(textArea.getText())) {
                    addFrame.dispose();
                } else {
                    try {
                        DatabaseController.insertAdminRecord(textArea.getText().trim(), passwordArea.getText());
                        initAdminAccounts();
                        addFrame.dispose();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFrame.dispose();
            }
        });

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });

        addFrame.add(amount);
        addFrame.add(textArea);
        addFrame.add(password);
        addFrame.add(passwordArea);
        addFrame.add(addAccount);
        addFrame.add(cancelButton);
        addFrame.setResizable(false);
        addFrame.setLocation(frame.getWidth()/3, frame.getHeight()/3);
        addFrame.setVisible(true);
    }
}
