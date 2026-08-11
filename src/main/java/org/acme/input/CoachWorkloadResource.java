package org.acme.input;

import jakarta.ws.rs.*;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.UpdateCoachWorkloadRequest;
import org.acme.logic.CoachWorkloadService;

@Path("/coach/{id}/workload/{year}/{month}")
@Produces(MediaType.APPLICATION_JSON)
public class CoachWorkloadResource {

    private final CoachWorkloadService service;


    public CoachWorkloadResource(CoachWorkloadService service) {
        this.service = service;
    }


    @GET
    public Uni<Integer> get(@PathParam("id") Long id,
                            @PathParam("year") int year,
                            @PathParam("month") int month) {

        return service.get(id, year, month);
    }

    @PATCH
    public Uni<Void> update(@PathParam("id") Long id,
                            @PathParam("year") int year,
                            @PathParam("month") int month,
                            @QueryParam("delta") int delta) {

        final UpdateCoachWorkloadRequest request =
                new UpdateCoachWorkloadRequest(id, year, month, delta);

        return service.update(request);
    }

    @DELETE
    public Uni<Boolean> delete(@PathParam("id") Long id,
                               @PathParam("year") int year,
                               @PathParam("month") int month) {

        return service.delete(id, year, month);
    }
}