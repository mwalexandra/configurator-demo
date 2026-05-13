package de.thm.configurator.dto;

import java.math.BigDecimal;
import java.util.List;

public record ConfiguratorResponse(
        String configurationId,
        String productId,
        List<AttributeDto> attributes,
        PriceDto price,
        boolean complete
) {

    public record AttributeDto(
            String name,
            String label,
            List<ValueDto> values
    ) {}

    public record ValueDto(
            String value,
            String label,
            boolean selectable,
            boolean selected
    ) {}

    public record PriceDto(
            BigDecimal amount,
            String currency
    ) {}
}