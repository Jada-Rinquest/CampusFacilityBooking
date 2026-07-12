package za.ac.cput.campusfacilitybooking.service.impl;

/* Author: Nuyra Swanson (221290524)
     Date: 12 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.repository.NotificationRepository;
import za.ac.cput.campusfacilitybooking.service.NotificationService;

import java.util.Optional;

@Service
public class NotificationServiceImpl  implements NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification create(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public Notification read(String notificationId) {
        Optional<Notification> notification = repository.findById(notificationId);
        return notification.orElse(null);
    }

    @Override
    public Notification update(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public boolean delete(String notificationId) {
        if (repository.existsById(notificationId)) {
            repository.deleteById(notificationId);
            return true;
        }
        return false;
    }
}
