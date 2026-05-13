package de.thm.configurator.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.thm.configurator.dto.ConfiguratorResponse;
import de.thm.configurator.dto.InitConfigurationRequest;
import de.thm.configurator.dto.UpdateConfigurationRequest;
import de.thm.configurator.sap.SapMappingService;
import de.thm.configurator.sap.SapVcpClient;
import org.springframework.stereotype.Service;

@Service
public class ConfiguratorService {

    private final SapVcpClient sapVcpClient;
    private final SapMappingService sapMappingService;

    public ConfiguratorService(SapVcpClient sapVcpClient, SapMappingService sapMappingService) {
        this.sapVcpClient = sapVcpClient;
        this.sapMappingService = sapMappingService;
    }

    public ConfiguratorResponse initConfiguration(InitConfigurationRequest request) {
        System.out.println(">>> initConfiguration called, productId=" + request.productId());
        
        JsonNode sapResponse = sapVcpClient.createConfiguration();
        System.out.println("SAP RAW RESPONSE: " + sapResponse.toPrettyString());
        return sapMappingService.mapToConfiguratorResponse(sapResponse);
    }

    public ConfiguratorResponse updateConfiguration(UpdateConfigurationRequest request) {
        // для PoC пока тоже создаём новую конфигурацию
        // позже здесь будет PATCH /configurations/{id}
        JsonNode sapResponse = sapVcpClient.createConfiguration();
        return sapMappingService.mapToConfiguratorResponse(sapResponse);
    }
}