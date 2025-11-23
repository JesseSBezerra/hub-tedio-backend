package com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageMessageDTO {
    
    private String url;
    private String mimetype;
    private String fileSha256;
    private String fileLength;
    private Integer height;
    private Integer width;
    private String mediaKey;
    private String fileEncSha256;
    private String directPath;
    private String mediaKeyTimestamp;
    private String jpegThumbnail;
    private Map<String, Object> contextInfo;
}
