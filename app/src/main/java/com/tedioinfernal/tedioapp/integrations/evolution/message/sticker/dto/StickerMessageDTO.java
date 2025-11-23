package com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickerMessageDTO {
    
    private String url;
    private String fileSha256;
    private String fileEncSha256;
    private String mediaKey;
    private String mimetype;
    private String directPath;
    private String fileLength;
    private String mediaKeyTimestamp;
}
