package com.danyatheworst.user;

import com.danyatheworst.exceptions.DatabaseOperationException;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> findBy(String login) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);
            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to fetch user with login " + login + " from the database"
            );
        }
    }

    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.save(user);
        } catch (ConstraintViolationException e) {
            throw new EntityAlreadyExistsException("That username is taken. Try another");
        } catch (HibernateException e) {
            throw new DatabaseOperationException(
                    "Failed to save user with login " + user.getLogin() + " into database"
            );
        }
    }
}
