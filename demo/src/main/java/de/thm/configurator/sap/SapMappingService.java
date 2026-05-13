package de.thm.configurator.sap;

import com.fasterxml.jackson.databind.JsonNode;
import de.thm.configurator.dto.ConfiguratorResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SapMappingService {

    public ConfiguratorResponse mapToConfiguratorResponse(JsonNode sapResponse) {
        String configId = sapResponse.path("id").asText("unknown");
        String productKey = sapResponse.path("productKey").asText("UNKNOWN");

        List<ConfiguratorResponse.AttributeDto> attributes = new ArrayList<>();

        JsonNode items = sapResponse.path("cstics");
        if (items.isArray()) {
            for (JsonNode cstic : items) {
                String name = cstic.path("name").asText();
                String label = cstic.path("langdepname").asText(name);

                List<ConfiguratorResponse.ValueDto> values = new ArrayList<>();
                JsonNode domainValues = cstic.path("domainvalues");

                if (domainValues.isArray()) {
                    String selectedValue = cstic.path("value").asText("");
                    for (JsonNode dv : domainValues) {
                        String val = dv.path("key").asText();
                        String valLabel = dv.path("langdepname").asText(val);
                        boolean selectable = !dv.path("selectable").asBoolean(true) == false;
                        boolean selected = val.equals(selectedValue);
                        values.add(new ConfiguratorResponse.ValueDto(val, valLabel, selectable, selected));
                    }
                }

                attributes.add(new ConfiguratorResponse.AttributeDto(name, label, values));
            }
        }

        // Если SAP не вернул характеристики — показываем fallback
        if (attributes.isEmpty()) {
            attributes = fallbackAttributes();
        }

        ConfiguratorResponse.PriceDto price = new ConfiguratorResponse.PriceDto(
                new BigDecimal("0.00"), "EUR"
        );

        boolean complete = sapResponse.path("complete").asBoolean(false);

        return new ConfiguratorResponse(configId, productKey, attributes, price, complete);
    }

    private List<ConfiguratorResponse.AttributeDto> fallbackAttributes() {
        return List.of(
                new ConfiguratorResponse.AttributeDto(
                        "INFO", "SAP Response",
                        List.of(new ConfiguratorResponse.ValueDto(
                                "CONNECTED", "Connected to SAP Sandbox", true, true))
                )
        );
    }
}