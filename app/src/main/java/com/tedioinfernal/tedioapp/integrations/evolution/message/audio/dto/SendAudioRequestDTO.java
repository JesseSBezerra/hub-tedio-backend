package com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendAudioRequestDTO {
    
    private String number;
    private String audio;
}
