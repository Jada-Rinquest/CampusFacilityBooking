package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Nuyra Swanson (221290524)
     Date: 28 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Notification;
import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationFactoryTest {

    @Test
    void testCreateNotification() {

        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Your booking has been approved",
                "BOOKING",
                "2026-08-20",
                false
        );

        assertNotNull(notification);
        assertNull(notification.getNotificationId());
        assertEquals("S001", notification.getNotificationId());
        assertEquals("BOOKING", notification.getType());
        assertFalse(notification.isRead());
    }

    @Test
    void testCreateNotificationFail() {
        Notification notification = NotificationFactory.createNotification(
                "",
                "Your booking has been approved",
                "BOOKING",
                "2026-08-20",
                false
        );

        assertNull(notification);
    }
}
