package org.example;

import org.example.dto.Provider;
import org.example.exceptions.UnknownProviderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ProviderRegistryTest {

    private static final String PHONE = "79001234567";
    private static final String API_KEY_HEADER = "X-API-Key";

    private MockRestServiceServer server;
    private ProviderRegistry registry;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();

        server = MockRestServiceServer.bindTo(builder).build();

        registry = new ProviderRegistry(builder);
        registry.load(new ProviderSourceList(List.of(
                new Provider("alpha", "https://alpha.example.com", "key-a"),
                new Provider("beta", "https://beta.example.com/api/", "key-b"))));
    }

    @Test
    void statusOkMeansAvailable() {
        server.expect(requestTo("https://alpha.example.com/subscribers/" + PHONE))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(API_KEY_HEADER, "key-a"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        assertTrue(registry.isAvailable("alpha", PHONE));
        server.verify();
    }

    @Test
    void notFoundMeansUnavailable() {
        server.expect(requestTo("https://beta.example.com/api/subscribers/" + PHONE))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(API_KEY_HEADER, "key-b"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertFalse(registry.isAvailable("beta", PHONE));
        server.verify();
    }

    @Test
    void otherHttpErrorsPropagate() {
        server.expect(requestTo("https://alpha.example.com/subscribers/" + PHONE))
                .andRespond(withServerError());

        assertThrows(HttpServerErrorException.class, () -> registry.isAvailable("alpha", PHONE));
    }

    @Test
    void replaceAllSwapsWholeSet() {
        registry.replaceAll(List.of(new Provider("gamma", "https://gamma.example.com", "key-g")));

        assertEquals(Set.of("gamma"), registry.getProviderIds());
        assertThrows(UnknownProviderException.class, () -> registry.isAvailable("alpha", PHONE));
    }

    // Дополнительно: некорректный набор отклоняется, старый продолжает работать.
    @Test
    void duplicateIdsRejectedAndOldSetKept() {
        List<Provider> invalid = List.of(
                new Provider("dup", "https://a.example.com", "k1"),
                new Provider("dup", "https://b.example.com", "k2"));

        assertThrows(IllegalArgumentException.class, () -> registry.replaceAll(invalid));
        assertEquals(Set.of("alpha", "beta"), registry.getProviderIds());
    }
}