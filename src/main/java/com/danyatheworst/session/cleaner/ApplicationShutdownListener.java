package com.danyatheworst.session.cleaner;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final SessionCleaner sessionCleaner;

    public ApplicationShutdownListener(SessionCleaner sessionCleaner) {
        this.sessionCleaner = sessionCleaner;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.sessionCleaner.shutdown();
    }
}