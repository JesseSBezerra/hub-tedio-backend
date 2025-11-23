package com.tedioinfernal.tedioapp.integrations.evolution.instance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstanceRequestDTO {
    
    private String instanceName;
    private Boolean qrcode;
    private String integration;
}
