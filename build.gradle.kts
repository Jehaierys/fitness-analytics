plugins {
    java
    id("io.quarkus") version "3.38.0"
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Quarkus BOM
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:3.38.0"))

    // Quarkus
    implementation("io.quarkus:quarkus-mutiny")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-messaging-kafka")
    implementation("io.quarkus:quarkus-redis-client")
    implementation("io.quarkus:quarkus-micrometer")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    implementation("io.quarkus:quarkus-arc")


    // Stork
    implementation("io.quarkus:quarkus-smallrye-stork")
    implementation("io.smallrye.stork:stork-service-registration-eureka:3.0.0")
    implementation("io.smallrye.stork:stork-service-discovery-eureka:3.0.0")

    // Tests
    testImplementation("io.quarkus:quarkus-junit")

    testImplementation("io.rest-assured:rest-assured:6.0.0") {
        exclude(module = "activation")
        exclude(module = "javax.activation-api")
        exclude(module = "jakarta.activation-api")
        exclude(module = "jaxb-osgi")
        exclude(module = "commons-logging")
    }

    testImplementation("org.junit.platform:junit-platform-suite")

    testImplementation("io.quarkiverse.cucumber:quarkus-cucumber:1.3.0")
    testImplementation("io.cucumber:cucumber-java:7.29.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.29.0")

    testImplementation("org.testcontainers:testcontainers:1.21.3") {
        exclude(module = "hamcrest-core")
    }

    testImplementation("org.testcontainers:kafka:1.21.3")
    testImplementation("com.redis:testcontainers-redis:2.2.4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}