package org.acme.integration;


import io.quarkiverse.cucumber.CucumberQuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import org.acme.config.KafkaTestResource;
import org.acme.config.RedisTestResource;


@QuarkusTestResource(KafkaTestResource.class)
@QuarkusTestResource(RedisTestResource.class)
public class RunCucumberTest extends CucumberQuarkusTest {
}