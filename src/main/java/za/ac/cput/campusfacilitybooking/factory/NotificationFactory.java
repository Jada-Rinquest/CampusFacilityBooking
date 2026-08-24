
/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Notification;

import java.time.LocalDate;

public class NotificationFactory {

    public static Notification createNotification(
            String notificationId,
            String userId,
            String message,
            LocalDate sentDate,
            String notificationTypeId) {

        if (notificationId == null || notificationId.isEmpty()) {
            throw new IllegalArgumentException("Notification ID is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }

        if (sentDate == null) {
            throw new IllegalArgumentException("Sent date is required");
        }

        if (notificationTypeId == null || notificationTypeId.isEmpty()) {
            throw new IllegalArgumentException("Notification type ID is required");
        }

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setUserId(userId)
                .setMessage(message)
                .setSentDate(sentDate)
                .setNotificationTypeId(notificationTypeId)
                .build();
    }
}