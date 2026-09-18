package org.acme.config;

import com.redis.testcontainers.RedisContainer;

public interface RedisTestContainer {

    public static final RedisContainer REDIS = new RedisContainer("redis:7-alpine");
}
