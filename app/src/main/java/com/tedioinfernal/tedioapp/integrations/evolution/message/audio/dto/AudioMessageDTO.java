package com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioMessageDTO {
    
    private String url;
    private String mimetype;
    private String fileSha256;
    private String fileLength;
    private Integer seconds;
    private Boolean ptt;
    private String mediaKey;
    private String fileEncSha256;
    private String directPath;
    private String mediaKeyTimestamp;
}
