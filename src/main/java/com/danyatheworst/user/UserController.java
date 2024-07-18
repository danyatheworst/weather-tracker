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
    public String signUp(Model model) {
        model.addAttribute("signUpRequestDto", new SignUpRequestDto());
        //check if user is not logged in and return the sign-up page

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(
            @Valid @ModelAttribute("signUpRequestDto") SignUpRequestDto signUpRequestDto,
            BindingResult result
    ) {
        if (!Objects.equals(signUpRequestDto.getPassword(), signUpRequestDto.getRepeatedPassword())) {
            result.rejectValue(
                    "repeatedPassword",
                    "error.signUpRequestDto",
                    "Passwords do not match");
        }

        int a = 123;

        if (result.hasErrors()) {
            return "sign-up";
        }


        return "index";
    }
}
