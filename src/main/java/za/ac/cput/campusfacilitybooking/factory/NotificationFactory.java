package za.ac.cput.campusfacilitybooking.factory;

/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Notification;

import java.time.LocalDate;
import java.util.UUID;

public class NotificationFactory {

    public static Notification createNotification(
            String userId,
            String message,
            LocalDate sentDate,
            String notificationTypeId) {

        if (userId == null || userId.isEmpty()
                || message == null || message.isEmpty()
                || sentDate == null
                || notificationTypeId == null || notificationTypeId.isEmpty()) {

            return null;
        }

        String notificationId = UUID.randomUUID().toString();

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setUserId(userId)
                .setMessage(message)
                .setSentDate(sentDate)
                .setNotificationTypeId(notificationTypeId)
                .build();
    }
}