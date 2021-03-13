package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    TicketDAO ticketDAO;
    
    public FareCalculatorService(TicketDAO ticketDAO) {
	super();
	this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}
	long inHour = ticket.getInTime().getTime();
	long outHour = ticket.getOutTime().getTime();
	double duration = (double) (outHour - inHour) / (1000 * 60 * 60);
	double totalDiscount = getPricing(ticket, duration);
	switch (ticket.getParkingSpot().getParkingType()) {
	case CAR: {
	    ticket.setPrice(duration * totalDiscount * Fare.CAR_RATE_PER_HOUR);
	    break;
	}
	case BIKE: {
	    ticket.setPrice(duration * totalDiscount * Fare.BIKE_RATE_PER_HOUR);
	    break;
	}
	default:
	    throw new IllegalArgumentException("Unkown Parking Type");
	}
    }

    public double getPricing(Ticket ticket, double duration) {
	double totalDiscount;
	double discount30mnFree;
	double discount5PcRecurrentCustomer;
	int nbOfParking = ticketDAO.getNumberOfTicketsForVehicle(ticket.getVehicleRegNumber());
	Boolean isRecurringCustomer = nbOfParking > Fare.RECURRING_NUMBER ? true : false;
	
	// First half hour will not be invoiced
	discount30mnFree = (duration > 0.5) ? (duration - 0.5) / duration : 0;
	
	// Discount 5% for recurring vehicle
	discount5PcRecurrentCustomer = (discount30mnFree > 0) && isRecurringCustomer ? 0.95 : 1;
	
	totalDiscount = discount30mnFree * discount5PcRecurrentCustomer;
	return totalDiscount;
    }
}