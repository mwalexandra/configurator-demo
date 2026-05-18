package de.thm.configurator.sap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thm.configurator.dto.ConfiguratorResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SapMappingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ConfiguratorResponse mapToConfiguratorResponse(String sapJson) {
        try {
            JsonNode sapResponse = objectMapper.readTree(sapJson);

            String configId = sapResponse.path("id").asText("unknown");
            String productKey = sapResponse.path("productKey").asText("UNKNOWN");

            List<ConfiguratorResponse.AttributeDto> attributes = new ArrayList<>();

            JsonNode cstics = sapResponse.path("cstics");
            if (cstics.isArray()) {
                for (JsonNode cstic : cstics) {
                    String name = cstic.path("name").asText();
                    String label = cstic.path("langdepname").asText(name);
                    String selectedValue = cstic.path("value").asText("");

                    List<ConfiguratorResponse.ValueDto> values = new ArrayList<>();
                    JsonNode domainValues = cstic.path("domainvalues");

                    if (domainValues.isArray()) {
                        for (JsonNode dv : domainValues) {
                            String val = dv.path("key").asText();
                            String valLabel = dv.path("langdepname").asText(val);
                            boolean selectable = dv.path("selectable").asBoolean(true);
                            boolean selected = val.equals(selectedValue);
                            values.add(new ConfiguratorResponse.ValueDto(val, valLabel, selectable, selected));
                        }
                    }
                    attributes.add(new ConfiguratorResponse.AttributeDto(name, label, values));
                }
            }

            if (attributes.isEmpty()) {
                attributes = fallbackAttributes(configId);
            }

            boolean complete = sapResponse.path("complete").asBoolean(false);

            return new ConfiguratorResponse(
                    configId, productKey, attributes,
                    new ConfiguratorResponse.PriceDto(new BigDecimal("0.00"), "EUR"),
                    complete
            );

        } catch (JsonProcessingException e) {
            System.out.println("JSON parse error: " + e.getMessage());
            return errorResponse(sapJson);
        }
    }

    private List<ConfiguratorResponse.AttributeDto> fallbackAttributes(String configId) {
        return List.of(
                new ConfiguratorResponse.AttributeDto(
                        "SAP_ID", "SAP Configuration ID",
                        List.of(new ConfiguratorResponse.ValueDto(
                                configId, configId, true, true))
                )
        );
    }

    private ConfiguratorResponse errorResponse(String raw) {
        return new ConfiguratorResponse(
                "error", "ERROR",
                List.of(new ConfiguratorResponse.AttributeDto(
                        "ERROR", "Parse Error",
                        List.of(new ConfiguratorResponse.ValueDto(raw, raw, false, true))
                )),
                new ConfiguratorResponse.PriceDto(BigDecimal.ZERO, "EUR"),
                false
        );
    }
}