package controllers;

import model.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ExpenseService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // GET /expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses()
            throws SQLException {

        return ResponseEntity.ok(
                expenseService.getAllExpenses()
        );
    }

    // GET /expenses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(
            @PathVariable Long id
    ) throws SQLException {

        Expense expense =
                expenseService.getExpenseById(id);

        if (expense == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(expense);
    }

    // GET /expenses/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUserId(
            @PathVariable Long userId
    ) throws SQLException {

        return ResponseEntity.ok(
                expenseService.getExpensesByUserId(userId)
        );
    }

    // POST /expenses?userId=1
    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestParam Long userId,
            @RequestBody Expense expense
    ) throws SQLException {

        return ResponseEntity.ok(
                expenseService.createExpense(
                        userId,
                        expense
                )
        );
    }

    // PUT /expenses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(
            @PathVariable Long id,
            @RequestBody Expense expense
    ) throws SQLException {

        expense.setId(id);
        expenseService.updateExpense(expense);

        return ResponseEntity.noContent().build();
    }

    // DELETE /expenses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id
    ) throws SQLException {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}