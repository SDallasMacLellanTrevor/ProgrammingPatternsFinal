package org.example.view;
import org.example.control.DatabaseController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindowView {
    private int userID;
    JFrame frame = new JFrame();

    JPanel accountPanel = new JPanel();
    JScrollPane accountScrollPane = new JScrollPane(accountPanel);
    JTextArea TextArea = new JTextArea();
    JScrollPane TextAreaScrollPane = new JScrollPane(TextArea);
    JButton newChequing = new JButton("Open new Chequing account");
    JButton newSaving = new JButton("Open new Savings account");

    public MainWindowView(int x, int y, String userID) {
        this.userID = Integer.parseInt(userID);
        frame.setLayout(null);
        frame.setSize(new Dimension(800, 600));
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        accountScrollPane.setBounds(200,100,400, 50);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        frame.add(accountScrollPane);
        newChequing.setBounds(399,150,200, 20);
        newSaving.setBounds(200,150,199, 20);
        frame.add(newChequing);
        frame.add(newSaving);


        frame.setVisible(true);

        newChequing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseController.insertBankRecord("CHEQUING", Integer.parseInt(userID));
                initAccounts();
            }
        });
    }

    private void initAccounts() {
        accountScrollPane.removeAll();
        int height = 0;
        for (int i = 0; i < 20; i++) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel("User ID: " + userID++);
            label.setBounds(0,0, 100, 50);
            panel.add(label);
            panel.setBounds(0,height, 100, 200);
            accountPanel.add(panel);
            height += 20;
        }
    }
}
