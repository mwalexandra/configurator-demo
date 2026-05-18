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

        try {
            String sapResponse = sapVcpClient.createConfiguration();
            System.out.println("SAP RAW: " + sapResponse);
            return sapMappingService.mapToConfiguratorResponse(sapResponse);
        } catch (Exception e) {
            System.out.println("SAP ERROR: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ConfiguratorResponse updateConfiguration(UpdateConfigurationRequest request) {
        try {
            String sapResponse = sapVcpClient.createConfiguration();
            return sapMappingService.mapToConfiguratorResponse(sapResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}