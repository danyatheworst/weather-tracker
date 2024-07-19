package com.danyatheworst;

import com.danyatheworst.exceptions.InvalidParameterException;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.CSession;
import com.danyatheworst.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
public class CookieInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    public CookieInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        String uri = request.getRequestURI();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    try {
                        UUID sessionId = this.fromString(cookie.getValue());
                        this.sessionService.updateExpirationTime(sessionId);
                        CSession session = this.sessionService.findBy(sessionId);
                        Cookie newCookie = new Cookie("sessionId", cookie.getValue());
                        newCookie.setMaxAge(this.getCookieMaxAge(session.getExpiresAt()));
                        response.addCookie(newCookie);
                        request.setAttribute("user", session.getUser());
                        if (this.isSigningPage(uri)) {
                            response.sendRedirect("/index");
                            return false;
                        }
                        return true;
                    } catch (InvalidParameterException | NotFoundException e) {
                        if (this.isSigningPage(uri)) {
                            return true;
                        }

                        // Redirect if sessionId was faked and a user is not on a sign-in/sign-up page
                        response.sendRedirect("/sign-in");
                        return false;
                    }
                }
            }
        }
        if (this.isSigningPage(uri)) {
            return true;
        }
        response.sendRedirect("/sign-in"); // Redirect if sessionId is not present in db
        return false;
    }

    private int getCookieMaxAge(LocalDateTime expirationTime) {
        Duration diff = Duration.between(expirationTime, LocalDateTime.now());
        return (int)  diff.getSeconds();
    }

    private UUID fromString(String string) {
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    private boolean isSigningPage(String uri) {
        return Objects.equals(uri, "/sign-in") || Objects.equals(uri, "/sign-up");
    }
}
