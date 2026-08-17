package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
     Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFactoryTest {

    @Test
    void testCreateBooking() {

        Booking booking = BookingFactory.createBooking(
                "F001",
                "TS001",
                "U001",
                "Student Session",
                "BS001"
        );

        assertNotNull(booking);
        assertNotNull(booking.getBookingId());
        assertEquals("F001", booking.getFacilityId());
        assertEquals("TS001", booking.getTimeSlotId());
        assertEquals("U001", booking.getUserId());
        assertEquals("Student Session", booking.getPurpose());
        assertEquals("BS001", booking.getBookingStatusId());
    }

    @Test
    void testCreateBookingWithInvalidFacilityId() {

        Booking booking = BookingFactory.createBooking(
                "",
                "TS001",
                "U001",
                "Study Session",
                "BS001"
        );

        assertNull(booking);
    }
}