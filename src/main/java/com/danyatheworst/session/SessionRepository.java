package com.danyatheworst.session;

import com.danyatheworst.exceptions.DatabaseOperationException;
import com.danyatheworst.exceptions.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
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

    public Optional<CSession> findBy(UUID sessionId) {
        try (Session session = this.sessionFactory.openSession()) {
            Query<CSession> query = session.createQuery("FROM CSession s WHERE s.id = :sessionId", CSession.class);
            query.setParameter("sessionId", sessionId);
            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to update session expiration time in database"
            );
        }
    }

    public UUID save(CSession cSession) {
        try (Session session = this.sessionFactory.openSession()) {
            session.save(cSession);
            return cSession.getId();
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to save session with user login " + cSession.getUser().getLogin() + " into database"
            );
        }
    }

    public void update(UUID sessionId, LocalDateTime expirationTime) {
        try (Session session = this.sessionFactory.openSession()) {
            //hibernate makes us to use transactions with createMutationQuery;
            session.beginTransaction();
            int updatedEntities = session.createMutationQuery(
                            "UPDATE CSession s SET s.expiresAt = :expirationTime WHERE s.id = :sessionId"
                    )
                    .setParameter("expirationTime", expirationTime)
                    .setParameter("sessionId", sessionId)
                    .executeUpdate();
            session.getTransaction().commit();

            if (updatedEntities == 0) {
                throw new NotFoundException("Session with id " + sessionId + " is not present in db");
            }

        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to update session expiration time in database"
            );
        }
    }

    public void removeBy(UUID sessionId) {
        try (Session session = this.sessionFactory.openSession()) {
            //hibernate makes us to use transactions with createMutationQuery;
            session.beginTransaction();
            String hql = "DELETE FROM CSession WHERE id = :sessionId";
            session.createMutationQuery(hql).setParameter("sessionId", sessionId).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to update session expiration time in database"
            );
        }
    }
}
