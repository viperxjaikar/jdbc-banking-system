package com.banking.exception;

/**
 * Thrown when an invalid amount (zero or negative) is provided.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class InvalidAmountException extends Exception {

    public InvalidAmountException(String message) {
        super(message);
    }
}
