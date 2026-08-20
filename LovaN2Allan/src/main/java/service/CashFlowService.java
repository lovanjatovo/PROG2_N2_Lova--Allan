package service;

import dao.CashFlowDAO;
import dao.DonationDAO;
import dao.ExpenseDAO;
import model.CashFlow;

import java.sql.SQLException;
import java.util.List;

public class CashFlowService {

    private final CashFlowDAO cashFlowDAO;
    private final DonationDAO donationDAO;
    private final ExpenseDAO expenseDAO;

    public CashFlowService(
            CashFlowDAO cashFlowDAO,
            DonationDAO donationDAO,
            ExpenseDAO expenseDAO
    ) {
        this.cashFlowDAO = cashFlowDAO;
        this.donationDAO = donationDAO;
        this.expenseDAO = expenseDAO;
    }

    // READ
    public CashFlow getCashFlowById(Long id) throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "CashFlow id cannot be null"
            );
        }

        return cashFlowDAO.findById(id);
    }

    // READ BY USER
    public List<CashFlow> getCashFlowsByUserId(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User id cannot be null"
            );
        }

        return cashFlowDAO.findByUserId(userId);
    }

    // READ BY TYPE
    public List<? extends CashFlow> getCashFlowsByType(String type)
            throws SQLException {

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "CashFlow type cannot be empty"
            );
        }

        return switch (type.toLowerCase()) {

            case "donation" ->
                    donationDAO.findAll();

            case "expense" ->
                    expenseDAO.findAll();

            default ->
                    throw new IllegalArgumentException(
                            "Unknown cash flow type: " + type
                    );
        };
    }

    // DELETE
    public void deleteCashFlow(Long id) throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "CashFlow id cannot be null"
            );
        }

        cashFlowDAO.delete(id);
    }
}