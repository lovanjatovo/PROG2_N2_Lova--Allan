package service;

import dao.CashFlowDAO;

import java.math.BigDecimal;
import java.sql.SQLException;

public class BalanceService {

    private final CashFlowDAO cashFlowDAO;

    public BalanceService(CashFlowDAO cashFlowDAO) {
        this.cashFlowDAO = cashFlowDAO;
    }

    public BigDecimal getBalance() throws SQLException {

        return cashFlowDAO.getBalance();
    }
}