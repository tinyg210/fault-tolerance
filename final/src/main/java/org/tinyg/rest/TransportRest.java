package org.tinyg.rest;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.jboss.logging.Logger;
import org.tinyg.exception.KnownProcessingException;
import org.tinyg.model.Transport;
import org.tinyg.service.TransportService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Path("/transports")
public class TransportRest {

    private static final Logger LOGGER = Logger.getLogger(TransportRest.class);

    @Inject
    TransportService transportService;


    @GET
    @Retry(
            retryOn = HTTPException.class, // or maybe a specific http exception
            maxRetries = 5,
            abortOn = KnownProcessingException.class, // could also be http status codes that you generally don't retry, some 4xx, 5xx, 3xx, etc
            delay = 500
    )
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transport> transports() {
        try {
            extraInformationProcessing();
            LOGGER.info("Information fetched successfully.");
            return transportService.getAllTransports();
        } catch (KnownProcessingException exception) {
            LOGGER.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    @GET
    @Path("/valid")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getValidTransports() {
        try {
            return transportService.getValidTrackingIds();
        } catch (CircuitBreakerOpenException cbe) {
            LOGGER.errorf("Circuit breaker is open.");
            return Collections.emptyList();
        }
    }

    private void extraInformationProcessing() {
        if (new Random().nextInt(50) % 2 == 0) {
            LOGGER.error("The required information could not be retrieved right now.Try again.");
            throw new HTTPException(408);  //408 request timeout
        }
        if (40 <= new Random().nextInt(50)) {
            LOGGER.error(String.format("Something went wrong here at %s", LOGGER.getName()));
            throw new KnownProcessingException();
        }
    }

}