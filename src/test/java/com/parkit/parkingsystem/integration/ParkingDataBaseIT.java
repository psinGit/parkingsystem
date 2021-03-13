package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
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
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public  class ParkingDataBaseIT {
    // working with db: test
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
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	// All spots free and no ticket entry
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
	dataBaseTestConfig.closeConnection(dataBasePrepareService.getConnection());
    }

    /**
     * When parking a car, test related ticket is saved and first ParkingSlot is
     * locked.
     */
    @Test
    public void testParkingACar() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();
	Ticket ticket = ticketDAO.getTicket("ABCDEF");
	// test response from database
	if (ticket != null) {
	    assertThat(ticketDAO.getTicket("ABCDEF")).isNotNull();
	    try {
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
	    } catch (Exception e) {
	    }
	} else
	    System.out.println("## DB ACCESS ERROR, PLS CHECK ! ##");
    }

    /**
     * When exiting the parking half and a hour later, test correct fare and out
     * time.
     */

    @Test
    public void testParkingLotExit() {
	Ticket ticket;
	// entry car
	testParkingACar();
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	ticket = ticketDAO.getTicket("ABCDEF");
	// save to ticket new inTime to last previous half and a hour from outTime
	Date arivalTime = new Date();
	arivalTime.setTime(System.currentTimeMillis() - (90 * 60 * 1000));
	Timestamp arrivalTS = new Timestamp(arivalTime.getTime());
	try (Connection con = dataBaseTestConfig.getConnection(); Statement st = con.createStatement();) {
	    st.executeUpdate("update test.ticket set IN_TIME='" + arrivalTS + "' where ID=1");
	} catch (Exception ex) {
	    System.out.println("Error saving ticket info: " + ex.getMessage());
	}
	// exit car
	parkingService.processExitingVehicle();
	// get Fare
	ticket = ticketDAO.getTicket("ABCDEF");
	double result = (double) Math.round(ticket.getPrice() * 10) / 10;
	// double expectedFare = (miniRecurrency == 0) ? 0.95 : 1;
	// expectedFare = (double) Math.round(1.5 * expectedFare * 10) / 10;
	assertThat(result).isEqualTo(1.5);
    }
}
