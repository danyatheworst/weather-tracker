package com.danyatheworst.user;

import com.danyatheworst.exceptions.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User findBy(String login) {
        return this.userRepository
                .findBy(login)
                .orElseThrow(() -> new NotFoundException("The username and/or password you specified are not correct"));
    }

    public void create(SignUpRequestDto signUpRequestDto) {
        String encryptedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        this.userRepository.save(new User(signUpRequestDto.getLogin(), encryptedPassword));
    }
}
