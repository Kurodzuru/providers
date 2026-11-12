package org.example.exceptions;

public class UnknownProviderException extends RuntimeException{

    public UnknownProviderException(String providerId) {
        super("Unknown provider: " + providerId);
    }
}
