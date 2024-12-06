package org.example.control;

import org.example.view.LoginScreenView;

public class WindowController {
    private static WindowController instance;

    private WindowController() {
    }

    public static WindowController getInstance() {
        if (instance == null) {
            synchronized (WindowController.class) {
                if (instance == null) {
                    instance = new WindowController();
                }
            }
        }
        return instance;
    }

    public static void initialize() {
        DatabaseController.initUserTable();
        DatabaseController.initAdminTable();
        LoginScreenView loginScreenView = new LoginScreenView();
    }


}
