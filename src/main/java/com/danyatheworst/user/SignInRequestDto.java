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
public class SignInRequestDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String login;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    private String redirectTo;

    public SignInRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public SignInRequestDto(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}
