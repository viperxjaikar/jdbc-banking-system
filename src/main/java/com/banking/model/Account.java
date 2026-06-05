package com.banking.model;

/**
 * Represents a Customer Account in the Banking System.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class Account {

    private int accountId;
    private String customerName;
    private String email;
    private String accountType;   // SAVINGS or CURRENT
    private double balance;

    public Account() {}

    public Account(String customerName, String email, String accountType, double balance) {
        this.customerName = customerName;
        this.email = email;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Account(int accountId, String customerName, String email, String accountType, double balance) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.email = email;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("| %-8d | %-20s | %-25s | %-10s | %-12.2f |",
                accountId, customerName, email, accountType, balance);
    }
}
