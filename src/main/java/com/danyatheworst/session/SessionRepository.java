package com.danyatheworst.session;

import com.danyatheworst.exceptions.InternalServerException;
import com.danyatheworst.exceptions.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository {

    private final SessionFactory sessionFactory;

    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Session> findBy(UUID sessionId) {
        try (var session = this.sessionFactory.openSession()) {
            Query<Session> query = session.createQuery("FROM Session s WHERE s.id = :sessionId", Session.class);
            query.setParameter("sessionId", sessionId);
            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to update session expiration time in database"
            );
        }
    }

    public UUID save(Session session) {
        try (var s = this.sessionFactory.openSession()) {
            s.save(session);
            return session.getId();
        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to save session with user login " + session.getUser().getLogin() + " into database"
            );
        }
    }

    public void update(UUID sessionId, LocalDateTime expirationTime) {
        try (var session = this.sessionFactory.openSession()) {
            //hibernate makes us to use transactions with createMutationQuery;
            session.beginTransaction();
            int updatedEntities = session.createMutationQuery(
                            "UPDATE Session s SET s.expiresAt = :expirationTime WHERE s.id = :sessionId"
                    )
                    .setParameter("expirationTime", expirationTime)
                    .setParameter("sessionId", sessionId)
                    .executeUpdate();
            session.getTransaction().commit();

            if (updatedEntities == 0) {
                throw new NotFoundException("Session with id " + sessionId + " is not present in db");
            }

        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to update session expiration time in database"
            );
        }
    }

    public void removeBy(UUID sessionId) {
        try (var session = this.sessionFactory.openSession()) {
            //hibernate makes us to use transactions with createMutationQuery;
            session.beginTransaction();
            String hql = "DELETE FROM Session WHERE id = :sessionId";
            session.createMutationQuery(hql).setParameter("sessionId", sessionId).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to update session expiration time in database"
            );
        }
    }
}
