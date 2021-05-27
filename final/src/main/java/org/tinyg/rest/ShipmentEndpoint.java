package org.tinyg.rest;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;
import org.tinyg.exception.UnexpectedProcessingException;
import org.tinyg.model.Shipment;
import org.tinyg.service.ShipmentRepoService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.http.HTTPException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Path("/shipments")
public class ShipmentEndpoint {

    private static final Logger LOGGER = Logger.getLogger(ShipmentEndpoint.class);

    @Inject
    ShipmentRepoService shipmentRepoService;

    private AtomicLong counter = new AtomicLong(0);


    @GET
    @Retry(
            retryOn = HTTPException.class, // or maybe a specific http exception
            maxRetries = 5,
            abortOn = UnexpectedProcessingException.class, // could also be http status codes that you generally don't retry, some 4xx, 5xx, 3xx, etc
            delay = 500
    )
    @Produces(MediaType.APPLICATION_JSON)
    public List<Shipment> shipments() {
        try {
            extraInformationProcessing();
            LOGGER.info("Information fetched successfully.");
            return shipmentRepoService.getAllShipments();
        } catch (UnexpectedProcessingException exception) {
            LOGGER.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    @GET
    @Path("/valid")
    @Produces(MediaType.TEXT_PLAIN)
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 3000
    )
    public List<String> getValidShipments() {
        final Long invocationNumber = counter.getAndIncrement();

        try {
            maybeFail();
            LOGGER.infof("#%d invocation returning successfully.Shipments that are still out in transit are being displayed", invocationNumber);
            return shipmentRepoService.getValidTrackingIds();
        } catch (RuntimeException e) {
            String message = e.getClass().getSimpleName() + ": " + e.getMessage();
            LOGGER.errorf("Can not fetch shipments that are still in transit.Invocation #%d failed: %s", invocationNumber, message);
            return Collections.emptyList();
        }

    }

    private void maybeFail() {
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }

    private void extraInformationProcessing() {
        if (new Random().nextInt(50) % 2 == 0) {
            LOGGER.error("The required information could not be retrieved right now.Try again.");
            throw new HTTPException(408);  //408 request timeout
        }
        if (40 <= new Random().nextInt(50)) {
            LOGGER.error(String.format("Something went wrong here at %s", LOGGER.getName()));
            throw new UnexpectedProcessingException();
        }
    }

}