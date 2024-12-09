package org.example.view;
import org.example.control.DatabaseController;
import org.example.model.BankAccountFactory;
import org.example.model.BankAccountModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainWindowView {
    private int userID;
    JFrame frame = new JFrame();
    JLabel signedIn = new JLabel("", JLabel.CENTER);
    JButton signOut = new JButton("Sign Out");
    JPanel accountPanel = new JPanel();
    JScrollPane accountScrollPane = new JScrollPane(accountPanel);
    JButton newChequing = new JButton("Open new Chequing account");
    JButton newSaving = new JButton("Open new Savings account");
    JLabel errorLabel = new JLabel("", JLabel.CENTER);

    public MainWindowView(int x, int y, String userID) {
        this.userID = Integer.parseInt(userID);
        frame.setLayout(null);
        frame.setSize(new Dimension(800, 600));
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        signedIn.setFont(new Font("Arial", Font.PLAIN, 40));
        signedIn.setBounds(200, 20, 400, 60);
        signedIn.setText("Signed in as user: " + userID);
        frame.add(signedIn);

        accountScrollPane.setBounds(200,100,400, 100);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        accountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(accountScrollPane);
        newChequing.setBounds(399,200,200, 20);
        newSaving.setBounds(200,200,199, 20);
        frame.add(newChequing);
        frame.add(newSaving);
        frame.add(errorLabel);
        errorLabel.setBounds(220, 200, 400, 100);
        errorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        errorLabel.setForeground(Color.RED);

        signOut.setBounds(300, 300, 200, 50);
        signOut.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        frame.add(signOut);

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

        newChequing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountPanel.getComponents().length >= 10) {
                    errorLabel.setText("ERROR: MAX ACCOUNTS REACHED");
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
                    errorLabel.setText("ERROR: MAX ACCOUNTS REACHED");
                } else {
                    DatabaseController.insertBankRecord("SAVINGS", Integer.parseInt(userID));
                    initAccounts();
                }
            }
        });
    }

    private void initAccounts() {
        List<BankAccountModel> accountModels = getAccounts();
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
                    withdraw(accountModel.getID(), accountModel.getBalance());
                }
            });

            depositButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deposit(accountModel.getID(), accountModel.getBalance());
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

    private void withdraw(int id, double balance) {
        JFrame withdrawFrame = new JFrame();
        withdrawFrame.setLayout(null);
        withdrawFrame.setSize(new Dimension(300, 158));
        withdrawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel amount = new JLabel("Withdraw Amount:");
        amount.setBounds(100,0, 150, 50);
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 50, 284, 20);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(0, 70, 150, 50);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(149, 70, 150, 50);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double newBalance = balance - Double.parseDouble(textArea.getText());
                if (newBalance < 0) {
                    errorLabel.setText("NOT ENOUGH FUNDS IN ACCOUNT TO WITHDRAW");
                    withdrawFrame.dispose();
                } else {
                    DatabaseController.updateAccountBalance(id, newBalance);
                    initAccounts();
                    errorLabel.setText("FUNDS WITHDRAWN SUCCESSFULLY");
                    withdrawFrame.dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawFrame.dispose();
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

        withdrawFrame.add(amount);
        withdrawFrame.add(textArea);
        withdrawFrame.add(withdrawButton);
        withdrawFrame.add(cancelButton);
        withdrawFrame.setResizable(false);
        withdrawFrame.setLocation(frame.getWidth()/3, frame.getHeight()/3);
        withdrawFrame.setVisible(true);
    }

    private void deposit(int id, double balance) {
        JFrame depositFrame = new JFrame();
        depositFrame.setLayout(null);
        depositFrame.setSize(new Dimension(300, 158));
        depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel amount = new JLabel("Deposit Amount:");
        amount.setBounds(100,0, 150, 50);
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 50, 284, 20);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(0, 70, 150, 50);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(149, 70, 150, 50);

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double newBalance = Double.parseDouble(textArea.getText()) + balance;
                DatabaseController.updateAccountBalance(id, newBalance);
                initAccounts();
                errorLabel.setText("FUNDS DEPOSITED SUCCESSFULLY");
                depositFrame.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositFrame.dispose();
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

        depositFrame.add(amount);
        depositFrame.add(textArea);
        depositFrame.add(depositButton);
        depositFrame.add(cancelButton);
        depositFrame.setResizable(false);
        depositFrame.setLocation(frame.getWidth()/3, frame.getHeight()/3);
        depositFrame.setVisible(true);
    }
}
