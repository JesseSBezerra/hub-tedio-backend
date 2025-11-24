package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionMediaRequestDTO {
    
    @NotBlank(message = "Message ID é obrigatório")
    private String messageId;
    
    @NotNull(message = "Evolution Instance ID é obrigatório")
    private Long evolutionInstanceId;
    
    @NotNull(message = "ConvertToMp4 é obrigatório")
    private Boolean convertToMp4;
}
