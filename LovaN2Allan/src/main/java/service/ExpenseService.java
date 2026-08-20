package service;

import dao.ExpenseDAO;
import model.Expense;

import java.sql.SQLException;
import java.util.List;

public class ExpenseService {

    private final ExpenseDAO expenseDAO;

    public ExpenseService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    // CREATE
    public Expense createExpense(
            Long userId,
            Expense expense
    ) throws SQLException {

        validateExpense(userId, expense);

        return expenseDAO.save(userId, expense);
    }

    // READ
    public Expense getExpenseById(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Expense id cannot be null"
            );
        }

        return expenseDAO.findById(id);
    }

    // READ ALL
    public List<Expense> getAllExpenses()
            throws SQLException {

        return expenseDAO.findAll();
    }

    // READ BY USER
    public List<Expense> getExpensesByUserId(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User id cannot be null"
            );
        }

        return expenseDAO.findByUserId(userId);
    }

    // UPDATE
    public void updateExpense(Expense expense)
            throws SQLException {

        validateExpense(expense);

        if (expense.getId() == null) {
            throw new IllegalArgumentException(
                    "Expense id cannot be null"
            );
        }

        expenseDAO.update(expense);
    }

    // DELETE
    public void deleteExpense(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Expense id cannot be null"
            );
        }

        expenseDAO.delete(id);
    }

    private void validateExpense(
            Long userId,
            Expense expense
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User id cannot be null"
            );
        }

        validateExpense(expense);
    }

    private void validateExpense(Expense expense) {

        if (expense == null) {
            throw new IllegalArgumentException(
                    "Expense cannot be null"
            );
        }

        if (expense.getAmount() == null) {
            throw new IllegalArgumentException(
                    "Expense amount cannot be null"
            );
        }

        if (expense.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Expense amount must be positive"
            );
        }

        if (expense.getReason() == null
                || expense.getReason().isBlank()) {

            throw new IllegalArgumentException(
                    "Expense reason cannot be empty"
            );
        }

        if (expense.getFrequency() == null) {
            throw new IllegalArgumentException(
                    "Expense frequency cannot be null"
            );
        }
    }
}