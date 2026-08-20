package dao;

import lombok.Data;
import model.CashFlow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Data

public class CashFlowDAO {

    private final Connection connection;

    public CashFlowDAO(Connection connection) {
        this.connection = connection;
    }

    // READ - par ID
    public CashFlow findById(Long id) throws SQLException {

        String sql = """
                SELECT id, created_at, amount
                FROM cash_flows
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCashFlow(resultSet);
                }
            }
        }

        return null;
    }

    public List<CashFlow> findAll() throws SQLException {

        String sql = """
            SELECT
                id,
                created_at,
                amount
            FROM cash_flows
            ORDER BY created_at DESC
            """;

        List<CashFlow> cashFlows = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                cashFlows.add(mapCashFlow(resultSet));
            }
        }

        return cashFlows;
    }

    // READ - tous les CashFlow d'un utilisateur
    public List<CashFlow> findByUserId(Long userId) throws SQLException {

        String sql = """
                SELECT id, created_at, amount
                FROM cash_flows
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        List<CashFlow> cashFlows = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    cashFlows.add(mapCashFlow(resultSet));
                }
            }
        }

        return cashFlows;
    }

    // DELETE
    public void delete(Long id) throws SQLException {

        String sql = """
                DELETE FROM cash_flows
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    // Mapping ResultSet -> CashFlow
    private CashFlow mapCashFlow(ResultSet resultSet) throws SQLException {

        CashFlow cashFlow = new CashFlow();

        cashFlow.setId(resultSet.getLong("id"));
        cashFlow.setCreatedAt(
                resultSet.getTimestamp("created_at").toInstant()
        );
        cashFlow.setAmount(
                resultSet.getBigDecimal("amount")
        );

        return cashFlow;
    }
}