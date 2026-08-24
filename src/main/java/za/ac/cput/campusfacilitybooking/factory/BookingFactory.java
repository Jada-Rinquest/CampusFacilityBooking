package za.ac.cput.campusfacilitybooking.factory;

/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Booking;

public class BookingFactory {

    public static Booking createBooking(String bookingId,
                                        String facilityId,
                                        String timeSlotId,
                                        String userId,
                                        String purpose,
                                        String bookingStatusId) {

        if (bookingId == null || bookingId.isEmpty()) {
            throw new IllegalArgumentException("Booking ID is required");
        }

        if (facilityId == null || facilityId.isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        if (timeSlotId == null || timeSlotId.isEmpty()) {
            throw new IllegalArgumentException("Time Slot ID is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (purpose == null || purpose.isEmpty()) {
            throw new IllegalArgumentException("Purpose is required");
        }

        if (bookingStatusId == null || bookingStatusId.isEmpty()) {
            throw new IllegalArgumentException("Booking Status ID is required");
        }

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