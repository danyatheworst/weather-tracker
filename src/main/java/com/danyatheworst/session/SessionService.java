package com.danyatheworst.session;

import com.danyatheworst.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<CSession> findAll() {
        return this.sessionRepository.findAll();
    }

    public CSession findBy(UUID sessionId) {
        return this.sessionRepository.findBy(sessionId);
    }

    public UUID create(User user) {
        CSession CSession = new CSession(user, this.getExpirationTime());
        return this.sessionRepository.save(CSession);
    }

    public void updateExpirationTime(UUID sessionId) {
        this.sessionRepository.update(sessionId, this.getExpirationTime());
    }

    private LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plusHours(72);
    }
}
