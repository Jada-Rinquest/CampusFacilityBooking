package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;

import java.time.LocalDate;

public class NotificationFactory {

    public static Notification createNotification(
            String notificationId,
            User user,
            String message,
            LocalDate sentDate,
            NotificationType notificationType) {

        if (notificationId == null || notificationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification ID is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }

        if (sentDate == null) {
            throw new IllegalArgumentException("Sent date is required");
        }

        if (notificationType == null) {
            throw new IllegalArgumentException("Notification type is required");
        }

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setUser(user)
                .setMessage(message)
                .setSentDate(sentDate)
                .setNotificationType(notificationType)
                .build();
    }
}