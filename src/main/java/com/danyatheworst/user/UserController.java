package com.danyatheworst.user;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class UserController {

    @GetMapping("/sign-up")
    public String signUp() {

        //check if user is not logged in and return the sign-up page

        return "sign-up";
    }

    @PostMapping()
    public String signUp(
            @Valid @ModelAttribute("signUpRequestDto") SignUpRequestDto signUpRequestDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "sign-up";
        }
        if (!Objects.equals(signUpRequestDto.getPassword(), signUpRequestDto.getRepeatedPassword())) {
            result.rejectValue(
                    "repeatPassword",
                    "error.signUpRequestDto",
                    "Passwords do not match");
            return "sign-up";
        }



        return "index";
    }
}
