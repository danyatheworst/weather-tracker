package com.danyatheworst.user;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 symbols")
    private String login;

    @NotBlank
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 symbols")
    private String password;

    @NotBlank
    private String confirmPassword;
}
