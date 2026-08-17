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

import java.time.LocalDate;
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
                "U001",
                "Booking Approved",
                LocalDate.of(2026, 7, 12),
                "NT001"
        );

        when(repository.save(notification)).thenReturn(notification);

        Notification created = service.create(notification);

        assertNotNull(created);
        assertEquals(notification.getNotificationId(), created.getNotificationId());
    }

    @Test
    void testRead() {

        Notification notification = NotificationFactory.createNotification(
                "U001",
                "Booking Approved",
                LocalDate.of(2026, 7, 12),
                "NT001"
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
                "U001",
                "Booking Cancelled",
                LocalDate.of(2026, 7, 13),
                "NT002"
        );

        when(repository.save(notification)).thenReturn(notification);

        Notification updated = service.update(notification);

        assertNotNull(updated);
        assertEquals("Booking Cancelled", updated.getMessage());
        assertEquals("NT002", updated.getNotificationTypeId());
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