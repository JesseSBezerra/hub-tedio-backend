package com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto;

import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.MessageKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMediaResponseDTO {
    
    private MessageKeyDTO key;
    private String pushName;
    private String status;
    private MediaMessageContentDTO message;
    private Map<String, Object> contextInfo;
    private String messageType;
    private Long messageTimestamp;
    private String instanceId;
    private String source;
}
