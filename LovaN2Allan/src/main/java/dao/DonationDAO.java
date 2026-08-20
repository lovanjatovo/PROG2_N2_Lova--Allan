package dao;

import model.Donation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {

    private final Connection connection;

    public DonationDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public Donation save(Long userId, Donation donation) throws SQLException {

        String cashFlowSql = """
                INSERT INTO cash_flows
                (user_id, created_at, amount)
                VALUES (?, ?, ?)
                """;

        String donationSql = """
                INSERT INTO donations
                (id, comment)
                VALUES (?, ?)
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
                        Timestamp.from(donation.getCreatedAt())
                );
                statement.setBigDecimal(3, donation.getAmount());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (resultSet.next()) {
                        donation.setId(resultSet.getLong(1));
                    } else {
                        throw new SQLException(
                                "Unable to retrieve generated CashFlow ID"
                        );
                    }
                }
            }

            // Création de la Donation
            try (PreparedStatement statement =
                         connection.prepareStatement(donationSql)) {

                statement.setLong(1, donation.getId());
                statement.setString(2, donation.getComment());

                statement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException exception) {

            connection.rollback();
            throw exception;

        } finally {

            connection.setAutoCommit(true);
        }

        return donation;
    }

    // READ - par ID
    public Donation findById(Long id) throws SQLException {

        String sql = """
                SELECT
                    cf.id,
                    cf.created_at,
                    cf.amount,
                    d.comment
                FROM cash_flows cf
                INNER JOIN donations d ON d.id = cf.id
                WHERE cf.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapDonation(resultSet);
                }
            }
        }

        return null;
    }

    // READ - donations d'un utilisateur
    public List<Donation> findByUserId(Long userId) throws SQLException {

        String sql = """
                SELECT
                    cf.id,
                    cf.created_at,
                    cf.amount,
                    d.comment
                FROM cash_flows cf
                INNER JOIN donations d ON d.id = cf.id
                WHERE cf.user_id = ?
                ORDER BY cf.created_at DESC
                """;

        List<Donation> donations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    donations.add(mapDonation(resultSet));
                }
            }
        }

        return donations;
    }

    public List<Donation> findAll() throws SQLException {

        String sql = """
            SELECT
                cf.id,
                cf.created_at,
                cf.amount,
                d.comment
            FROM cash_flows cf
            INNER JOIN donations d ON d.id = cf.id
            ORDER BY cf.created_at DESC
            """;

        List<Donation> donations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                donations.add(mapDonation(resultSet));
            }
        }

        return donations;
    }

    // UPDATE
    public void update(Donation donation) throws SQLException {

        String sql = """
                UPDATE donations
                SET comment = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, donation.getComment());
            statement.setLong(2, donation.getId());

            statement.executeUpdate();
        }

        updateCashFlow(donation);
    }

    private void updateCashFlow(Donation donation) throws SQLException {

        String sql = """
                UPDATE cash_flows
                SET created_at = ?,
                    amount = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(
                    1,
                    Timestamp.from(donation.getCreatedAt())
            );
            statement.setBigDecimal(2, donation.getAmount());
            statement.setLong(3, donation.getId());

            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(Long id) throws SQLException {

        String donationSql = """
                DELETE FROM donations
                WHERE id = ?
                """;

        String cashFlowSql = """
                DELETE FROM cash_flows
                WHERE id = ?
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(donationSql)) {

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

    // Mapping ResultSet -> Donation
    private Donation mapDonation(ResultSet resultSet) throws SQLException {

        Donation donation = new Donation();

        donation.setId(resultSet.getLong("id"));
        donation.setCreatedAt(
                resultSet.getTimestamp("created_at").toInstant()
        );
        donation.setAmount(
                resultSet.getBigDecimal("amount")
        );
        donation.setComment(
                resultSet.getString("comment")
        );

        return donation;
    }
}