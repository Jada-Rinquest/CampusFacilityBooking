package za.ac.cput.campusfacilitybooking.factory;

/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Booking;
import java.util.UUID;

public class BookingFactory {

    public static Booking createBooking(String facilityId,
                                        String timeSlotId,
                                        String requesterId,
                                        String requesterType,
                                        String purpose,
                                        String status) {

        if (facilityId == null || facilityId.isEmpty()
        || timeSlotId == null || timeSlotId.isEmpty()
        || requesterId == null || requesterId.isEmpty()
        || requesterType == null || requesterType.isEmpty()
                || purpose == null || purpose.isEmpty()
                || status == null || status.isEmpty()) {
            return null;
        }

        String bookingId = UUID.randomUUID().toString();

        return new Booking.Builder()
                .setBookingId(bookingId)
                .setFacilityId(facilityId)
                .setTimeSlotId(timeSlotId)
                .setRequesterId(requesterId)
                .setRequesterType(requesterType)
                .setPurpose(purpose)
                .setStatus(status)
                .build();
    }
}
