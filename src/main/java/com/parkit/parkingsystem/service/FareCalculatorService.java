package com.parkit.parkingsystem.service;

import java.time.Duration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null)
				|| (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:"
					+ ticket.getOutTime().toString());
		}

		// int inHour = ticket.getInTime().getHours();
		// int outHour = ticket.getOutTime().getHours();
		// DP : Correction

		// TODO: Some tests are failing here. Need to check if this logic is
		// correct
		// DP : soustraction 2 dates incorrecte

		// int duration = outHour - inHour;

		Duration duration = Duration.between(ticket.getInTime().toInstant(),
				ticket.getOutTime().toInstant());

		// DP: < 30 mn gratuites (feature 1)

		if (duration.toMinutes() < 30) {
			ticket.setPrice(0f);
		} else {

			// Mettre duration en heure (60f = conversion en double)
			switch (ticket.getParkingSpot().getParkingType()) {
				case CAR : {
					ticket.setPrice(duration.toMinutes() / 60f
							* Fare.CAR_RATE_PER_HOUR);
					break;
				}
				case BIKE : {
					ticket.setPrice(duration.toMinutes() / 60f
							* Fare.BIKE_RATE_PER_HOUR);
					break;
				}
				default :
					throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
	}
}