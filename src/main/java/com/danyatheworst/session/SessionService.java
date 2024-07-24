package com.danyatheworst.session;

import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Value("${session_expiration_seconds}")
    private int sessionExpiration;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public CSession findBy(UUID sessionId) {
        return this.sessionRepository.findBy(sessionId).orElseThrow(() ->
                new NotFoundException("Session with " + sessionId + " is not found"));
    }

    public UUID create(User user) {
        CSession CSession = new CSession(user, this.getExpirationTime());
        return this.sessionRepository.save(CSession);
    }

    public void checkExpiration(CSession session) {
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("Session is expired");
        }
    }

    public void updateExpirationTime(UUID sessionId) {
        this.sessionRepository.update(sessionId, this.getExpirationTime());
    }

    public void removeBy(UUID sessionId) {
        this.sessionRepository.removeBy(sessionId);
    }

    private LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plusSeconds(this.sessionExpiration);
    }
}
