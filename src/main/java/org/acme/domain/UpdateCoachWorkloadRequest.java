package org.acme.domain;

public record UpdateCoachWorkloadRequest(
    Long coachId,
    int year,
    int month,
    // minutes
    int delta
) {
}