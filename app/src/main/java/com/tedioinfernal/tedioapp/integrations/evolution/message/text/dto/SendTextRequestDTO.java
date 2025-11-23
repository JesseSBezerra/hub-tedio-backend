package com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendTextRequestDTO {
    
    private String number;
    private String text;
}
