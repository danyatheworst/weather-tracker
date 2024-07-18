package com.danyatheworst.user;

import com.danyatheworst.common.ErrorResponseDto;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
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
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpRequestDto", new SignUpRequestDto());
        //check if user is not logged in and return the sign-up page

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(
            @Valid @ModelAttribute("signUpRequestDto") SignUpRequestDto signUpRequestDto,
            BindingResult result,
            Model model
    ) {
        if (!Objects.equals(signUpRequestDto.getPassword(), signUpRequestDto.getConfirmPassword())) {
            result.rejectValue(
                    "confirmPassword",
                    "error.signUpRequestDto",
                    "Passwords do not match");
        }

        if (result.hasErrors()) {
            return "sign-up";
        }
        try {
            this.userService.create(signUpRequestDto);
            return "sign-in";

        } catch (EntityAlreadyExistsException e) {
            model.addAttribute("error", new ErrorResponseDto(e.getMessage()));
            return "sign-up";
        }
    }
}
