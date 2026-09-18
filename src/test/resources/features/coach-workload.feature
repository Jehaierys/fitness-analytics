Feature: Coach workload processing

  Scenario: Process coach workload updates from Kafka and store them in Redis
    Given Kafka and Redis test containers are running
    When I send 5 coach workload updates to Kafka
    Then Redis should contain the expected coach workloads