package org.tinyg.rest;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.tinyg.service.EmissionsCalculatorService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Path("/emissions")
public class EmissionsCalculatorEndpoint {

    @Inject
    EmissionsCalculatorService emissionsCalculatorService;

    private static final Logger LOGGER = Logger.getLogger(EmissionsCalculatorEndpoint.class);

    AtomicLong attempts = new AtomicLong(1);

    @GET
    @Path("/{trackingId}")
    @Timeout(2500)
    public String calculateEmissions(@PathParam String trackingId) {
        long started = System.currentTimeMillis();
        final long invocation = attempts.getAndIncrement();

        try {

            delay();
            LOGGER.infof("EmissionsCalculatorService invocation #%d returning successfully", invocation);
            return emissionsCalculatorService.calculate(trackingId);

        } catch (InterruptedException e) {
            LOGGER.errorf("EmissionsCalculatorService invocation #%d timed out after %d ms",
                    invocation, System.currentTimeMillis() - started);
            return null;
        }
    }

    private void delay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(5000));
    }
}

