package com.banking.exception;

/**
 * Thrown when withdrawal or transfer amount exceeds available balance.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
