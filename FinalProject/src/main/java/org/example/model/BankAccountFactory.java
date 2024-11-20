package org.example.model;

public class BankAccountFactory {
    public BankAccountFactory() {
    }

    public BankAccountModel createBankAccount(String accountType) {
        switch (accountType) {
            case "chequing":return new ChequingAccountModel();
            case "savings":return new SavingsAccountModel();
            default: return null;
        }
    }

}
