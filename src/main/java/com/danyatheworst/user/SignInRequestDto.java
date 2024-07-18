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
    @Size(min = 3, max = 50, message = "The username and/or password you specified are not correct")
    private String login;

    @NotBlank
    @Size(min = 6, max = 50, message = "The username and/or password you specified are not correct")
    private String password;
}
