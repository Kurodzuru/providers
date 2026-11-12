package org.example.providers.exceptions;

public class UnknownProviderException extends RuntimeException{

    public UnknownProviderException(String providerId) {
        super("Unknown provider: " + providerId);
    }
}
