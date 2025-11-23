package com.tedioinfernal.tedioapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionsResponseDTO {

    private Long userId;
    private String userName;
    private String userEmail;
    private List<String> permissions;
}
