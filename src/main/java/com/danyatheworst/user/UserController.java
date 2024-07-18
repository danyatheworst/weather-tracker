package com.danyatheworst.user;

import com.danyatheworst.common.ErrorResponseDto;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.exceptions.NotFoundException;
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

    @GetMapping("/sign-in")
    public String signIp(Model model) {
        model.addAttribute("signInRequestDto", new SignUpRequestDto());

        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIp(
            @Valid @ModelAttribute("signInRequestDto") SignInRequestDto signInRequestDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "sign-in";
        }

        try {
            User user =  this.userService.findBy(signInRequestDto.getLogin());
            int a = 123;
        } catch (NotFoundException e) {
            model.addAttribute("error", new ErrorResponseDto(e.getMessage()));
            return "sign-in";
        }

        return "redirect:/index";
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
            return "redirect:/sign-in";

        } catch (EntityAlreadyExistsException e) {
            model.addAttribute("error", new ErrorResponseDto(e.getMessage()));
            return "sign-up";
        }
    }
}
