package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordModelDTO {
    @NotBlank
    @Size(min = 6, max = 40)
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;
}
