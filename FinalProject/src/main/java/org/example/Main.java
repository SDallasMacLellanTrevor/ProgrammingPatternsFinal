package org.example;

import org.example.control.WindowController;
import org.example.view.MainWindowView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
//        String userUrl = "jdbc:sqlite:./FinalProject/src/main/resources/userAccounts.db";
//        String adminUrl =  "jdbc:sqlite:./FinalProject/src/main/resources/adminAccounts.db";
//        String bankUrl =  "jdbc:sqlite:./FinalProject/src/main/resources/bankAccounts.db";
//
//        try (Connection connection = DriverManager.getConnection(userUrl);
//             Statement statement = connection.createStatement()) {
//            String SQL = """
//                DROP TABLE IF EXISTS userAccounts
//                """;
//            statement.executeUpdate(SQL);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try (Connection connection = DriverManager.getConnection(adminUrl);
//             Statement statement = connection.createStatement()) {
//            String SQL = """
//                DROP TABLE IF EXISTS  adminAccounts
//                """;
//            statement.executeUpdate(SQL);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try (Connection connection = DriverManager.getConnection(bankUrl);
//             Statement statement = connection.createStatement()) {
//            String SQL = """
//                DROP TABLE IF EXISTS  bankAccounts
//                """;
//            statement.executeUpdate(SQL);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        WindowController.initialize();
    }
}