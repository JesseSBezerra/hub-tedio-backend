package com.tedioinfernal.tedioapp.integrations.evolution.instance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDTO {
    
    private String instanceName;
    private String instanceId;
    private String integration;
    private String webhookWaBusiness;
    private String accessTokenWaBusiness;
    private String status;
}
