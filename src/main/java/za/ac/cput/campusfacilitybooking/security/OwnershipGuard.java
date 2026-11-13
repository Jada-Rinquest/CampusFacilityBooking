package za.ac.cput.campusfacilitybooking.security;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import za.ac.cput.campusfacilitybooking.controller.BookingRequest;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.repository.BookingRepository;
import za.ac.cput.campusfacilitybooking.repository.NotificationRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;

/*
 * Record-level checks used from @PreAuthorize as "@ownership.xxx(...)".
 *
 * Staff (LECTURER / ADMIN) may act on any record. Students may only
 * act on records that belong to them.
 */
@Component("ownership")
public class OwnershipGuard {

    private final BookingRepository bookingRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public OwnershipGuard(BookingRepository bookingRepository,
                          NotificationRepository notificationRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /* A student may only book for themselves, only as PENDING,
       and may not overwrite an existing booking by reusing its id. */
    public boolean canCreateBooking(BookingRequest request, Authentication authentication) {
        if (isStaff(authentication)) {
            return true;
        }
        return request != null
                && Objects.equals(request.getUserId(), userId(authentication))
                && (request.getBookingStatus() == null || request.getBookingStatus() == BookingStatus.PENDING)
                && (request.getBookingId() == null || !bookingRepository.existsById(request.getBookingId()));
    }

    public boolean ownsBooking(String bookingId, Authentication authentication) {
        if (isStaff(authentication)) {
            return true;
        }
        return bookingId != null && bookingRepository.findById(bookingId)
                .map(booking -> Objects.equals(booking.getUserId(), userId(authentication)))
                .orElse(false);
    }

    public boolean ownsNotification(String notificationId, Authentication authentication) {
        if (isStaff(authentication)) {
            return true;
        }
        return notificationId != null && notificationRepository.findById(notificationId)
                .map(notification -> Objects.equals(notification.getUserId(), userId(authentication)))
                .orElse(false);
    }

    /* A student may edit only their own profile and may not change the
       email address, because the email domain decides student vs staff. */
    public boolean canUpdateUser(User updated, Authentication authentication) {
        if (isStaff(authentication)) {
            return true;
        }
        if (updated == null || !Objects.equals(updated.getUserId(), userId(authentication))) {
            return false;
        }
        return userRepository.findById(updated.getUserId())
                .map(existing -> sameEmail(existing.getEmail(), updated.getEmail()))
                .orElse(false);
    }

    private static boolean sameEmail(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }

    private static String userId(Authentication authentication) {
        return authentication == null ? null : authentication.getName();
    }

    private static boolean isStaff(Authentication authentication) {
        return authentication != null
                && authentication.getPrincipal() instanceof AuthenticatedUser user
                && user.isStaff();
    }
}
