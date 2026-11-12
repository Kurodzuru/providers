package org.example;

import org.example.dto.Provider;

import java.util.List;

public interface ProviderSource {
    List<Provider> loadProviders();
}
