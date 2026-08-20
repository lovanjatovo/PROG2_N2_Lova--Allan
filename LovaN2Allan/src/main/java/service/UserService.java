package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(User user) throws SQLException {
        validateUser(user);

        return userDAO.save(user);
    }

    public User getUserById(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        return userDAO.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public void updateUser(User user) throws SQLException {
        validateUser(user);

        if (user.getId() == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        userDAO.update(user);
    }

    public void deleteUser(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        userDAO.delete(id);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getRef() == null || user.getRef().isBlank()) {
            throw new IllegalArgumentException("User reference cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
    }
}