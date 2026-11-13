
package za.ac.cput.campusfacilitybooking.service.impl;

/*
 * Author: Nuyra Swanson (221290524)
 * Date: 12 July 2026
 *
 * Updated: Maximum 3 bookings per facility and time slot
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.repository.BookingRepository;
import za.ac.cput.campusfacilitybooking.service.BookingService;

import java.util.List;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {

    private static final int MAX_BOOKINGS = 3;

    private static final Set<BookingStatus> ACTIVE_STATUSES =
            Set.of(
                    BookingStatus.PENDING,
                    BookingStatus.APPROVED
            );

    private final BookingRepository repository;
    private final EntityManager entityManager;

    @Autowired
    public BookingServiceImpl(
            BookingRepository repository,
            EntityManager entityManager) {

        this.repository = repository;
        this.entityManager = entityManager;
    }

    /* =========================================================
       VALIDATION
    ========================================================= */

    private boolean isActive(Booking booking) {
        return ACTIVE_STATUSES.contains(booking.getBookingStatus());
    }

    private void requireBookingDetails(Booking booking) {

        if (booking == null ||
                booking.getFacilityId() == null ||
                booking.getFacilityId().isBlank() ||
                booking.getTimeSlotId() == null ||
                booking.getTimeSlotId().isBlank() ||
                booking.getUserId() == null ||
                booking.getUserId().isBlank()) {

            throw new IllegalArgumentException(
                    "Facility, time slot and user are required."
            );
        }
    }

    private void lockTimeSlot(String timeSlotId) {

        TimeSlot timeSlot = entityManager.find(
                TimeSlot.class,
                timeSlotId,
                LockModeType.PESSIMISTIC_WRITE
        );

        if (timeSlot == null) {
            throw new IllegalArgumentException(
                    "Selected time slot does not exist."
            );
        }
    }

    private void validateCapacity(
            Booking booking,
            String excludedBookingId) {

        requireBookingDetails(booking);

        lockTimeSlot(booking.getTimeSlotId());

        String facilityId = booking.getFacilityId();
        String timeSlotId = booking.getTimeSlotId();
        String userId = booking.getUserId();

        long bookedCount;
        boolean duplicate;

        if (excludedBookingId == null) {

            bookedCount =
                    repository.countByFacilityIdAndTimeSlotIdAndBookingStatusIn(
                            facilityId,
                            timeSlotId,
                            ACTIVE_STATUSES
                    );

            duplicate =
                    repository.existsByFacilityIdAndTimeSlotIdAndUserIdAndBookingStatusIn(
                            facilityId,
                            timeSlotId,
                            userId,
                            ACTIVE_STATUSES
                    );

        } else {

            bookedCount =
                    repository.countByFacilityIdAndTimeSlotIdAndBookingStatusInAndBookingIdNot(
                            facilityId,
                            timeSlotId,
                            ACTIVE_STATUSES,
                            excludedBookingId
                    );

            duplicate =
                    repository.existsByFacilityIdAndTimeSlotIdAndUserIdAndBookingStatusInAndBookingIdNot(
                            facilityId,
                            timeSlotId,
                            userId,
                            ACTIVE_STATUSES,
                            excludedBookingId
                    );
        }

        if (bookedCount >= MAX_BOOKINGS) {

            throw new IllegalStateException(
                    "This facility and time slot are fully booked. " +
                            "Maximum 3 students allowed."
            );
        }

        if (duplicate) {

            throw new IllegalStateException(
                    "You have already booked this facility " +
                            "for the selected time slot."
            );
        }
    }

    /* =========================================================
       CREATE
    ========================================================= */

    @Override
    @Transactional
    public Booking create(Booking booking) {

        requireBookingDetails(booking);

        if (booking.getBookingStatus() == null) {
            throw new IllegalArgumentException(
                    "Booking status is required."
            );
        }

        if (isActive(booking)) {
            validateCapacity(booking, null);
        }

        return repository.saveAndFlush(booking);
    }

    /* =========================================================
       READ
    ========================================================= */

    @Override
    @Transactional(readOnly = true)
    public Booking read(String bookingId) {

        return repository.findById(bookingId)
                .orElse(null);
    }

    /* =========================================================
       UPDATE
    ========================================================= */

    @Override
    @Transactional
    public Booking update(Booking booking) {

        if (booking == null ||
                booking.getBookingId() == null ||
                booking.getBookingId().isBlank()) {

            throw new IllegalArgumentException(
                    "Booking ID is required."
            );
        }

        if (!repository.existsById(booking.getBookingId())) {
            throw new IllegalArgumentException(
                    "Booking does not exist."
            );
        }

        requireBookingDetails(booking);

        if (booking.getBookingStatus() == null) {
            throw new IllegalArgumentException(
                    "Booking status is required."
            );
        }

        if (isActive(booking)) {

            validateCapacity(
                    booking,
                    booking.getBookingId()
            );
        }

        return repository.saveAndFlush(booking);
    }

    /* =========================================================
       DELETE
    ========================================================= */

    @Override
    @Transactional
    public boolean delete(String bookingId) {

        if (repository.existsById(bookingId)) {

            repository.deleteById(bookingId);
            return true;
        }

        return false;
    }

    /* =========================================================
       GET ALL
    ========================================================= */

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAll() {

        return repository.findAll();
    }
}
