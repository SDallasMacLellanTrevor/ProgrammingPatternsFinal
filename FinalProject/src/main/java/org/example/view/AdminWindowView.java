package org.example.view;

import javax.swing.*;
import java.awt.*;

public class AdminWindowView {

    private int userID;
    JFrame frame = new JFrame();
    JPanel adminPanel = new JPanel();
    JPanel accountPanel = new JPanel();
    JScrollPane accountScrollPane = new JScrollPane(accountPanel);

    public AdminWindowView(int x, int y, String userID) {
        this.userID = Integer.parseInt(userID);
        frame.setLayout(null);
        frame.setSize(new Dimension(800, 600));
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setBounds(200,100,400, 100);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        accountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(accountScrollPane);

        frame.setVisible(true);
    }


}
