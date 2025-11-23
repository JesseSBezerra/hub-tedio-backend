package com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaMessageContentDTO {
    
    private ImageMessageDTO imageMessage;
    private Object videoMessage;
    private Object audioMessage;
    private Object documentMessage;
}
