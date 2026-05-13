package de.thm.configurator.dto;

import java.util.Map;

public record UpdateConfigurationRequest(
        String configurationId,
        Map<String, String> selections
) {}