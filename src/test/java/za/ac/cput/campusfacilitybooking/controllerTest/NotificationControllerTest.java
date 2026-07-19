package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Nuyra Swanson (221290524)
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.NotificationController;
import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;
import za.ac.cput.campusfacilitybooking.service.NotificationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class NotificationControllerTest {

    private NotificationService service;
    private NotificationController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(NotificationService.class);
        controller = new NotificationController(service);
    }

    @Test
    void testCreate() {

        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Approved",
                "BOOKING",
                "2026-07-19",
                false
        );

        when(service.create(notification)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.create(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notification.getNotificationId(), response.getBody().getNotificationId());
    }

    @Test
    void testRead() {

        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Approved",
                "BOOKING",
                "2026-07-19",
                false
        );

        when(service.read(notification.getNotificationId())).thenReturn(notification);

        ResponseEntity<Notification> response = controller.read(notification.getNotificationId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(notification.getNotificationId(), response.getBody().getNotificationId());
    }

    @Test
    void testUpdate() {

        Notification notification = NotificationFactory.createNotification(
                "S001",
                "Booking Updated",
                "BOOKING",
                "2026-07-19",
                true
        );

        when(service.update(notification)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.update(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isRead());
    }

    @Test
    void testDelete() {

        when(service.delete("N001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("N001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        verify(service).delete("N001");
    }
}
