package org.example.view;

import org.example.control.DatabaseController;
import org.example.model.BankAccountModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        List<String> users = DatabaseController.getBankAccounts();
        accountPanel.removeAll();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));

        for (String user : users) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel typeLabel = new JLabel("Account: " + user);
            panel.add(typeLabel);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
         
            accountPanel.add(panel);
        }

        accountPanel.revalidate();
        accountPanel.repaint();
    }

}
