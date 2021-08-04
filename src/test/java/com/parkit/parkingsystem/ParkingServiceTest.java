package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processIncomingVehicleTest() {
		System.out.println("\n**************************  processIncomingVehicleTest  **************************");

		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
		System.out.println("\n**************************  end processIncomingVehicleTest  **************************");
	}

	@Test
	public void processIncomingVehicleForRecurringUserTest() {
		System.out.println(
				"\n**************************  processIncomingVehicleForRecurringUserTest  **************************");
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
		ticket.setOutTime(new Date(System.currentTimeMillis() - (22 * 60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setPrice(32.50);
		ticket.setRecurringUser(true);

		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
		System.out.println(
				"\n**************************  end processIncomingVehicleForRecurringUserTest  **************************");
	}

	@Test
	public void processExitingVehicleTest() {
		System.out.println("\n**************************  processExitingVehicleTest  **************************");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

		parkingService.processExitingVehicle();

		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
		assertEquals((Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

		System.out.println("\n**************************  end processExitingVehicleTest  **************************");
	}

	@Test
	public void processExitingVehicleRecurringUserTest() {
		System.out.println(
				"\n**************************  processExitingVehicleRecurringUserTest  **************************");
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		Ticket oldTicket = new Ticket();
		oldTicket.setInTime(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
		oldTicket.setOutTime(new Date(System.currentTimeMillis() - (22 * 60 * 60 * 1000)));
		oldTicket.setParkingSpot(parkingSpot);
		oldTicket.setVehicleRegNumber("ABCDEF");
		oldTicket.setPrice(32.50);
		oldTicket.setRecurringUser(true);

		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.getOldTicket(anyString())).thenReturn(oldTicket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
		assertEquals((Fare.CAR_RATE_PER_HOUR * (1 - 0.05)), ticket.getPrice());
		System.out.println(
				"\n**************************  end processExitingVehicleRecurringUserTest  **************************");
	}

}
