package com.danyatheworst.location;

import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.exceptions.InternalServerException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepository {

    private final SessionFactory sessionFactory;

    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Location> findAllBy(Long userId) {
        try (var session = this.sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Location l WHERE l.user.id = :userId ORDER BY l.id DESC",
                            Location.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to fetch all locations of user with id " + userId + " from database"
            );
        }
    }

    public long save(Location location) {
        try (var session = this.sessionFactory.openSession()) {
            session.save(location);
            return location.getId();
        } catch (ConstraintViolationException e) {
            //TODO: deep check
            throw new EntityAlreadyExistsException("Location " + location.getName() + " is being tracked");
        } catch (HibernateException e) {
            throw new InternalServerException(
                    "Failed to save location with name " + location.getName() + " into database"
            );
        }
    }
}
