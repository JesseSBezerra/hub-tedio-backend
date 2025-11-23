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
public class WebhookDTO {
    
    private Map<String, Object> webhook;
}
