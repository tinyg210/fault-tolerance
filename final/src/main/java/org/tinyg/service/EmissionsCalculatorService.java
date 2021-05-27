package org.tinyg.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmissionsCalculatorService {

    public String calculate(final String trackingId) {
        //Fetch shipment based on trackingId.
        //Figure out the load.
        //Try to locate it based on last checkpoint.
        //Actual calculations...
        return "CO2 report has been computed.";

    }
}
