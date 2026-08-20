package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlow {
    private Long id;
    private Instant createdAt;
    private BigDecimal amount;
}