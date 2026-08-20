package controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.BalanceService;

import java.math.BigDecimal;
import java.sql.SQLException;

@RestController
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
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