package org.example.dto;

import java.util.Objects;

public record Provider(String id, String baseUrl, String apiKey) {

    public Provider {
        requireNotBlank(id, "id");
        requireNotBlank(baseUrl, "baseUrl");
        requireNotBlank(apiKey, "apiKey");

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
    }

    private static void requireNotBlank(String value, String name) {
        Objects.requireNonNull(value, name + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " cannot be blank");
        }
    }

    @Override
    public String toString() {
        return "Provider[id=" + id + ", baseUrl=" + baseUrl + ", apiKey=***]";
    }
}
