package org.acme.config;

import org.testcontainers.kafka.KafkaContainer;

public interface KafkaTestContainer {


    public static final String TOPIC = "coach-workload";

    public static final KafkaContainer KAFKA = new KafkaContainer("apache/kafka:4.0.0");
}
