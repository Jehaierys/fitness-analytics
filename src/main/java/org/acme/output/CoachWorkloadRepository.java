package org.acme.output;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class CoachWorkloadRepository {

    private final ReactiveValueCommands<String, Integer> values;

    public CoachWorkloadRepository(ReactiveRedisDataSource dataSource) {
        this.values = dataSource.value(Integer.class);
    }

    @CircuitBreaker(
            requestVolumeThreshold = 5,
            failureRatio = 0.5,
            delay = 10,
            successThreshold = 2
    )
    public Uni<Integer> get(String key) {
        return values.get(key);
    }

    @CircuitBreaker(
            requestVolumeThreshold = 5,
            failureRatio = 0.5,
            delay = 10,
            successThreshold = 2
    )
    public Uni<Void> incrementBy(String key, long delta) {
        return values.incrby(key, delta)
                .replaceWithVoid();
    }

    @CircuitBreaker(
            requestVolumeThreshold = 5,
            failureRatio = 0.5,
            delay = 10,
            successThreshold = 2
    )
    public Uni<Boolean> delete(String key) {
        return values.getdel(key)
                .map(value -> value != null);
    }
}