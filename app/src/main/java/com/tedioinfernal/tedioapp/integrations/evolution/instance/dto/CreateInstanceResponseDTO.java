package com.tedioinfernal.tedioapp.integrations.evolution.instance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstanceResponseDTO {
    
    private InstanceDTO instance;
    private String hash;
    private Map<String, Object> webhook;
    private Map<String, Object> websocket;
    private Map<String, Object> rabbitmq;
    private Map<String, Object> sqs;
    private SettingsDTO settings;
    private QrCodeDTO qrcode;
}
