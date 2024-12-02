package org.example;

import org.example.control.DatabaseController;
import org.example.view.LoginScreenView;


public class Main {
    public static void main(String[] args) {
        DatabaseController.initUserTable();
        DatabaseController.initAdminTable();
        LoginScreenView loginScreenView = new LoginScreenView();
    }
}