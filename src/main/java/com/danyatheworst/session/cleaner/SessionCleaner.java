package com.danyatheworst.session.cleaner;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SessionCleaner {
    private final SessionFactory sessionFactory;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SessionCleaner(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.startSessionCleanupTask();
    }

    private void startSessionCleanupTask() {
        long initialDelay = 0;
        long period = 15;

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                deleteExpiredSessions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    private void deleteExpiredSessions() {
        try (var session = this.sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "DELETE FROM Session s WHERE s.expiresAt < CURRENT_TIMESTAMP";
            int rowsDeleted = session.createMutationQuery(hql).executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " expired sessions");

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.scheduler.shutdown();
    }
}