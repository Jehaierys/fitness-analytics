package org.acme.output;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoachWorkloadRepository {

    private final ReactiveValueCommands<String, Integer> values;

    public CoachWorkloadRepository(ReactiveRedisDataSource dataSource) {
        this.values = dataSource.value(Integer.class);
    }

    public Uni<Integer> get(String key) {
        return values.get(key);
    }

    public Uni<Void> incrementBy(String key, long delta) {
        values.incrby(key, delta);
        return Uni.createFrom().voidItem();
    }

    public Uni<Boolean> delete(String key) {
        return values.getdel(key)
                .map(value -> value != null);
    }
}