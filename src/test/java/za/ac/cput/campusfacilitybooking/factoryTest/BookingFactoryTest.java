package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
     Date: 28 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Booking;
import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFactoryTest {

    @Test
    void testCreateBooking() {
        Booking booking = BookingFactory.createBooking(
                "F001",
                "TS001",
                "S001",
                "Student",
                "Student Session",
                "Pending"
        );

        assertNotNull(booking);
        assertNotNull(booking.getBookingId());
        assertEquals("F001", booking.getFacilityId());
        assertEquals("TS001", booking.getTimeSlotId());
        assertEquals("S001", booking.getRequesterId());
        assertEquals("Student", booking.getRequesterType());
        assertEquals("Student Session", booking.getPurpose());
        assertEquals("Pending", booking.getStatus());
    }

    @Test
    void testCreateBookingWithInvalidDate() {
        Booking booking = BookingFactory.createBooking(
                "",
                "TS001",
                "S001",
                "Student",
                "Study Session",
                "Pending"
        );

        assertNull(booking);
    }
}
