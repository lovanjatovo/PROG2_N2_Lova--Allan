package dao;

import model.User;

import java.sql.*;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {

        String sql = """
                INSERT INTO users
                (ref, first_name, last_name, email, phone)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getRef());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhone());

            statement.executeUpdate();
        }
    }
}