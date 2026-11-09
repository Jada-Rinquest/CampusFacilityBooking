
package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;
import za.ac.cput.campusfacilitybooking.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // CREATE NOTIFICATION
    @PostMapping("/create")
    public ResponseEntity<Notification> create(
            @RequestBody NotificationRequest request) {

        Notification notification = NotificationFactory.createNotification(
                request.getNotificationId(),
                request.getUserId(),
                request.getMessage(),
                request.getSentDate(),
                request.getNotificationType()
        );

        return ResponseEntity.ok(service.create(notification));
    }

    // GET ALL NOTIFICATIONS
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET NOTIFICATION BY ID
    @GetMapping("/read/{id}")
    public ResponseEntity<Notification> read(
            @PathVariable String id) {

        Notification notification = service.read(id);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(notification);
    }

    // UPDATE NOTIFICATION
    @PutMapping("/update")
    public ResponseEntity<Notification> update(
            @RequestBody Notification notification) {

        Notification updatedNotification = service.update(notification);

        return ResponseEntity.ok(updatedNotification);
    }

    // MARK NOTIFICATION AS READ
    @PreAuthorize("@ownership.ownsNotification(#id, authentication)")
    @PutMapping("/mark-read/{id}")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable String id) {

        Notification notification = service.read(id);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        notification.setRead(true);

        Notification updatedNotification = service.update(notification);

        return ResponseEntity.ok(updatedNotification);
    }

    // DELETE NOTIFICATION
    @PreAuthorize("@ownership.ownsNotification(#id, authentication)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable String id) {

        boolean deleted = service.delete(id);

        return ResponseEntity.ok(deleted);
    }
}
