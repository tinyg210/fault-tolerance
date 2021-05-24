package org.tinyg.rest;

import org.jboss.logging.Logger;
import org.tinyg.model.Shipment;
import org.tinyg.service.ShipmentRepoService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;

@Path("/shipment")
public class ShipmentEndpoint {

    private static final Logger LOGGER = Logger.getLogger(ShipmentEndpoint.class);

    @Inject
    ShipmentRepoService shipmentRepoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Shipment> shipments() {
        extraInformationProcessing();
        LOGGER.info("Information fetched successfully.");
        return shipmentRepoService.getAllShipments();

    }

    @GET
    @Path("/trackingids")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getValidShipments() {
        return shipmentRepoService.getValidTrackingIds();
    }

    private void extraInformationProcessing() {
        if (new Random().nextInt(50) % 2 == 0) {
            LOGGER.error("The required information could not be retrieved at this time.");
            throw new RuntimeException();
        }
    }

}