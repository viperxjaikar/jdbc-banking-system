package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account table.
 * Performs CRUD operations using JDBC with PreparedStatement.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class AccountDAO {

    // ======================== CREATE ========================

    /**
     * Inserts a new account into the database.
     * Uses PreparedStatement to prevent SQL injection.
     */
    public int createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_name, email, account_type, balance) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getCustomerName());
            stmt.setString(2, account.getEmail());
            stmt.setString(3, account.getAccountType());
            stmt.setDouble(4, account.getBalance());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);  // Return generated account ID
                }
            }
        }
        return -1;
    }

    // ======================== READ ========================

    /**
     * Retrieves an account by its ID.
     */
    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves all accounts from the database.
     */
    public List<Account> getAllAccounts() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }

    /**
     * Search accounts by customer name (partial match).
     */
    public List<Account> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_name LIKE ?";
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }

    // ======================== UPDATE ========================

    /**
     * Updates customer name and email.
     */
    public boolean updateAccount(int accountId, String name, String email) throws SQLException {
        String sql = "UPDATE accounts SET customer_name = ?, email = ? WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, accountId);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates the account balance.
     * Used internally by deposit, withdraw, and transfer.
     */
    public boolean updateBalance(Connection conn, int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Gets an account using an existing connection (for transactions).
     */
    public Account getAccountById(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }

    // ======================== DELETE ========================

    /**
     * Deletes an account from the database.
     */
    public boolean deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ======================== Helper ========================

    /**
     * Maps a ResultSet row to an Account object.
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("account_id"),
                rs.getString("customer_name"),
                rs.getString("email"),
                rs.getString("account_type"),
                rs.getDouble("balance")
        );
    }
}
