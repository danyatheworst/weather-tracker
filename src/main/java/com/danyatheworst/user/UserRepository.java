package com.danyatheworst.user;

import com.danyatheworst.exceptions.DatabaseOperationException;
import com.danyatheworst.exceptions.EntityAlreadyExistException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User save(User user) {
//        try (Session session = this.sessionFactory.getCurrentSession()) {
//            session.persist(user);
//            return user;
//        } catch (HibernateException e) {
//            if (isUniqueConstraintViolation((ConstraintViolationException) e)) {
//                throw new EntityAlreadyExistException("That username is taken. Try another");
//            }
//            throw new DatabaseOperationException(
//                    "Failed to save player with login + " + user.getLogin() + " into database"
//            );
//        }
        return new User();
    }

    private boolean isUniqueConstraintViolation(ConstraintViolationException e) {
        return e.getSQLState().startsWith("23"); // SQLState 23xxx indicates constraint violations
    }
}
