package org.tinyg.rest;

import org.jboss.logging.Logger;
import org.tinyg.exception.KnownProcessingException;
import org.tinyg.model.Transport;
import org.tinyg.service.TransportService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/transports")
public class TransportRest {

    private static final Logger LOGGER = Logger.getLogger(TransportRest.class);

    @Inject
    TransportService transportService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transport> transports() {
        try {
            return transportService.getAllTransports();
        } catch (KnownProcessingException exception) { //we'll catch it like a bunch of responsible devs
            LOGGER.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    @GET
    @Path("/valid")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getValidTransports() {
        return transportService.getValidTrackingIds();
    }

}