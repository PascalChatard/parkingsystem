package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        
        
        //duration in decimal hours
        double duration = (ticket.getOutTime().getTime() - ticket.getInTime().getTime()) / 3600000.;
        // with-2-decimal precision
        duration = Math.round(duration*100.0)/100.0;

        double discount = 1;
        if (ticket.getRecurringUser())
            discount = 0.95; // 5% discount

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(discount * duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(discount * duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}