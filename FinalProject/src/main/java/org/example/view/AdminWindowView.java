package org.example.view;

import org.example.control.DatabaseController;
import org.example.model.BankAccountModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminWindowView {

    private int userID;
    private String selectedUser;
    JFrame frame = new JFrame();
    JPanel adminPanel = new JPanel();
    JPanel accountPanel = new JPanel();
    JScrollPane accountScrollPane = new JScrollPane(accountPanel);

    JLabel signedIn = new JLabel("", JLabel.CENTER);
    JButton signOut = new JButton("Sign Out");

    JButton newAccount = new JButton("Create a new user account");
    JButton deleteAccount = new JButton("Delete selected account");

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

        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setBounds(200,100,400, 100);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        accountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(accountScrollPane);

        newAccount.setBounds(399,200,200, 20);
        deleteAccount.setBounds(200,200,199, 20);
        frame.add(newAccount);
        frame.add(deleteAccount);

        initAccounts();
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
    }

    private void initAccounts() {
        List<String> users = DatabaseController.getBankAccounts();
        accountPanel.removeAll();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));

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

            accountPanel.add(panel);
        }

        accountPanel.revalidate();
        accountPanel.repaint();
    }

    private void updateLabels() {
        for (Component component : accountPanel.getComponents()) {
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
}
