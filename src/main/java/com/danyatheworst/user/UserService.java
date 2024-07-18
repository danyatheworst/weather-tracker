package com.danyatheworst.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void create(SignUpRequestDto signUpRequestDto) {
        String encryptedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        this.userRepository.save(new User(signUpRequestDto.getLogin(), encryptedPassword));
    }
}
