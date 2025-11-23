package com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMediaRequestDTO {
    
    private String number;
    private String mediatype;
    private String mimetype;
    private String media;
    private String fileName;
}
