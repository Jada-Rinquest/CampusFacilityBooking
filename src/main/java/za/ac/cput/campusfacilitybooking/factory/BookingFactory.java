package za.ac.cput.campusfacilitybooking.factory;

/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Booking;

import java.util.UUID;

public class BookingFactory {

    public static Booking createBooking(
            String facilityId,
            String timeSlotId,
            String userId,
            String purpose,
            String bookingStatusId) {

        if (facilityId == null || facilityId.isEmpty()
                || timeSlotId == null || timeSlotId.isEmpty()
                || userId == null || userId.isEmpty()
                || purpose == null || purpose.isEmpty()
                || bookingStatusId == null || bookingStatusId.isEmpty()) {

            return null;
        }

        String bookingId = UUID.randomUUID().toString();

        return new Booking.Builder()
                .setBookingId(bookingId)
                .setFacilityId(facilityId)
                .setTimeSlotId(timeSlotId)
                .setUserId(userId)
                .setPurpose(purpose)
                .setBookingStatusId(bookingStatusId)
                .build();
    }
}