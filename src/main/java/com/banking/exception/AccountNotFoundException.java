package com.banking.exception;

/**
 * Thrown when an account is not found in the database.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
