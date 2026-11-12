package org.example.providers.exceptions;

public class UnexpectedResponseException extends RuntimeException {

    public UnexpectedResponseException(String message) {
        super(message);
    }
}
