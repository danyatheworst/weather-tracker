package com.danyatheworst.user;

import com.danyatheworst.AuthenticationService;
import com.danyatheworst.common.ErrorResponseDto;
import com.danyatheworst.exceptions.DatabaseOperationException;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.UUID;

@Controller
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final SessionService sessionService;

    public UserController(UserService userService, AuthenticationService authenticationService, SessionService sessionService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
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
            Model model,
            HttpServletResponse response
    ) {
        if (result.hasErrors()) {
            return "sign-in";
        }

        try {
            UUID sessionId = this.authenticationService.authenticate(signInRequestDto);
            Cookie cookie = new Cookie("sessionId", sessionId.toString());
            response.addCookie(cookie);
        } catch (NotFoundException e) {
            model.addAttribute("error", new ErrorResponseDto(e.getMessage()));
            return "sign-in";
        }

        return "redirect:/index";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpRequestDto", new SignUpRequestDto());

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

    @GetMapping("/sign-out")
    public String signOut(@CookieValue("sessionId") String sessionId,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) {
        try {
            this.sessionService.removeBy(UUID.fromString(sessionId));
            Cookie cookie = new Cookie("sessionId", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return "redirect:/sign-in";
        }
        catch (DatabaseOperationException e) {
            model.addAttribute("error", new ErrorResponseDto(e.getMessage()));
            return "redirect:" + request.getHeader("Referer");
        }
    }
}
