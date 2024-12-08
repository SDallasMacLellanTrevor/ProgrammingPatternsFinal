package org.example.view;
import org.example.control.DatabaseController;
import org.example.model.BankAccountFactory;
import org.example.model.BankAccountModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
        accountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        accountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(accountScrollPane);
        newChequing.setBounds(399,150,200, 20);
        newSaving.setBounds(200,150,199, 20);
        frame.add(newChequing);
        frame.add(newSaving);

        initAccounts();
        frame.setVisible(true);


        newChequing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountPanel.getComponents().length >= 10) {
                    System.out.println("TOO MANY ACCOUNTS");    // FIX THIS LATER WITH A LABEL
                } else {
                    DatabaseController.insertBankRecord("CHEQUING", Integer.parseInt(userID));
                    initAccounts();
                }
            }
        });

        newSaving.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountPanel.getComponents().length >= 10) {
                    System.out.println("TOO MANY ACCOUNTS");    // FIX THIS LATER WITH A LABEL
                } else {
                    DatabaseController.insertBankRecord("SAVINGS", Integer.parseInt(userID));
                    initAccounts();
                }
            }
        });
    }

    private void initAccounts() {
        List<BankAccountModel> accountModels = getAccounts();
        System.out.println("Number of accounts: " + accountModels.size());
        accountPanel.removeAll();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));

        for (BankAccountModel accountModel : accountModels) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel typeLabel = new JLabel("Account: " + accountModel.getAccountType());
            JLabel valueLabel = new JLabel("Balance: $" + accountModel.getBalance());
            panel.add(typeLabel);
            panel.add(valueLabel);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JButton withdrawButton = new JButton("Withdraw");
            JButton depositButton = new JButton("Deposit");

            panel.add(withdrawButton);
            panel.add(depositButton);

            withdrawButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("WITHDRAW for account " + accountModel.getID());
                }
            });

            depositButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("DEPOSIT for account " + accountModel.getID());
                }
            });

            accountPanel.add(panel);
        }

        accountPanel.revalidate();
        accountPanel.repaint();
    }

    private List<BankAccountModel> getAccounts() {
        ArrayList<String> accounts = new ArrayList<>(List.of());
        accounts.addAll(DatabaseController.getBankAccountByID(String.valueOf(userID)));
        ArrayList<BankAccountModel> newAccounts = new ArrayList<>();

        for (String account : accounts) {
            String[] accountData = account.split(",");
            BankAccountModel bankAccount = BankAccountFactory.createBankAccount(accountData[1].toLowerCase(), Integer.parseInt(accountData[0]));
            bankAccount.setBalance(Double.parseDouble(accountData[2]));
            newAccounts.add(bankAccount);
        }

        return newAccounts;
    }
}
