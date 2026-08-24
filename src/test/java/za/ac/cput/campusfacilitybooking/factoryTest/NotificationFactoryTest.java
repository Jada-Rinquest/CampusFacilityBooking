package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Notification;
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
                "NT001"
        );

        assertNotNull(notification);
        assertEquals("N001", notification.getNotificationId());
        assertEquals("U001", notification.getUserId());
        assertEquals("Your booking has been approved", notification.getMessage());
        assertEquals(
                LocalDate.of(2026, 8, 20),
                notification.getSentDate()
        );
        assertEquals("NT001", notification.getNotificationTypeId());
    }

    @Test
    void testCreateNotificationWithInvalidNotificationId() {

        assertThrows(IllegalArgumentException.class, () ->
                NotificationFactory.createNotification(
                        "",
                        "U001",
                        "Your booking has been approved",
                        LocalDate.of(2026, 8, 20),
                        "NT001"
                )
        );
    }
}