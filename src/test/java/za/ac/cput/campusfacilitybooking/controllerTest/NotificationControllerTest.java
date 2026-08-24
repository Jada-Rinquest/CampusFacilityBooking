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
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;
import za.ac.cput.campusfacilitybooking.service.NotificationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        NotificationRequest request = new NotificationRequest();
        request.setNotificationId("N001");
        request.setUserId("U001");
        request.setMessage("Booking Approved");
        request.setSentDate(LocalDate.of(2026, 7, 19));
        request.setNotificationType(NotificationType.BOOKING_CONFIRMATION);

        Notification notification = NotificationFactory.createNotification(
                request.getNotificationId(),
                request.getUserId(),
                request.getMessage(),
                request.getSentDate(),
                request.getNotificationType()
        );

        when(service.create(any(Notification.class))).thenReturn(notification);

        ResponseEntity<Notification> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("N001", response.getBody().getNotificationId());
        assertEquals(NotificationType.BOOKING_CONFIRMATION, response.getBody().getNotificationType());
    }

    @Test
    void testRead() {
        Notification notification = NotificationFactory.createNotification(
                "N001",
                "U001",
                "Booking Approved",
                LocalDate.of(2026, 7, 19),
                NotificationType.BOOKING_CONFIRMATION
        );

        when(service.read("N001")).thenReturn(notification);

        ResponseEntity<Notification> response = controller.read("N001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("N001", response.getBody().getNotificationId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("N999")).thenReturn(null);

        ResponseEntity<Notification> response = controller.read("N999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Notification notification = NotificationFactory.createNotification(
                "N001",
                "U001",
                "Booking Updated",
                LocalDate.of(2026, 7, 19),
                NotificationType.BOOKING_CONFIRMATION
        );

        when(service.update(notification)).thenReturn(notification);

        ResponseEntity<Notification> response = controller.update(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Booking Updated", response.getBody().getMessage());
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