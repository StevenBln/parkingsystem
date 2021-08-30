package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Calendar;

public class FareCalculatorService {
    public static final float DISCOUNT_FACTOR = 0.95f;
    public static final int TIME_CONVERTER = 3600000;
    public void calculateFare(Ticket ticket, int countTicket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour - inHour;


        switch (ticket.getParkingSpot().getParkingType()) {


            case CAR: {
                ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) / TIME_CONVERTER);
                break;
            }
            case BIKE: {
                ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) / TIME_CONVERTER);
                break;
            }


            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
        if (duration <= 30 * 60 * 1000) {
            ticket.setPrice(0);

        }
        if (countTicket > 1) {
            ticket.setPrice(DISCOUNT_FACTOR * ticket.getPrice());
        }

    }
}
