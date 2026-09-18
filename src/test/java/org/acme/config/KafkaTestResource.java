package org.acme.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class KafkaTestResource implements KafkaTestContainer, QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        KAFKA.start();

        return Map.of(
                "mp.messaging.incoming.coach-workload.connector",
                "smallrye-kafka",

                "mp.messaging.incoming.coach-workload.topic",
                TOPIC,

                "mp.messaging.incoming.coach-workload.bootstrap.servers",
                KAFKA.getBootstrapServers(),

                "mp.messaging.incoming.coach-workload.group.id",
                "analytics-test",

                "mp.messaging.incoming.coach-workload.auto.offset.reset",
                "earliest",

                "mp.messaging.incoming.coach-workload.value.deserializer",
                "org.acme.config.UpdateCoachWorkloadRequestDeserializer",

                "mp.messaging.incoming.coach-workload.value.deserializer.type",
                "org.acme.domain.UpdateCoachWorkloadRequest"
        );
    }

    @Override
    public void stop() {
        KAFKA.stop();
    }
}