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
	private static InputReaderUtil inputReaderUtil;

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
		lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
		lenient().when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		ParkingService parkingService = new ParkingService(inputReaderUtil,
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
	public void testParkingLotExit() {
		testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil,
				parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated
		// correctly in the database
	}

}
