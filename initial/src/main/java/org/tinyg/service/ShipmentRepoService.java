package org.tinyg.service;

import org.tinyg.model.Container;
import org.tinyg.model.ContainerLoad;
import org.tinyg.model.ContainerType;
import org.tinyg.model.Shipment;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShipmentRepoService {

    private HashMap<String, Shipment> shipments;

    public ShipmentRepoService() {
        shipments = new HashMap<>();

        Shipment shipment1 = new Shipment(1000L, "KG545478", LocalDate.of(2021, 5, 21), LocalDate.of(2021, 5, 25));
        Container container11 = new Container(1001L, ContainerType.FULL_LOAD, "CU-87635808654");
        container11.addContainerLoad(new ContainerLoad(1002L, 6000.0, 5.0, 7.0));
        shipment1.addContainer(container11);

        Shipment shipment2 = new Shipment(1003L, "JT565689", LocalDate.of(2021, 5, 12), LocalDate.of(2021, 12, 1));
        Container container21 = new Container(1004L, ContainerType.PARTIAL_LOAD, "TV-88893218654");
        container21.addContainerLoad(new ContainerLoad(1005L, 4000.0, 3.0, 4.0));
        container21.addContainerLoad(new ContainerLoad(1006L, 2000.0, 4.0, 8.0));
        Container container22 = new Container(1007L, ContainerType.PARTIAL_LOAD, "CT-96430008541");
        container22.addContainerLoad(new ContainerLoad(1008L, 5000.0, 2.0, 4.0));
        container22.addContainerLoad(new ContainerLoad(1009L, 3000.0, 5.0, 9.0));
        shipment2.addContainer(container21);
        shipment2.addContainer(container22);

        shipments.put(shipment1.getTrackingId(), shipment1);
        shipments.put(shipment2.getTrackingId(), shipment2);
    }

    public List<Shipment> getAllShipments() {
        return new ArrayList<>(shipments.values());
    }

    public List<String> getValidTrackingIds() {
        return shipments.values().stream().filter(shipment ->
                LocalDate.now().compareTo(shipment.getArrivalDate()) <= 0).map(Shipment::getTrackingId).collect(Collectors.toList());
    }

}
