package org.tinyg.dao;

import org.tinyg.model.Container;
import org.tinyg.model.ContainerLoad;
import org.tinyg.model.ContainerType;
import org.tinyg.model.Transport;

import java.time.LocalDate;
import java.util.HashMap;

public class TransportRepo extends HashMap<String, Transport> {

    public TransportRepo() {
        super();

        Transport transport1 = new Transport(1000L, "KG545478", LocalDate.of(2021, 5, 21), LocalDate.of(2021, 5, 25));
        Container container11 = new Container(1001L, ContainerType.FULL_LOAD, "CU-87635808654");
        container11.addContainerLoad(new ContainerLoad(1002L, 6000.0, 5.0, 7.0));
        transport1.addContainer(container11);

        Transport transport2 = new Transport(1003L, "JT565689", LocalDate.of(2021, 5, 12), LocalDate.of(2021, 12, 1));
        Container container21 = new Container(1004L, ContainerType.PARTIAL_LOAD, "TV-88893218654");
        container21.addContainerLoad(new ContainerLoad(1005L, 4000.0, 3.0, 4.0));
        container21.addContainerLoad(new ContainerLoad(1006L, 2000.0, 4.0, 8.0));
        Container container22 = new Container(1007L, ContainerType.PARTIAL_LOAD, "CT-96430008541");
        container22.addContainerLoad(new ContainerLoad(1008L, 5000.0, 2.0, 4.0));
        container22.addContainerLoad(new ContainerLoad(1009L, 3000.0, 5.0, 9.0));
        transport2.addContainer(container21);
        transport2.addContainer(container22);

        this.put(transport1.getTrackingId(), transport1);
        this.put(transport2.getTrackingId(), transport2);
    }
}
