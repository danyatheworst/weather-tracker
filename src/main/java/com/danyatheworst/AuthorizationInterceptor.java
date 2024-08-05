package com.danyatheworst;

import com.danyatheworst.exceptions.InvalidParameterException;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.Session;
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
import java.util.Set;
import java.util.UUID;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private static final Set<String> publicURIs = Set.of("/", "/sign-in", "/sign-up");
    private final SessionService sessionService;

    public AuthorizationInterceptor(SessionService sessionService) {
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
                        UUID sessionId = fromString(cookie.getValue());
                        Session session = this.sessionService.findBy(sessionId);
                        this.sessionService.checkExpiration(session);
                        this.sessionService.updateExpirationTime(sessionId);
                        Cookie newCookie = new Cookie("sessionId", cookie.getValue());
                        newCookie.setMaxAge(getCookieMaxAge(session.getExpiresAt()));
                        response.addCookie(newCookie);
                        request.setAttribute("user", session.getUser());
                        if (isSigningURI(uri)) {
                            response.sendRedirect("/");
                            return false;
                        }
                        return true;
                    } catch (InvalidParameterException | NotFoundException e) {
                        if (isSigningURI(uri)) {
                            return true;
                        }
                        if (isPrivateURI(uri)) {
                            response.sendRedirect(getSignInRedirectURI(request));
                            return false;
                        }
                    }
                }
            }
        }
        if (isPrivateURI(uri)) {
            response.sendRedirect(getSignInRedirectURI(request));
            return false;
        }
        return true;
    }

    private static int getCookieMaxAge(LocalDateTime expirationTime) {
        Duration diff = Duration.between(expirationTime, LocalDateTime.now());
        return Math.abs((int) diff.getSeconds());
    }

    private static UUID fromString(String string) {
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    private static boolean isSigningURI(String uri) {
        return Objects.equals(uri, "/sign-in") || Objects.equals(uri, "/sign-up");
    }

    private static boolean isPrivateURI(String url) {
        return !publicURIs.contains(url);
    }

    private static String getSignInRedirectURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String qs = request.getQueryString();
        if (qs == null) {
            return "sign-in?redirect_to=" + uri;
        }
        return "sign-in?redirect_to=" + uri + "?" + qs;
    }
}
