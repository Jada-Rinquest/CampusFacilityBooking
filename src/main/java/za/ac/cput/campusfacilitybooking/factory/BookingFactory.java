package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

public class BookingFactory {

    public static Booking createBooking(String bookingId,
                                        String facilityId,
                                        String timeSlotId,
                                        String userId,
                                        String purpose,
                                        BookingStatus bookingStatus) {

        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID is required");
        }

        if (facilityId == null || facilityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        if (timeSlotId == null || timeSlotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Time Slot ID is required");
        }

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalArgumentException("Purpose is required");
        }

        if (bookingStatus == null) {
            throw new IllegalArgumentException("Booking Status is required");
        }

        return new Booking.Builder()
                .setBookingId(bookingId)
                .setFacilityId(facilityId)
                .setTimeSlotId(timeSlotId)
                .setUserId(userId)
                .setPurpose(purpose)
                .setBookingStatus(bookingStatus)
                .build();
    }
}