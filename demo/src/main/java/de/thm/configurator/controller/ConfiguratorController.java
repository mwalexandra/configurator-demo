package de.thm.configurator.controller;

import de.thm.configurator.dto.ConfiguratorResponse;
import de.thm.configurator.dto.InitConfigurationRequest;
import de.thm.configurator.dto.UpdateConfigurationRequest;
import de.thm.configurator.service.ConfiguratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ConfiguratorController {

    private final ConfiguratorService configuratorService;

    public ConfiguratorController(ConfiguratorService configuratorService) {
        this.configuratorService = configuratorService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/configurations/init")
    public ConfiguratorResponse init(@RequestBody InitConfigurationRequest request) {
        return configuratorService.initConfiguration(request);
    }

    @PostMapping("/configurations/update")
    public ConfiguratorResponse update(@RequestBody UpdateConfigurationRequest request) {
        return configuratorService.updateConfiguration(request);
    }
}