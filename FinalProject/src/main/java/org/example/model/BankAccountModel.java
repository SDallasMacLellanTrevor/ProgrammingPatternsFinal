package org.example.model;

import java.text.DecimalFormat;
import java.util.Objects;

public abstract class BankAccountModel {
    protected String accountType;
    protected static int IDCounter = 0;
    protected int ID;
    protected double balance;
    protected static final DecimalFormat df = new DecimalFormat("0.00");

    protected boolean deposit(double amount) {
        if (amount >= 0) {
            balance += amount;
            return true;
        } else {
            return false;
        }
    }

    protected boolean withdraw(double amount) {
        if (amount <= balance && amount >= 0) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    protected BankAccountModel() {
        ID = ++IDCounter;
        balance = 0;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountModel that = (BankAccountModel) o;
        return Double.compare(balance, that.balance) == 0 && Objects.equals(accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, balance);
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getBalance() {
        return Double.parseDouble(df.format(balance));
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
