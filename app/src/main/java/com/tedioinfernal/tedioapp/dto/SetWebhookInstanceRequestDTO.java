package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetWebhookInstanceRequestDTO {
    
    @NotBlank(message = "URL do webhook é obrigatória")
    private String webhookUrl;
}
