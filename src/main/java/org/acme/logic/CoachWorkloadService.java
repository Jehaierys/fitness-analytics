package org.acme.logic;

import jakarta.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.domain.UpdateCoachWorkloadRequest;
import org.acme.output.CoachWorkloadRepository;

import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CoachWorkloadService {

    private static final String COACH_WORKLOAD = "cchwld";

    private final CoachWorkloadRepository repository;


    public CoachWorkloadService(CoachWorkloadRepository repository) {
        this.repository = repository;
    }


    // todo: cut logs
    @Transactional
    public Uni<Void> update(UpdateCoachWorkloadRequest request) {
        final UUID transactionUuid = UUID.randomUUID();

        log.info(
                "Updating coach workload: coachId={}, year={}, month={}, delta={}, attempt's UUID: {}",
                request.coachId(),
                request.year(),
                request.month(),
                request.delta(),
                transactionUuid
        );

        return repository.incrementBy(
                        key(
                                request.coachId(),
                                request.year(),
                                request.month()
                        ),
                        request.delta()
                )
                .invoke(() -> log.info(
                        "Successfully updated coach workload: coachId={}, year={}, month={}, delta={}, process's UUID: {}",
                        request.coachId(),
                        request.year(),
                        request.month(),
                        request.delta(),
                        transactionUuid
                ))
                .onFailure().invoke(error -> log.error(
                        "Failed to update coach workload: coachId={}, year={}, month={}, delta={}, process's UUID: {}",
                        request.coachId(),
                        request.year(),
                        request.month(),
                        request.delta(),
                        transactionUuid,
                        error
                ));
    }

    public Uni<Integer> get(Long coachId, int year, int month) {
        return repository.get(key(coachId, year, month));
    }

    @Transactional
    public Uni<Boolean> delete(Long coachId, int year, int month) {
        final UUID transactionUuid = UUID.randomUUID();

        log.info(
                "Deleting coach workload: coachId={}, year={}, month={}, attempt's UUID: {}",
                coachId, year, month, transactionUuid
        );

        return repository.delete(key(coachId, year, month))
                .invoke(result -> log.info(
                        "Successfully deleted coach workload: coachId={}, year={}, month={}, result={}, process's UUID: {}",
                        coachId, year, month, result, transactionUuid
                ))
                .onFailure().invoke(error -> log.error(
                        "Failed to delete coach workload: coachId={}, year={}, month={}, process's UUID: {}",
                        coachId, year, month, transactionUuid, error
                ));
    }

    private String key(long id, int year, int month) {
        return COACH_WORKLOAD + ":%d:%d:%d".formatted(id, year, month);
    }
}