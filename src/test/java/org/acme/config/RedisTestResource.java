package org.acme.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class RedisTestResource implements RedisTestContainer, QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        REDIS.start();

        return Map.of(
                "quarkus.redis.hosts",
                "redis://" +
                        REDIS.getHost() +
                        ":" +
                        REDIS.getMappedPort(6379)
        );
    }

    @Override
    public void stop() {
        REDIS.stop();
    }
}