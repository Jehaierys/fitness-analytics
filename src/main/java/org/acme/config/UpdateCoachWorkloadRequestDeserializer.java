package org.acme.config;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.domain.UpdateCoachWorkloadRequest;

public class UpdateCoachWorkloadRequestDeserializer extends ObjectMapperDeserializer<UpdateCoachWorkloadRequest> {
    public UpdateCoachWorkloadRequestDeserializer() {
        super(UpdateCoachWorkloadRequest.class);
    }
}