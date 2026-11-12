package org.example.providers;

import org.example.providers.dto.Provider;
import org.example.providers.dto.StatusResponse;
import org.example.providers.exceptions.UnexpectedResponseException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

public class ProviderClient {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String OK = "ok";

    private final Provider provider;
    private final RestClient restClient;

    ProviderClient(final Provider provider, final RestClient.Builder builder) {
        this.provider = provider;
        this.restClient = builder.clone()
                .baseUrl(provider.baseUrl())
                .defaultHeader(API_KEY_HEADER, provider.apiKey())
                .build();
    }

    boolean isAvailable(String phone) {
        StatusResponse body;
        try {
            body = restClient.get()
                    .uri("/subscribers/{phone}", phone)
                    .retrieve()
                    .body(StatusResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
        if (body != null && OK.equals(body.status())) {
            return true;
        }

        throw new UnexpectedResponseException(
                "Provider '" + provider.id() + "' returned unexpected body: " + body);
    }
}
