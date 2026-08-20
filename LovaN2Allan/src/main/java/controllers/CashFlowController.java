package controllers;

import model.CashFlow;
import model.Expense;
import service.BalanceService;
import service.CashFlowService;
import service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping
public class CashFlowController {

    private final CashFlowService cashFlowService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;

    public CashFlowController(
            CashFlowService cashFlowService,
            ExpenseService expenseService,
            BalanceService balanceService
    ) {
        this.cashFlowService = cashFlowService;
        this.expenseService = expenseService;
        this.balanceService = balanceService;
    }

    // GET /cash-flows?type=donation
    // GET /cash-flows?type=expense
    @GetMapping("/cash-flows")
    public ResponseEntity<List<? extends CashFlow>> getCashFlows(
            @RequestParam String type
    ) throws SQLException {

        return ResponseEntity.ok(
                cashFlowService.getCashFlowsByType(type)
        );
    }

    // GET /users/{id}/cash-flows
    @GetMapping("/users/{id}/cash-flows")
    public ResponseEntity<List<CashFlow>> getUserCashFlows(
            @PathVariable Long id
    ) throws SQLException {

        return ResponseEntity.ok(
                cashFlowService.getCashFlowsByUserId(id)
        );
    }

    // POST /expenses
    @PostMapping("/expenses")
    public ResponseEntity<Expense> createExpense(
            @RequestParam Long userId,
            @RequestBody Expense expense
    ) throws SQLException {

        Expense createdExpense =
                expenseService.createExpense(userId, expense);

        return ResponseEntity.ok(createdExpense);
    }

    // GET /balance
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance()
            throws SQLException {

        return ResponseEntity.ok(
                balanceService.getBalance()
        );
    }
}