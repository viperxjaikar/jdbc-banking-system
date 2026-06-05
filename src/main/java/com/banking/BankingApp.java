package com.banking;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.service.BankingService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application for JDBC Banking System.
 *
 * Features:
 * 1. JDBC - MySQL database integration using PreparedStatement
 * 2. CRUD - Create, Read, Update, Delete customer accounts
 * 3. Transaction Management - Deposit, Withdraw, Transfer with commit/rollback
 * 4. Custom Exceptions - AccountNotFound, InsufficientFunds, InvalidAmount
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class BankingApp {

    private static final BankingService bankingService = new BankingService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("      JDBC BANKING SYSTEM");
        System.out.println("      Week 2 - Advanced Java");
        System.out.println("======================================");

        while (true) {
            displayMenu();
            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1:  createAccount();       break;
                case 2:  viewAccount();          break;
                case 3:  viewAllAccounts();      break;
                case 4:  searchAccounts();       break;
                case 5:  updateAccount();        break;
                case 6:  deleteAccount();        break;
                case 7:  deposit();              break;
                case 8:  withdraw();             break;
                case 9:  transfer();             break;
                case 0:
                    System.out.println("\nGoodbye.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n---------- MENU ----------");
        System.out.println("1. Create Account");
        System.out.println("2. View Account");
        System.out.println("3. View All Accounts");
        System.out.println("4. Search by Name");
        System.out.println("5. Update Account");
        System.out.println("6. Delete Account");
        System.out.println("7. Deposit");
        System.out.println("8. Withdraw");
        System.out.println("9. Transfer Funds");
        System.out.println("0. Exit");
    }

    // ======================== CRUD Operations ========================

    private static void createAccount() {
        String name = getStringInput("Customer Name: ");
        String email = getStringInput("Email: ");

        System.out.println("Account Type: 1. SAVINGS  2. CURRENT");
        int typeChoice = getIntInput("Select: ");
        String type = (typeChoice == 2) ? "CURRENT" : "SAVINGS";

        double balance = getDoubleInput("Initial Balance: ");

        try {
            bankingService.createAccount(new Account(name, email, type, balance));
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAccount() {
        int id = getIntInput("Account ID: ");
        try {
            Account account = bankingService.getAccount(id);
            System.out.println("\n--- Account Details ---");
            System.out.println("Account ID   : " + account.getAccountId());
            System.out.println("Customer Name: " + account.getCustomerName());
            System.out.println("Email        : " + account.getEmail());
            System.out.println("Account Type : " + account.getAccountType());
            System.out.printf("Balance      : %.2f%n", account.getBalance());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllAccounts() {
        try {
            bankingService.displayAllAccounts();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void searchAccounts() {
        String name = getStringInput("Search name: ");
        try {
            List<Account> results = bankingService.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }
            System.out.println("\nResults (" + results.size() + " found):");
            results.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void updateAccount() {
        int id = getIntInput("Account ID: ");
        String name = getStringInput("New Name: ");
        String email = getStringInput("New Email: ");

        try {
            bankingService.updateAccount(id, name, email);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteAccount() {
        int id = getIntInput("Account ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Cancelled.");
            return;
        }

        try {
            bankingService.deleteAccount(id);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================== Transactions ========================

    private static void deposit() {
        int id = getIntInput("Account ID: ");
        double amount = getDoubleInput("Deposit Amount: ");

        try {
            bankingService.deposit(id, amount);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException | InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdraw() {
        int id = getIntInput("Account ID: ");
        double amount = getDoubleInput("Withdraw Amount: ");

        try {
            bankingService.withdraw(id, amount);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException | InsufficientFundsException | InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transfer() {
        int fromId = getIntInput("From Account ID: ");
        int toId = getIntInput("To Account ID: ");
        double amount = getDoubleInput("Transfer Amount: ");

        try {
            bankingService.transfer(fromId, toId, amount);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AccountNotFoundException | InsufficientFundsException | InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================== Input Helpers ========================

    private static String getStringInput(String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }

    private static int getIntInput(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter valid number.");
            }
        }
    }

    private static double getDoubleInput(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter valid number.");
            }
        }
    }
}
