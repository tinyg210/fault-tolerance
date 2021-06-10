package org.tinyg.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Transport {

    private Long id;

    private String trackingId;
    private Set<Container> containers;

    private LocalDate departureDate;
    private LocalDate arrivalDate;

    public Transport(final Long id, final String trackingId, final LocalDate departureDate, final LocalDate arrivalDate) {
        this.containers = new HashSet<>();
        this.id = id;
        this.trackingId = trackingId;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public void addContainer(final Container container) {
        containers.add(container);
    }

}
