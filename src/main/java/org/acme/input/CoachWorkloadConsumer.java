package org.acme.input;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.UpdateCoachWorkloadRequest;
import org.acme.logic.CoachWorkloadService;
import org.eclipse.microprofile.reactive.messaging.Incoming;


@ApplicationScoped
public class CoachWorkloadConsumer {

    private final CoachWorkloadService service;

    @Inject
    public CoachWorkloadConsumer(CoachWorkloadService service) {
        this.service = service;
    }

    @Incoming("coach-workload")
    public void consume(UpdateCoachWorkloadRequest request) {

        service.update(request)
                .await().indefinitely();
    }
}