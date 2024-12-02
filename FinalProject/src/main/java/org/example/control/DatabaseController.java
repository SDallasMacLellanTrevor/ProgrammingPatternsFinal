package org.example.control;

import java.sql.*;
import java.util.Random;

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

    public static void insertUserRecord(String username, String password) throws SQLException {
        String salt = generateSalt();
        String saltedPassword = String.valueOf(password.concat(salt).hashCode());

        var insertSQL = """
                    INSERT INTO userAccounts VALUES (?, ?, ?);
                    """;
        try (Connection connection = DriverManager.getConnection(userUrl);
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, saltedPassword);
            preparedStatement.setString(3, salt);
            preparedStatement.executeUpdate();
        }
    }

    public static void insertAdminRecord(String username, String password) throws SQLException {
        String salt = generateSalt();
        String saltedPassword = String.valueOf(password.concat(salt).hashCode());

        var insertSQL = """
                    INSERT INTO adminAccounts VALUES (?, ?, ?);
                    """;
        try (Connection connection = DriverManager.getConnection(adminUrl);
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, saltedPassword);
            preparedStatement.setString(3, salt);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean verifyUserLogin(String usernameText, String passwordText) {
        String sql = """
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
        String sql = """
                SELECT ID, PASSWORD, SALT FROM adminAccounts WHERE ID = ?
                """;
        try (Connection connection = DriverManager.getConnection(adminUrl);
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

    public static boolean adminExists() {
        try (Connection connection = DriverManager.getConnection(adminUrl);
        Statement statement = connection.createStatement()) {
            String sql = """
                SELECT ID FROM adminAccounts
                """;
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a string made up of random characters to be used as a password salt
     * @return A string of 10 random characters, symbols, and numbers
     */
    private static String generateSalt() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*(){}<>?,./";
        Random random = new Random();
        String salt = "";

        for (int i = 0; i < 10; i++) {
            salt = salt + String.valueOf(characters.charAt(random.nextInt(characters.length())));
        }
        return salt;
    }
}
