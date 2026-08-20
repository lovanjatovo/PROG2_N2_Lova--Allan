package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data

public class User extends UserDTO {
    public User(Long id, String ref, String firstName, String lastName, String email, String phone, List<CashFlow> cashFlows) {
        super(id, ref, firstName, lastName, email, phone);
        this.cashFlows = cashFlows;
    }
    private List<CashFlow> cashFlows = new ArrayList<>();
}