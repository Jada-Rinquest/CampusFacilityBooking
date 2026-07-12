package za.ac.cput.campusfacilitybooking.serviceTest;

/* Author: Nuyra Swanson (221290524)
     Date: 12 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.repository.BookingRepository;
import za.ac.cput.campusfacilitybooking.service.impl.BookingServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private BookingRepository repository;
    private BookingServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(BookingRepository.class);
        service = new BookingServiceImpl(repository);
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

        when(repository.save(booking)).thenReturn(booking);

        Booking created = service.create(booking);

        assertNotNull(created);
        assertEquals(booking.getBookingId(), created.getBookingId());
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

        when(repository.findById(booking.getBookingId()))
                .thenReturn(Optional.of(booking));

        Booking found = service.read(booking.getBookingId());

        assertNotNull(found);
        assertEquals(booking.getBookingId(), found.getBookingId());
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

        when(repository.save(booking)).thenReturn(booking);

        Booking updated = service.update(booking);

        assertEquals("Approved", updated.getStatus());
    }

    @Test
    void testDelete() {
        String id = "B001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}
