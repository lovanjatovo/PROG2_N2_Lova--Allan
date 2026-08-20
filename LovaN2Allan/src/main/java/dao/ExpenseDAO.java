package dao;

import model.Expense;
import model.ExpenseFrequency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private final Connection connection;

    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public Expense save(Long userId, Expense expense) throws SQLException {

        String cashFlowSql = """
                INSERT INTO cash_flows
                (user_id, created_at, amount)
                VALUES (?, ?, ?)
                """;

        String expenseSql = """
                INSERT INTO expenses
                (id, reason, frequency)
                VALUES (?, ?, ?)
                """;

        try {
            connection.setAutoCommit(false);

            // Création du CashFlow
            try (PreparedStatement statement = connection.prepareStatement(
                    cashFlowSql,
                    Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, userId);
                statement.setTimestamp(
                        2,
                        Timestamp.from(expense.getCreatedAt())
                );
                statement.setBigDecimal(3, expense.getAmount());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (resultSet.next()) {
                        expense.setId(resultSet.getLong(1));
                    } else {
                        throw new SQLException(
                                "Unable to retrieve generated CashFlow ID"
                        );
                    }
                }
            }

            // Création de l'Expense
            try (PreparedStatement statement =
                         connection.prepareStatement(expenseSql)) {

                statement.setLong(1, expense.getId());
                statement.setString(2, expense.getReason());
                statement.setString(
                        3,
                        expense.getFrequency().name()
                );

                statement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException exception) {

            connection.rollback();
            throw exception;

        } finally {

            connection.setAutoCommit(true);
        }

        return expense;
    }

    // READ - par ID
    public Expense findById(Long id) throws SQLException {

        String sql = """
                SELECT
                    cf.id,
                    cf.created_at,
                    cf.amount,
                    e.reason,
                    e.frequency
                FROM cash_flows cf
                INNER JOIN expenses e ON e.id = cf.id
                WHERE cf.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapExpense(resultSet);
                }
            }
        }

        return null;
    }

    // READ - expenses d'un utilisateur
    public List<Expense> findByUserId(Long userId) throws SQLException {

        String sql = """
                SELECT
                    cf.id,
                    cf.created_at,
                    cf.amount,
                    e.reason,
                    e.frequency
                FROM cash_flows cf
                INNER JOIN expenses e ON e.id = cf.id
                WHERE cf.user_id = ?
                ORDER BY cf.created_at DESC
                """;

        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    expenses.add(mapExpense(resultSet));
                }
            }
        }

        return expenses;
    }

    // UPDATE
    public void update(Expense expense) throws SQLException {

        String expenseSql = """
                UPDATE expenses
                SET reason = ?,
                    frequency = ?
                WHERE id = ?
                """;

        String cashFlowSql = """
                UPDATE cash_flows
                SET created_at = ?,
                    amount = ?
                WHERE id = ?
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(expenseSql)) {

                statement.setString(1, expense.getReason());
                statement.setString(
                        2,
                        expense.getFrequency().name()
                );
                statement.setLong(3, expense.getId());

                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(cashFlowSql)) {

                statement.setTimestamp(
                        1,
                        Timestamp.from(expense.getCreatedAt())
                );
                statement.setBigDecimal(2, expense.getAmount());
                statement.setLong(3, expense.getId());

                statement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException exception) {

            connection.rollback();
            throw exception;

        } finally {

            connection.setAutoCommit(true);
        }
    }

    // DELETE
    public void delete(Long id) throws SQLException {

        String expenseSql = """
                DELETE FROM expenses
                WHERE id = ?
                """;

        String cashFlowSql = """
                DELETE FROM cash_flows
                WHERE id = ?
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(expenseSql)) {

                statement.setLong(1, id);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(cashFlowSql)) {

                statement.setLong(1, id);
                statement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException exception) {

            connection.rollback();
            throw exception;

        } finally {

            connection.setAutoCommit(true);
        }
    }

    // Mapping ResultSet -> Expense
    private Expense mapExpense(ResultSet resultSet) throws SQLException {

        Expense expense = new Expense();

        expense.setId(resultSet.getLong("id"));
        expense.setCreatedAt(
                resultSet.getTimestamp("created_at").toInstant()
        );
        expense.setAmount(
                resultSet.getBigDecimal("amount")
        );
        expense.setReason(
                resultSet.getString("reason")
        );

        String frequency = resultSet.getString("frequency");

        if (frequency != null) {
            expense.setFrequency(
                    ExpenseFrequency.valueOf(frequency)
            );
        }

        return expense;
    }
}