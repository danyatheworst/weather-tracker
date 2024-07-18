package com.danyatheworst.session;

import com.danyatheworst.exceptions.DatabaseOperationException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {

    private final SessionFactory sessionFactory;

    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public CSession save(CSession cSession) {
        try (Session session = this.sessionFactory.openSession()) {
            session.save(cSession);
            return cSession;
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to save session with user login " + cSession.getUser().getLogin() + " into database"
            );
        }
    }
}
