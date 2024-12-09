package org.example.control;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseController {
    private static String userUrl = "jdbc:sqlite:./FinalProject/src/main/resources/userAccounts.db";
    private static String adminUrl =  "jdbc:sqlite:./FinalProject/src/main/resources/adminAccounts.db";
    private static String bankUrl =  "jdbc:sqlite:./FinalProject/src/main/resources/bankAccounts.db";

    public static void initUserTable() {
        try (Connection connection = DriverManager.getConnection(userUrl);
            Statement statement = connection.createStatement()) {
            String SQL = """
                CREATE TABLE IF NOT EXISTS userAccounts (
                USERID INTEGER PRIMARY KEY,
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
                ADMINID INTEGER PRIMARY KEY,
                PASSWORD NUMERIC NOT NULL,
                SALT TEXT NOT NULL
                );
                """;
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initBankTable() {
        try (Connection connection = DriverManager.getConnection(bankUrl);
             Statement statement = connection.createStatement()) {
            String SQL = """
                CREATE TABLE IF NOT EXISTS bankAccounts (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                TYPE TEXT NOT NULL,
                VALUE TEXT NOT NULL,
                USERID INTEGER NOT NULL,
                FOREIGN KEY (USERID) REFERENCES userAccounts(USERID)
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
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, saltedPassword);
            preparedStatement.setString(3, salt);
            preparedStatement.executeUpdate();
        }
    }

    public static void insertBankRecord(String type, int userID) {

        var insertSQL = """
                    INSERT INTO bankAccounts (TYPE, VALUE, USERID) VALUES (?, ?, ?);
                    """;
        try (Connection connection = DriverManager.getConnection(bankUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, "0.00");
            preparedStatement.setInt(3, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyUserLogin(String usernameText, String passwordText) {
        String sql = """
                SELECT USERID, PASSWORD, SALT FROM userAccounts WHERE USERID = ?
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
                SELECT ADMINID, PASSWORD, SALT FROM adminAccounts WHERE ADMINID = ?
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
                SELECT ADMINID FROM adminAccounts
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

    public static List<String> getBankAccountByID(String userID) {
        String sql = """
                SELECT ID, TYPE, VALUE FROM bankAccounts WHERE USERID = ?
                """;
        try (Connection connection = DriverManager.getConnection(bankUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ArrayList<String> accounts = new ArrayList<>();
            preparedStatement.setString(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                        accounts.add(resultSet.getString("USERID") + "," + resultSet.getString("TYPE") + "," + resultSet.getString("VALUE"));
                    }
                return accounts;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateAccountBalance(int accountID, double newBalance) {
        String sql = """
            UPDATE bankAccounts SET VALUE = ? WHERE ID = ?
            """;
        try (Connection connection = DriverManager.getConnection(bankUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, accountID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getBankAccounts() {
        String sql = """
                SELECT USERID FROM userAccounts
                """;
        try (Connection connection = DriverManager.getConnection(userUrl);
             Statement statement = connection.createStatement()) {
            ArrayList<String> accounts = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    accounts.add(resultSet.getString("USERID"));
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAdminAccounts() {
        String sql = """
                SELECT ADMINID FROM adminAccounts
                """;
        try (Connection connection = DriverManager.getConnection(adminUrl);
             Statement statement = connection.createStatement()) {
            ArrayList<String> accounts = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    accounts.add(resultSet.getString("USERID"));
                }
                return accounts;
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
