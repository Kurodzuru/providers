package org.example;

import org.example.dto.Provider;

import java.util.List;

public class ProviderSourceList implements ProviderSource{

    private final List<Provider> providers;

    public ProviderSourceList(List<Provider> providers) {
        this.providers = providers;
    }

    @Override
    public List<Provider> loadProviders() {
        return providers;
    }
}
