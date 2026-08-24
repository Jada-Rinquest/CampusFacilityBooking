package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

public class BookingFactory {

    public static Booking createBooking(String bookingId,
                                        Facility facility,
                                        TimeSlot timeSlot,
                                        User user,
                                        String purpose,
                                        BookingStatus bookingStatus) {

        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID is required");
        }

        if (facility == null) {
            throw new IllegalArgumentException("Facility is required");
        }

        if (timeSlot == null) {
            throw new IllegalArgumentException("Time Slot is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalArgumentException("Purpose is required");
        }

        if (bookingStatus == null) {
            throw new IllegalArgumentException("Booking Status is required");
        }

        return new Booking.Builder()
                .setBookingId(bookingId)
                .setFacility(facility)
                .setTimeSlot(timeSlot)
                .setUser(user)
                .setPurpose(purpose)
                .setBookingStatus(bookingStatus)
                .build();
    }
}