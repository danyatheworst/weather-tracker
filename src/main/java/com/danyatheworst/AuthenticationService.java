package com.danyatheworst;

import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.SessionService;
import com.danyatheworst.user.SignInRequestDto;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    public AuthenticationService(BCryptPasswordEncoder passwordEncoder,
                                 UserRepository userRepository,
                                 SessionService sessionService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    public UUID authenticate(SignInRequestDto signInRequestDto) {
        User user = this.userRepository
                .findBy(signInRequestDto.getLogin())
                .orElseThrow(() -> new NotFoundException("The username and/or password you specified are not correct"));

        if (!this.passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new NotFoundException("The username and/or password you specified are not correct");
        };

        return this.sessionService.create(user);
    }
}
