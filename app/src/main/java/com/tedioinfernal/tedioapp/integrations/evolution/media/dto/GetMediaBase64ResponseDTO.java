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
public class GetMediaBase64ResponseDTO {
    
    @JsonProperty("mediaType")
    private String mediaType;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("size")
    private MediaSizeDTO size;
    
    @JsonProperty("mimetype")
    private String mimetype;
    
    @JsonProperty("base64")
    private String base64;
    
    @JsonProperty("buffer")
    private Object buffer;
}
