package org.acme.logic;

import jakarta.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import org.acme.domain.UpdateCoachWorkloadRequest;
import org.acme.output.CoachWorkloadRepository;

@ApplicationScoped
public class CoachWorkloadService {

    private static final String COACH_WORKLOAD = "cchwld";

    private final CoachWorkloadRepository repository;


    public CoachWorkloadService(CoachWorkloadRepository repository) {
        this.repository = repository;
    }


    public Uni<Void> update(UpdateCoachWorkloadRequest request) {
        return repository.incrementBy(
                key(request.coachId(), request.year(), request.month()),
                request.delta()
        ).replaceWithVoid();
    }

    public Uni<Integer> get(Long coachId, int year, int month) {
        return repository.get(key(coachId, year, month));
    }

    public Uni<Boolean> delete(Long coachId, int year, int month) {
        return repository.delete(key(coachId, year, month));
    }

    private String key(long id, int year, int month) {
        return COACH_WORKLOAD + ":%d:%d:%d".formatted(id, year, month);
    }
}