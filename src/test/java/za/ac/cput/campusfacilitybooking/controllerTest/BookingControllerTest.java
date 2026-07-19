package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Nuyra Swanson (221290524)
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;
import za.ac.cput.campusfacilitybooking.controller.BookingController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    private BookingService service;
    private BookingController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(BookingService.class);
        controller = new BookingController(service);
    }

    @Test
    void testCreate() {

        Booking booking = BookingFactory.createBooking(
                "F001",
                "TS001",
                "S001",
                "Student",
                "Study Session",
                "Pending"
        );

        when(service.create(booking)).thenReturn(booking);

        ResponseEntity<Booking> response = controller.create(booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(booking.getBookingId(), response.getBody().getBookingId());
    }

    @Test
    void testRead() {

        Booking booking = BookingFactory.createBooking(
                "F001",
                "TS001",
                "S001",
                "Student",
                "Study Session",
                "Pending"
        );

        when(service.read(booking.getBookingId())).thenReturn(booking);

        ResponseEntity<Booking> response = controller.read(booking.getBookingId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(booking.getBookingId(), response.getBody().getBookingId());
    }

    @Test
    void testUpdate() {

        Booking booking = BookingFactory.createBooking(
                "F001",
                "TS001",
                "S001",
                "Student",
                "Workshop",
                "Approved"
        );

        when(service.update(booking)).thenReturn(booking);

        ResponseEntity<Booking> response = controller.update(booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Approved", response.getBody().getStatus());
    }

    @Test
    void testDelete() {

        when(service.delete("B001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("B001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        verify(service).delete("B001");
    }
}
