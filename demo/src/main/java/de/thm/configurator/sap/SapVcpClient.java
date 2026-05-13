package de.thm.configurator.sap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SapVcpClient {

    private final WebClient webClient;

    @Value("${sap.api.base-url}")
    private String baseUrl;

    @Value("${sap.api.key}")
    private String apiKey;

    public SapVcpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String testConnection(String path) {
        return webClient.get()
                .uri(baseUrl + path)
                .header("APIKey", apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}