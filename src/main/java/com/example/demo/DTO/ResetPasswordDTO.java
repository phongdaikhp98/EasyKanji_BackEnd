package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String otpCode;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;
}
