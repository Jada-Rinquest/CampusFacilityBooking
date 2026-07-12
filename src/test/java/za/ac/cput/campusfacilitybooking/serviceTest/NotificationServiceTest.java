package za.ac.cput.campusfacilitybooking.serviceTest;

/* Author: Nuyra Swanson (221290524)
     Date: 12 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;
import za.ac.cput.campusfacilitybooking.repository.NotificationRepository;
import za.ac.cput.campusfacilitybooking.service.impl.NotificationServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private NotificationRepository repository;
    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(NotificationRepository.class);
        service = new NotificationServiceImpl(repository);
    }

    @Test
    void testCreate() {
        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Approved",
                "BOOKING",
                "2026-07-12",
                false
        );

        when(repository.save(notification)).thenReturn(notification);

        Notification created = service.create(notification);

        assertNotNull(created);
        assertEquals(notification.getNotificationId(), created.getNotificationId());
    }

    @Test
    void testRead() {
        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Approved",
                "BOOKING",
                "2026-07-12",
                false
        );

        when(repository.findById(notification.getNotificationId()))
                .thenReturn(Optional.of(notification));

        Notification found = service.read(notification.getNotificationId());

        assertNotNull(found);
        assertEquals(notification.getNotificationId(), found.getNotificationId());
    }

    @Test
    void testUpdate() {
        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Cancelled",
                "BOOKING",
                "2026-07-13",
                true
        );

        when(repository.save(notification)).thenReturn(notification);

        Notification updated = service.update(notification);

        assertTrue(updated.isRead());
    }

    @Test
    void testDelete() {
        String id = "N001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}
