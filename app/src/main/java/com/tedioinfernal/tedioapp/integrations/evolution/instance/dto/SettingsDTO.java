package com.tedioinfernal.tedioapp.integrations.evolution.instance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
    
    private Boolean rejectCall;
    private String msgCall;
    private Boolean groupsIgnore;
    private Boolean alwaysOnline;
    private Boolean readMessages;
    private Boolean readStatus;
    private Boolean syncFullHistory;
    private String wavoipToken;
}
