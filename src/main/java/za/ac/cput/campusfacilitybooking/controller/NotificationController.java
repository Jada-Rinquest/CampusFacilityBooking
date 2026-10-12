package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
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
