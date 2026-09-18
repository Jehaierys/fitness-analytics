package org.acme.integration.steps;


import jakarta.inject.Inject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import org.acme.config.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class CoachWorkloadSteps {

    private static final String TOPIC = "coach-workload";

    @Inject
    ReactiveRedisDataSource redis;

    private TestKafkaProducer kafkaProducer;

    @Given("Kafka and Redis test containers are running")
    public void containersAreRunning() {
        assertTrue(KafkaTestContainer.KAFKA.isRunning());
        assertTrue(RedisTestContainer.REDIS.isRunning());
    }

    @When("I send 5 coach workload updates to Kafka")
    public void sendUpdates() {

        kafkaProducer = new TestKafkaProducer();

        kafkaProducer.sendAll(List.of(
                """
                {"coachId":1,"year":2026,"month":9,"delta":10}
                """,
                """
                {"coachId":2,"year":2026,"month":9,"delta":20}
                """,
                """
                {"coachId":1,"year":2026,"month":9,"delta":5}
                """,
                """
                {"coachId":3,"year":2026,"month":10,"delta":7}
                """,
                """
                {"coachId":1,"year":2026,"month":10,"delta":3}
                """
        ));
    }

    @Then("Redis should contain the expected coach workloads")
    public void verifyRedis() {System.out.println(
            redis.key().keys("*")
                    .await()
                    .atMost(Duration.ofSeconds(5))
    );

        assertRedisValue("cchwld:1:2026:9", 15);
        assertRedisValue("cchwld:2:2026:9", 20);
        assertRedisValue("cchwld:3:2026:10", 7);
        assertRedisValue("cchwld:1:2026:10", 3);
    }

    private void assertRedisValue(String key, int expected) {
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    Integer actual = redis.value(Integer.class)
                            .get(key)
                            .await()
                            .atMost(Duration.ofSeconds(1));

                    assertEquals(expected, actual,
                            "Unexpected value for Redis key: " + key);
                });
    }
}