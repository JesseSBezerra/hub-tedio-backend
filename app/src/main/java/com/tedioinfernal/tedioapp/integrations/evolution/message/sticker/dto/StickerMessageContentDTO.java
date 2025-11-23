package com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickerMessageContentDTO {
    
    private StickerMessageDTO stickerMessage;
}
