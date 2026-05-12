package com.cuizhi.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCodeSendDto {

    @Email
    @NotBlank
    private String email;
}

