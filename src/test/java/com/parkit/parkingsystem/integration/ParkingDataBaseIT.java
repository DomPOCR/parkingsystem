package com.parkit.parkingsystem.integration;

import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtilCar;
	// private static InputReaderUtil inputReaderUtilBike;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		lenient().when(inputReaderUtilCar.readSelection()).thenReturn(1); // CAR
		lenient().when(inputReaderUtilCar.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");

		// lenient().when(inputReaderUtilBike.readSelection()).thenReturn(2); //
		// BIKE
		// lenient().when(inputReaderUtilBike.readVehicleRegistrationNumber())
		// .thenReturn("BIKE01");

		// Clear de la DB
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		ParkingService parkingService = new ParkingService(inputReaderUtilCar,
				parkingSpotDAO, ticketDAO);
		// DP : récuperer le no de la prochaine classe disponible pourn une
		// voiture
		int next = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is
		// updated with availability

		// Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>
		// mocké
		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		// Etape 2 : vérifier l'existence du ticket
		Assertions.assertNotNull(ticket);

		// Etape 3 : récupérer l'ID de parking (parkingSpot) et vérifier son
		// existence
		ParkingSpot parkingSpot = ticket.getParkingSpot();
		Assertions.assertNotNull(parkingSpot);

		// Etape 4 : vérifier que l'état de la colonne AVAILABLE = FALSE (place
		// n'est plus disponible)
		Assertions.assertFalse(parkingSpot.isAvailable());

		// Etape 5 : vérifier que la place qui était disponible soit bien celle
		// retournée
		Assertions.assertEquals(next, parkingSpot.getId());

	}

	@Test
	public void testParkingLotExitACar() {
		// DP : On relance le test précédent
		testParkingACar();

		// Initialisation du service parking
		ParkingService parkingService = new ParkingService(inputReaderUtilCar,
				parkingSpotDAO, ticketDAO);

		// Récupération du véhicule pour la sortie
		parkingService.processExitingVehicle();

		// TODO: check that the fare generated and out time are populated
		// correctly in the database

		// Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>
		// mocké
		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		// Etape 2 : vérification de l'existence du ticket
		Assertions.assertNotNull(ticket);

		// Etape 3 : vérification que le ticket a une date de sortie du véhicule
		Assertions.assertNotNull(ticket.getOutTime());

		// Etape 4 : vérification que le prix du ticket est correct
		// On teste la valeur 0 car dans le test on rentre et on sort de suite.
		// On peut pas mocker le prix (variable static).
		Assertions.assertEquals(0, ticket.getPrice());

	}

}
