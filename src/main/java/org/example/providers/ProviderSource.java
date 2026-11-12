package org.example.providers;

import org.example.providers.dto.Provider;

import java.util.List;

public interface ProviderSource {
    List<Provider> loadProviders();
}
