package com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetWebhookResponseDTO {
    
    private WebhookConfigDTO webhook;
}
