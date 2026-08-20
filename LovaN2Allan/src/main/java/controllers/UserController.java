package controllers;

import model.CashFlow;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CashFlowService;
import service.UserService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id
    ) throws SQLException {

        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers()
            throws SQLException {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user
    ) throws SQLException {

        return ResponseEntity.ok(
                userService.createUser(user)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) throws SQLException {

        user.setId(id);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) throws SQLException {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cash-flows")
    public ResponseEntity<List<CashFlow>> getUserCashFlows(
            @PathVariable Long id
    ) throws SQLException {

        return ResponseEntity.ok(
                CashFlowService.getCashFlowsByUserId(id)
        );
    }
}