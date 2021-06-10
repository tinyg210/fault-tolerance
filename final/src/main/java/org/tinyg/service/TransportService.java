package org.tinyg.service;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.tinyg.dao.TransportRepo;
import org.tinyg.model.Transport;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransportService {

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

}
