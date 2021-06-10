package org.tinyg.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmissionsCalculatorService {

    public String calculate(final String trackingId) {
        //Fetch transport based on trackingId.
        //Figure out the load.
        //Try to locate it based on last checkpoint.
        //Retrieve weather conditions, etc
        //Actual calculations...
        return "CO2 report has been computed.";

    }

    public String genericInformationAboutCO2(String trackingId) {
        return "An average consumption of 4,2 kg /" +
                " 100 km " +
                "corresponds to 4,2 kg x 2666 " +
                "g/kg = 112 g of CO2/km. This should be no different for the transport number: " + trackingId;
    }

}
