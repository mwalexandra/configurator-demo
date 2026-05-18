package de.thm.configurator.sap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class SapVcpClient {

    private final WebClient webClient;

    @Value("${sap.api.base-url}")
    private String baseUrl;

    @Value("${sap.api.key}")
    private String apiKey;

    @Value("${sap.api.kb-id}")
    private int kbId;

    @Value("${sap.api.product-key}")
    private String productKey;

    public SapVcpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String createConfiguration() {
        Map<String, Object> body = Map.of(
            "context", List.of(Map.of("name", "VBAP-VRKME", "value", "EA")),
            "date", "2018-08-09",
            "kbId", kbId,
            "productKey", productKey,
            "source", Map.of(
                "application", "cpq",
                "type", "quote_item",
                "id", "10"
            )
        );

        return webClient.post()
                .uri(baseUrl + "/api/v2/configurations")
                .header("APIKey", apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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