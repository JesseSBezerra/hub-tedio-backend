package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFieldsRequestDTO {

    @NotNull(message = "Os campos do response não podem ser nulos")
    private Map<String, String> responseFields;
}
