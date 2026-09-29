package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFactoryTest {

    @Test
    void testCreateBooking() {
        Booking booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Student Session",
                BookingStatus.PENDING
        );

        assertNotNull(booking);
        assertEquals("B001", booking.getBookingId());
        assertEquals("F001", booking.getFacilityId());
        assertEquals("TS001", booking.getTimeSlotId());
        assertEquals("U001", booking.getUserId());
        assertEquals("Student Session", booking.getPurpose());
        assertEquals(BookingStatus.PENDING, booking.getBookingStatus());
    }

    @Test
    void testCreateBookingWithInvalidBookingId() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "",
                        "F001",
                        "TS001",
                        "U001",
                        "Study Session",
                        BookingStatus.PENDING
                )
        );
    }

    @Test
    void testCreateBookingWithInvalidFacilityId() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "B002",
                        "",
                        "TS001",
                        "U001",
                        "Study Session",
                        BookingStatus.PENDING
                )
        );
    }

    @Test
    void testCreateBookingWithInvalidTimeSlotId() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "B003",
                        "F001",
                        "",
                        "U001",
                        "Study Session",
                        BookingStatus.PENDING
                )
        );
    }

    @Test
    void testCreateBookingWithInvalidUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "B004",
                        "F001",
                        "TS001",
                        "",
                        "Study Session",
                        BookingStatus.PENDING
                )
        );
    }

    @Test
    void testCreateBookingWithInvalidPurpose() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "B005",
                        "F001",
                        "TS001",
                        "U001",
                        "",
                        BookingStatus.PENDING
                )
        );
    }

    @Test
    void testCreateBookingWithNullBookingStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                BookingFactory.createBooking(
                        "B006",
                        "F001",
                        "TS001",
                        "U001",
                        "Study Session",
                        null
                )
        );
    }
}