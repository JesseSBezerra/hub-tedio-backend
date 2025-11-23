package com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto;

import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.MessageKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendStickerResponseDTO {
    
    private MessageKeyDTO key;
    private String pushName;
    private String status;
    private StickerMessageContentDTO message;
    private Object contextInfo;
    private String messageType;
    private Long messageTimestamp;
    private String instanceId;
    private String source;
}
