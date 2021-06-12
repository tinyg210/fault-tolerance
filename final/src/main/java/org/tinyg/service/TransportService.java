package org.tinyg.service;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.jboss.logging.Logger;
import org.tinyg.dao.TransportRepo;
import org.tinyg.model.Transport;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransportService {

    private static final Logger LOGGER = Logger.getLogger(TransportService.class);

    private TransportRepo transports;

    private AtomicLong counter = new AtomicLong(0);

    public TransportService() {
        transports = new TransportRepo();
    }

    public List<Transport> getAllTransports() {
        return new ArrayList<>(transports.values());
    }

    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 1 / 2,
            delay = 10000
    )
    public List<String> getValidTrackingIds() {
        final Long invocationNumber = counter.getAndIncrement();
        try {
            possibleFail(invocationNumber);
            LOGGER.infof("#%d invocation returning successfully.Transports that are still out in transit are being " +
                    "displayed", invocationNumber);
            return transports.values().stream().filter(transport ->
                    LocalDate.now().compareTo(transport.getArrivalDate()) <= 0).map(Transport::getTrackingId).collect(Collectors.toList());
        } catch (RuntimeException e) {
            String message = e.getClass().getSimpleName() + ": " + e.getMessage();
            LOGGER.errorf("Can not fetch transports that are still in transit.Invocation #%d failed: %s",
                    invocationNumber, message);
            return Collections.emptyList();
        }
    }

    private void possibleFail(final Long invocationNumber) {
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }

}
