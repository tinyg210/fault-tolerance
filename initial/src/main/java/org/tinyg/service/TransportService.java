package org.tinyg.service;

import org.jboss.logging.Logger;
import org.tinyg.dao.TransportRepo;
import org.tinyg.exception.KnownProcessingException;
import org.tinyg.model.Transport;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.ws.http.HTTPException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransportService {

    private static final Logger LOGGER = Logger.getLogger(TransportService.class);
    private static final Random RANDOM = new Random();

    private TransportRepo transports;

    private AtomicLong counter = new AtomicLong(0);

    public TransportService() {
        transports = new TransportRepo();
    }

    public List<Transport> getAllTransports() {
        extraValidationOrProcessing();
        LOGGER.info("Information fetched successfully.");
        return new ArrayList<>(transports.values());
    }

    public List<String> getValidTrackingIds() {
        possibleFail();
        return transports.values().stream().filter(transport ->
                LocalDate.now().compareTo(transport.getArrivalDate()) <= 0).map(Transport::getTrackingId).collect(Collectors.toList());
    }

    private void possibleFail() {
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }

    private void extraValidationOrProcessing() {
        // let's pretend there's a call to a different user service to check status/association with transports'
        // companies, etc
        if (RANDOM.nextInt(50) % 2 == 0) {
            LOGGER.error("The required information could not be retrieved right now.Try again.");
            throw new HTTPException(408);  //408 request timeout
        }
        if (40 <= RANDOM.nextInt(50)) {
            LOGGER.error(String.format("Something went wrong here at %s", LOGGER.getName()));
            throw new KnownProcessingException();
        }
    }

}
