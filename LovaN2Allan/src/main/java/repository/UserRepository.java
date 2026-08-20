package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@AllArgsConstructor
@Data
@Repository
public class UserRepository {
    private Connection connection;
    public UserRepository() {
        DataConnection dataConnection = new DataConnection();
        connection = dataConnection.getConnection();
    }
    public void getAll() {
        String sql = "SELECT * FROM \"user\"";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                resultSet.getInt("id");
                resultSet.getString("ref");
                resultSet.getString("firstname");
                resultSet.getString("lastname");
                resultSet.getString("email");
                resultSet.getString("phone");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }
}