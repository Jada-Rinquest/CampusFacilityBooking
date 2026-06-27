package za.ac.cput.campusfacilitybooking.factory;

/* Author: Nuyra Swanson (221290524)
     Date: 27 June 2026 */

import za.ac.cput.campusfacilitybooking.domain.Notification;
import java.util.UUID;

public class NotificationFactory {

    public static Notification createNotification(String recipientId,
                                                  String message,
                                                  String type,
                                                  String sentDate,
                                                  Boolean isRead) {

        if (recipientId == null || recipientId.isEmpty()
            || message == null || message.isEmpty()
            || type == null || type.isEmpty()
            || sentDate == null || sentDate.isEmpty()) {
            return null;
        }

        String notificationId = UUID.randomUUID().toString();

        return new Notification.Builder()
                .setNotificationId(notificationId)
                .setRecipientId(recipientId)
                .setMessage(message)
                .setType(type)
                .setSentDate(sentDate)
                .setIsRead(isRead)
                .build();
    }

}
