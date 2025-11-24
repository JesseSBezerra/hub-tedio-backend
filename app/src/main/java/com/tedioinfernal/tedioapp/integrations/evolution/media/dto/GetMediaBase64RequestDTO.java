package com.tedioinfernal.tedioapp.integrations.evolution.media.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMediaBase64RequestDTO {
    
    @JsonProperty("message")
    private MessageDTO message;
    
    @JsonProperty("convertToMp4")
    private Boolean convertToMp4;
}
