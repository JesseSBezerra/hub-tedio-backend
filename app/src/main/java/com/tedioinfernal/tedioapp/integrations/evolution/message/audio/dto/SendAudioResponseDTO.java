package com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto;

import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.MessageKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendAudioResponseDTO {
    
    private MessageKeyDTO key;
    private String pushName;
    private String status;
    private AudioMessageContentDTO message;
    private Object contextInfo;
    private String messageType;
    private Long messageTimestamp;
    private String instanceId;
    private String source;
}
