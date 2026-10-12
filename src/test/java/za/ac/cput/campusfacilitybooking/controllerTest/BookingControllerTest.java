package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Nuyra Swanson (221290524)
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.BookingController;
import za.ac.cput.campusfacilitybooking.controller.BookingRequest;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;

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
        BookingRequest request = new BookingRequest();
        request.setBookingId("B001");
        request.setFacilityId("F001");
        request.setTimeSlotId("TS001");
        request.setUserId("U001");
        request.setPurpose("Study Session");
        request.setBookingStatus(BookingStatus.PENDING);

        Booking booking = BookingFactory.createBooking(
                request.getBookingId(),
                request.getFacilityId(),
                request.getTimeSlotId(),
                request.getUserId(),
                request.getPurpose(),
                request.getBookingStatus()
        );

        when(service.create(any(Booking.class))).thenReturn(booking);

        ResponseEntity<Booking> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("B001", response.getBody().getBookingId());
        assertEquals(BookingStatus.PENDING, response.getBody().getBookingStatus());
    }

    @Test
    void testRead() {
        Booking booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        when(service.read("B001")).thenReturn(booking);

        ResponseEntity<Booking> response = controller.read("B001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("B001", response.getBody().getBookingId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("B999")).thenReturn(null);

        ResponseEntity<Booking> response = controller.read("B999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Booking booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Workshop",
                BookingStatus.APPROVED
        );

        when(service.update(booking)).thenReturn(booking);

        ResponseEntity<Booking> response = controller.update(booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BookingStatus.APPROVED, response.getBody().getBookingStatus());
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