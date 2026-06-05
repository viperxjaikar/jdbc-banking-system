package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Banking service layer with business logic and transaction management.
 * Handles deposit, withdrawal, and fund transfer with proper commit/rollback.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class BankingService {

    private final AccountDAO accountDAO;

    public BankingService() {
        this.accountDAO = new AccountDAO();
    }

    // ======================== Account CRUD ========================

    /**
     * Creates a new customer account.
     */
    public void createAccount(Account account) throws SQLException, InvalidAmountException {
        if (account.getBalance() < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative.");
        }

        int id = accountDAO.createAccount(account);
        if (id > 0) {
            System.out.println("Account created successfully. Account ID: " + id);
        } else {
            System.out.println("Failed to create account.");
        }
    }

    /**
     * Retrieves an account by ID.
     */
    public Account getAccount(int accountId) throws SQLException, AccountNotFoundException {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        return account;
    }

    /**
     * Gets all accounts.
     */
    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }

    /**
     * Search accounts by name.
     */
    public List<Account> searchByName(String name) throws SQLException {
        return accountDAO.searchByName(name);
    }

    /**
     * Updates customer details.
     */
    public void updateAccount(int accountId, String name, String email)
            throws SQLException, AccountNotFoundException {

        getAccount(accountId);  // Verify account exists
        if (accountDAO.updateAccount(accountId, name, email)) {
            System.out.println("Account updated successfully.");
        }
    }

    /**
     * Deletes a customer account.
     */
    public void deleteAccount(int accountId) throws SQLException, AccountNotFoundException {
        getAccount(accountId);  // Verify account exists
        if (accountDAO.deleteAccount(accountId)) {
            System.out.println("Account deleted successfully. ID: " + accountId);
        }
    }

    // ======================== Transaction Management ========================

    /**
     * Deposits amount into an account.
     * Uses transaction with commit/rollback.
     */
    public void deposit(int accountId, double amount)
            throws SQLException, AccountNotFoundException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);   // Start transaction

            Account account = accountDAO.getAccountById(conn, accountId);
            if (account == null) {
                throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
            }

            double newBalance = account.getBalance() + amount;
            accountDAO.updateBalance(conn, accountId, newBalance);

            conn.commit();   // Commit transaction
            System.out.printf("Deposited %.2f to Account %d. New Balance: %.2f%n",
                    amount, accountId, newBalance);

        } catch (Exception e) {
            if (conn != null) conn.rollback();   // Rollback on error
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Withdraws amount from an account.
     * Uses transaction with commit/rollback.
     */
    public void withdraw(int accountId, double amount)
            throws SQLException, AccountNotFoundException, InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);   // Start transaction

            Account account = accountDAO.getAccountById(conn, accountId);
            if (account == null) {
                throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
            }

            if (account.getBalance() < amount) {
                throw new InsufficientFundsException(
                        "Insufficient funds. Available: " + account.getBalance() + ", Requested: " + amount);
            }

            double newBalance = account.getBalance() - amount;
            accountDAO.updateBalance(conn, accountId, newBalance);

            conn.commit();   // Commit transaction
            System.out.printf("Withdrawn %.2f from Account %d. New Balance: %.2f%n",
                    amount, accountId, newBalance);

        } catch (Exception e) {
            if (conn != null) conn.rollback();   // Rollback on error
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Transfers funds between two accounts.
     * Uses single transaction — if either debit or credit fails, both are rolled back.
     * This is the key demonstration of transaction management.
     */
    public void transfer(int fromAccountId, int toAccountId, double amount)
            throws SQLException, AccountNotFoundException, InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }

        if (fromAccountId == toAccountId) {
            throw new InvalidAmountException("Cannot transfer to the same account.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);   // Start transaction

            // Get source account
            Account fromAccount = accountDAO.getAccountById(conn, fromAccountId);
            if (fromAccount == null) {
                throw new AccountNotFoundException("Source Account ID " + fromAccountId + " not found.");
            }

            // Get destination account
            Account toAccount = accountDAO.getAccountById(conn, toAccountId);
            if (toAccount == null) {
                throw new AccountNotFoundException("Destination Account ID " + toAccountId + " not found.");
            }

            // Check sufficient balance
            if (fromAccount.getBalance() < amount) {
                throw new InsufficientFundsException(
                        "Insufficient funds in Account " + fromAccountId
                                + ". Available: " + fromAccount.getBalance() + ", Requested: " + amount);
            }

            // Debit from source
            accountDAO.updateBalance(conn, fromAccountId, fromAccount.getBalance() - amount);

            // Credit to destination
            accountDAO.updateBalance(conn, toAccountId, toAccount.getBalance() + amount);

            conn.commit();   // Commit — both debit and credit succeed together
            System.out.printf("Transferred %.2f from Account %d to Account %d.%n",
                    amount, fromAccountId, toAccountId);
            System.out.printf("Account %d Balance: %.2f%n", fromAccountId, fromAccount.getBalance() - amount);
            System.out.printf("Account %d Balance: %.2f%n", toAccountId, toAccount.getBalance() + amount);

        } catch (Exception e) {
            if (conn != null) conn.rollback();   // Rollback — neither debit nor credit happens
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // ======================== Display ========================

    /**
     * Displays all accounts in a formatted table.
     */
    public void displayAllAccounts() throws SQLException {
        List<Account> accounts = getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("\n========================== ALL ACCOUNTS ==========================");
        System.out.printf("| %-8s | %-20s | %-25s | %-10s | %-12s |%n",
                "ID", "Customer Name", "Email", "Type", "Balance");
        System.out.println("|----------|----------------------|---------------------------|------------|--------------|");
        accounts.forEach(System.out::println);
        System.out.println("===================================================================\n");
    }
}
