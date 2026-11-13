
package za.ac.cput.campusfacilitybooking.repository;

/*
 * Author: Nuyra Swanson (221290524)
 * Date: 12 July 2026
 *
 * Updated: Booking capacity and duplicate validation
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    /*
     * Count active bookings for a facility and time slot.
     *
     * Only PENDING and APPROVED bookings occupy spaces.
     */
    long countByFacilityIdAndTimeSlotIdAndBookingStatusIn(
            String facilityId,
            String timeSlotId,
            Collection<BookingStatus> statuses
    );

    /*
     * Count active bookings excluding the booking being updated.
     */
    long countByFacilityIdAndTimeSlotIdAndBookingStatusInAndBookingIdNot(
            String facilityId,
            String timeSlotId,
            Collection<BookingStatus> statuses,
            String bookingId
    );

    /*
     * Check whether a student has already booked
     * the same facility and time slot.
     */
    boolean existsByFacilityIdAndTimeSlotIdAndUserIdAndBookingStatusIn(
            String facilityId,
            String timeSlotId,
            String userId,
            Collection<BookingStatus> statuses
    );

    /*
     * Check for duplicate bookings during an update,
     * excluding the booking being edited.
     */
    boolean existsByFacilityIdAndTimeSlotIdAndUserIdAndBookingStatusInAndBookingIdNot(
            String facilityId,
            String timeSlotId,
            String userId,
            Collection<BookingStatus> statuses,
            String bookingId
    );
}
