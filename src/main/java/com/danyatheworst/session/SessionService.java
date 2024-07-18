package com.danyatheworst.session;

import com.danyatheworst.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private static final int sessionAliveHours = 72;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public CSession create(User user) {
        CSession CSession = new CSession(user, LocalDateTime.now().plusHours(sessionAliveHours));
        return this.sessionRepository.save(CSession);

    }
}
