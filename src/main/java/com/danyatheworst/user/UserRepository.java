package com.danyatheworst.user;

import com.danyatheworst.exceptions.DatabaseOperationException;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;


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
        try (Session session = sessionFactory.openSession()) {
            int a = 123;
            session.save(user);
            return user;
        } catch (ConstraintViolationException e) {
            throw new EntityAlreadyExistsException("That username is taken. Try another");
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to save player with login " + user.getLogin() + " into database"
            );
        }
    }
}
