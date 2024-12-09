package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminWindowView {

    private int userID;
    JFrame frame = new JFrame();
    JPanel adminPanel = new JPanel();
    JPanel accountPanel = new JPanel();
    JScrollPane accountScrollPane = new JScrollPane(accountPanel);

    JLabel signedIn = new JLabel("", JLabel.CENTER);
    JButton signOut = new JButton("Sign Out");

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

        signOut.setBounds(300, 300, 200, 50);
        signOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        frame.add(signOut);

        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setBounds(200,100,400, 100);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        accountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(accountScrollPane);

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

    }

}
