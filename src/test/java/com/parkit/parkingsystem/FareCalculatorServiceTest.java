package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test // 1 heure CAR
	public void calculateFareCar() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test // 1 heure BIKE
	public void calculateFareBike() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test // 1 heure Unknown
	public void calculateFareUnkownType() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		// THEN
		assertThrows(NullPointerException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		// THEN
		assertThrows(IllegalArgumentException.class,
				() -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45
																		// minutes
																		// parking
																		// time
																		// should
																		// give
																		// 3/4th
																		// parking
																		// fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45
																		// minutes
																		// parking
																		// time
																		// should
																		// give
																		// 3/4th
																		// parking
																		// fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24
																			// hours
																			// parking
																			// time
																			// should
																			// give
																			// 24
																			// *
																			// parking
																			// fare
																			// per
																			// hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithMoreThanADayParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24
																			// hours
																			// parking
																			// time
																			// should
																			// give
																			// 24
																			// *
																			// parking
																			// fare
																			// per
																			// hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	// Feature 1 DP
	@Test // Auto moins de 30 mn
	public void calculateFareCarWithLessThan30MinutesParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000 - 1000));// 30
																				// mn
																				// -
																				// 1
																				// sec
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(0, ticket.getPrice());

	}

	@Test // Moto moins de 30 mn
	public void calculateFareBikeWithLessThan30MinutesParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000 - 1000));// 30
																				// mn
																				// -
																				// 1
																				// sec
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(0, ticket.getPrice());

	}

	@Test // Auto entre 30 mn et 1h
	public void calculateFareCarWithMoreThan30MinutesAndLessThanOneHourParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000)); // 30 mn

		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());

	}

	@Test // Moto entre 30 mn et 1h
	public void calculateFareBikeWithMoreThan30MinutesAndLessThanOneHourParkingTime() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000)); // 30 mn

		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertEquals(0.5 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());

	}
	@Test // Auto avec discount fidélité
	public void calculateFareCarWithRegularCustomer() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24
																			// H
																			// de
																			// parking

		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket,
				Fare.DISCOUNT_REGULAR_CUSTOMER);

		// THEN
		assertEquals((int) (ticket.getPrice() * 100) / 100.,
				(int) (24 * Fare.CAR_RATE_PER_HOUR * 0.95 * 100) / 100.);

		// 5%
		// de
		// reduction

	}

	@Test // Moto avec discount fidélité
	public void calculateFareBikeWithRegularCustomer() {

		// GIVEN

		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24
																			// H
																			// de
																			// parking

		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// WHEN

		fareCalculatorService.calculateFare(ticket,
				Fare.DISCOUNT_REGULAR_CUSTOMER);

		// THEN
		assertEquals((int) (ticket.getPrice() * 100) / 100.,
				(int) (24 * Fare.BIKE_RATE_PER_HOUR * 0.95 * 100) / 100.);

		// 5%
		// de
		// reduction

	}
}
