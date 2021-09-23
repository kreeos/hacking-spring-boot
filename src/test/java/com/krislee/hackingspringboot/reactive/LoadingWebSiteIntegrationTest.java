package com.krislee.hackingspringboot.reactive;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoadingWebSiteIntegrationTest {
    @Autowired WebTestClient client;

    @Test
    void test() {
        client.get().uri("/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).contains("<form method=\"post\" action=\"/add/");
                });
    }
}
