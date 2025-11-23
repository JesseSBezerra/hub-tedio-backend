package com.tedioinfernal.tedioapp.integrations.evolution.instance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeDTO {
    
    private String pairingCode;
    private String code;
    private String base64;
    private Integer count;
}
