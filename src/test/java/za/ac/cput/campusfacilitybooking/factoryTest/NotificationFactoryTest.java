package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationFactoryTest {

    @Test
    void testCreateNotification() {
        Notification notification = NotificationFactory.createNotification(
                "N001",
                "U001",
                "Your booking has been approved",
                LocalDate.of(2026, 8, 20),
                NotificationType.BOOKING_CONFIRMATION
        );

        assertNotNull(notification);
        assertEquals("N001", notification.getNotificationId());
        assertEquals("U001", notification.getUserId());
        assertEquals("Your booking has been approved", notification.getMessage());
        assertEquals(LocalDate.of(2026, 8, 20), notification.getSentDate());
        assertEquals(NotificationType.BOOKING_CONFIRMATION, notification.getNotificationType());
    }

    @Test
    void testCreateNotificationWithInvalidNotificationId() {
        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "",
                        "U001",
                        "Your booking has been approved",
                        LocalDate.of(2026, 8, 20),
                        NotificationType.BOOKING_CONFIRMATION
                )
        );
    }

    @Test
    void testCreateNotificationWithInvalidUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "N002",
                        "",
                        "Your booking has been approved",
                        LocalDate.of(2026, 8, 20),
                        NotificationType.BOOKING_CONFIRMATION
                )
        );
    }

    @Test
    void testCreateNotificationWithInvalidMessage() {
        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "N003",
                        "U001",
                        "",
                        LocalDate.of(2026, 8, 20),
                        NotificationType.BOOKING_CONFIRMATION
                )
        );
    }

    @Test
    void testCreateNotificationWithNullSentDate() {
        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "N004",
                        "U001",
                        "Your booking has been approved",
                        null,
                        NotificationType.BOOKING_CONFIRMATION
                )
        );
    }

    @Test
    void testCreateNotificationWithNullNotificationType() {
        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "N005",
                        "U001",
                        "Your booking has been approved",
                        LocalDate.of(2026, 8, 20),
                        null
                )
        );
    }
}