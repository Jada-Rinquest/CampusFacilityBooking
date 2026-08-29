package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Notification;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;

import java.time.LocalDate;

public class NotificationFactory {

    public static Notification createNotification(
            String notificationId,
            String userId,
            String message,
            LocalDate sentDate,
            NotificationType notificationType) {

        if (notificationId == null || notificationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification ID is required");
        }

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
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
                .setUserId(userId)
                .setMessage(message)
                .setSentDate(sentDate)
                .setNotificationType(notificationType)
                .build();
    }
}