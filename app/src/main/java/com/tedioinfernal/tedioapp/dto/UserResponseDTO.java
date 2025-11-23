package com.tedioinfernal.tedioapp.dto;

import com.tedioinfernal.tedioapp.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private UserType userType;
    private String clientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
