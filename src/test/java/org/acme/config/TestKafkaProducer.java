package org.acme.config;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class TestKafkaProducer implements AutoCloseable {

    private static final String TOPIC = "coach-workload";

    private final KafkaProducer<String, String> producer;

    public TestKafkaProducer() {
        Properties properties = new Properties();

        properties.put(
                "bootstrap.servers",
                KafkaTestContainer.KAFKA.getBootstrapServers()
        );
        properties.put(
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );
        properties.put(
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        this.producer = new KafkaProducer<>(properties);
    }

    public void send(String message) {
        try {
            producer.send(new ProducerRecord<>(TOPIC, message)).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAll(List<String> messages) {
        messages.forEach(this::send);
        producer.flush();
    }

    @Override
    public void close() {
        producer.close();
    }
}