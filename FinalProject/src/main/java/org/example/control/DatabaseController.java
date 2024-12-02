package org.example.control;

import java.sql.*;

public class DatabaseController {
    private static String url = "jdbc:sqlite:./src/main/resources/userAccounts.db";

    public static boolean verifyUserLogin(String usernameText, String passwordText) {
        var sql = """
                SELECT ID, PASSWORD, SALT FROM userAccounts WHERE ID = ?
                """;
        try (Connection connection = DriverManager.getConnection(url);
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
}
