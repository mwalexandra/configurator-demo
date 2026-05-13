package de.thm.configurator.dto;

import java.util.Map;

public record InitConfigurationRequest(
        String productId,
        String kbId,
        String locale,
        Map<String, String> selections
) {}