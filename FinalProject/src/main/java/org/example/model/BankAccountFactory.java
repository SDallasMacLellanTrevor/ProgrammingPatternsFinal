package org.example.model;

public class BankAccountFactory {
    public BankAccountFactory() {
    }

    public static BankAccountModel createBankAccount(String accountType, int ID) {
        BankAccountModel account;
        switch (accountType) {
            case "chequing":
                account = new ChequingAccountModel();
                break;
            case "savings":
                account = new SavingsAccountModel();
                break;
            default:
                return null;
        }
        account.setID(ID);
        return account;
    }

}
