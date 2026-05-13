package de.thm.configurator.service;

import de.thm.configurator.dto.ConfiguratorResponse;
import de.thm.configurator.dto.InitConfigurationRequest;
import de.thm.configurator.dto.UpdateConfigurationRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ConfiguratorService {

    public ConfiguratorResponse initConfiguration(InitConfigurationRequest request) {
        Map<String, String> selections = request.selections() == null ? Map.of() : request.selections();

        return buildResponse(
                UUID.randomUUID().toString(),
                request.productId(),
                selections
        );
    }

    public ConfiguratorResponse updateConfiguration(UpdateConfigurationRequest request) {
        Map<String, String> selections = request.selections() == null ? Map.of() : request.selections();

        return buildResponse(
                request.configurationId(),
                "DEMO_PRODUCT",
                selections
        );
    }

    private ConfiguratorResponse buildResponse(String configurationId,
                                               String productId,
                                               Map<String, String> selections) {

        String selectedColor = selections.getOrDefault("COLOR", "RED");
        String selectedEngine = selections.getOrDefault("ENGINE", "ELECTRIC");

        BigDecimal price = new BigDecimal("1000.00");

        if ("BLUE".equals(selectedColor)) {
            price = price.add(new BigDecimal("50.00"));
        }

        if ("PETROL".equals(selectedEngine)) {
            price = price.add(new BigDecimal("100.00"));
        }

        return new ConfiguratorResponse(
                configurationId,
                productId,
                List.of(
                        new ConfiguratorResponse.AttributeDto(
                                "COLOR",
                                "Color",
                                List.of(
                                        new ConfiguratorResponse.ValueDto("RED", "Red", true, "RED".equals(selectedColor)),
                                        new ConfiguratorResponse.ValueDto("BLUE", "Blue", true, "BLUE".equals(selectedColor))
                                )
                        ),
                        new ConfiguratorResponse.AttributeDto(
                                "ENGINE",
                                "Engine",
                                List.of(
                                        new ConfiguratorResponse.ValueDto("ELECTRIC", "Electric", true, "ELECTRIC".equals(selectedEngine)),
                                        new ConfiguratorResponse.ValueDto("PETROL", "Petrol", true, "PETROL".equals(selectedEngine))
                                )
                        )
                ),
                new ConfiguratorResponse.PriceDto(price, "EUR"),
                true
        );
    }
}