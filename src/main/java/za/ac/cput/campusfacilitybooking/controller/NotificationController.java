package za.ac.cput.campusfacilitybooking.controller;

/* Author: Nuyra Swanson (221290524)
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Notification;
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
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        Notification createdNotification = service.create(notification);
        return ResponseEntity.ok(createdNotification);
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
