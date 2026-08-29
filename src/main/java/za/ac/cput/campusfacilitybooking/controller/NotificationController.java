package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;
import za.ac.cput.campusfacilitybooking.factory.NotificationFactory;
import za.ac.cput.campusfacilitybooking.service.NotificationService;

import java.time.LocalDate;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Notification> create(@RequestBody NotificationRequest request) {
        Notification notification = NotificationFactory.createNotification(
                request.getNotificationId(),
                request.getUserId(),
                request.getMessage(),
                request.getSentDate(),
                request.getNotificationType()
        );

        return ResponseEntity.ok(service.create(notification));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Notification> read(@PathVariable String id) {
        Notification notification = service.read(id);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(notification);
    }

    @PutMapping("/update")
    public ResponseEntity<Notification> update(@RequestBody Notification notification) {
        Notification updatedNotification = service.update(notification);
        return ResponseEntity.ok(updatedNotification);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }
}

class NotificationRequest {
    private String notificationId;
    private String userId;
    private String message;
    private LocalDate sentDate;
    private NotificationType notificationType;

    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDate getSentDate() { return sentDate; }
    public void setSentDate(LocalDate sentDate) { this.sentDate = sentDate; }
    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
}