package org.example.control;

import java.sql.*;

public class DatabaseController {
    private static String userUrl = "jdbc:sqlite:./src/main/resources/userAccounts.db";
    private static String adminUrl =  "jdbc:sqlite:./src/main/resources/adminAccounts.db";

    public static void initUserTable() {
        try (Connection connection = DriverManager.getConnection(userUrl);
            Statement statement = connection.createStatement()) {
            String SQL = """
                CREATE TABLE IF NOT EXISTS userAccounts (
                ID NUMERIC PRIMARY KEY,
                PASSWORD NUMERIC NOT NULL,
                SALT TEXT NOT NULL
                );
                """;
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initAdminTable() {
        try (Connection connection = DriverManager.getConnection(adminUrl);
             Statement statement = connection.createStatement()) {
            String SQL = """
                CREATE TABLE IF NOT EXISTS adminAccounts (
                ID NUMERIC PRIMARY KEY,
                PASSWORD NUMERIC NOT NULL,
                SALT TEXT NOT NULL
                );
                """;
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyUserLogin(String usernameText, String passwordText) {
        var sql = """
                SELECT ID, PASSWORD, SALT FROM userAccounts WHERE ID = ?
                """;
        try (Connection connection = DriverManager.getConnection(userUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usernameText);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (String.valueOf(passwordText.concat
                                    (resultSet.getString("SALT")).hashCode())
                            .equals(resultSet.getString("PASSWORD"))) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean verifyAdminLogin(String usernameText, String passwordText) {

        return true;
    }
}
