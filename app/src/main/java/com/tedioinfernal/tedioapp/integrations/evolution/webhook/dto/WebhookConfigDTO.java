package com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookConfigDTO {
    
    private Boolean enabled;
    private String url;
    private Map<String, String> headers;
    private Boolean byEvents;
    private Boolean base64;
    private List<String> events;
}
