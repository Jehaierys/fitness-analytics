package org.acme.logic;

import jakarta.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import org.acme.domain.UpdateCoachWorkloadRequest;
import org.acme.output.CoachWorkloadRepository;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class CoachWorkloadService {

    private static final String COACH_WORKLOAD = "cchwld";

    private static final Logger log = Logger.getLogger(CoachWorkloadService.class.getName());


    private final CoachWorkloadRepository repository;


    public CoachWorkloadService(CoachWorkloadRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Uni<Void> update(UpdateCoachWorkloadRequest request) {
        final UUID transactionUuid = UUID.randomUUID();

        logUpdating(request, transactionUuid);

        return repository.incrementBy(
                        key(
                                request.coachId(),
                                request.year(),
                                request.month()
                        ),
                        request.delta()
                )
                .invoke(() -> logSuccessfullyUpdated(request, transactionUuid))
                .onFailure().invoke(error -> logUpdateFailure(request, transactionUuid, error));
    }

    private void logUpdating(
            UpdateCoachWorkloadRequest request,
            UUID transactionUuid
    ) {
        log.info(String.format(
                "Updating coach workload: coachId=%d, year=%d, month=%d, delta=%d, attempt's UUID=%s",
                request.coachId(),
                request.year(),
                request.month(),
                request.delta(),
                transactionUuid
        ));
    }

    private void logSuccessfullyUpdated(
            UpdateCoachWorkloadRequest request,
            UUID transactionUuid
    ) {
        log.info(String.format(
                "Successfully updated coach workload: coachId=%d, year=%d, month=%d, delta=%d, process's UUID=%s",
                request.coachId(),
                request.year(),
                request.month(),
                request.delta(),
                transactionUuid
        ));
    }

    private void logUpdateFailure(
            UpdateCoachWorkloadRequest request,
            UUID transactionUuid,
            Throwable error
    ) {
        log.log(
                Level.SEVERE,
                String.format(
                        "Failed to update coach workload: coachId=%d, year=%d, month=%d, delta=%d, process's UUID=%s",
                        request.coachId(),
                        request.year(),
                        request.month(),
                        request.delta(),
                        transactionUuid
                ),
                error
        );
    }

    public Uni<Integer> get(Long coachId, int year, int month) {
        return repository.get(key(coachId, year, month));
    }

    @Transactional
    public Uni<Boolean> delete(Long coachId, int year, int month) {
        final UUID transactionUuid = UUID.randomUUID();

        log.info(
                "Deleting coach workload: coachId={}, year={}, month={}, attempt's UUID: {}"
        );

        return repository.delete(key(coachId, year, month))
                .invoke(result -> log.info(
                        "Successfully deleted coach workload: coachId={}, year={}, month={}, result={}, process's UUID: {}"
                ))
                .onFailure().invoke(error -> log.info(
                        "Failed to delete coach workload: coachId={}, year={}, month={}, process's UUID: {}"
                ));
    }

    private String key(long id, int year, int month) {
        return COACH_WORKLOAD + ":%d:%d:%d".formatted(id, year, month);
    }
}