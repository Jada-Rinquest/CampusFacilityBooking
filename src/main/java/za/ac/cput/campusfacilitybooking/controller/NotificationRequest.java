package za.ac.cput.campusfacilitybooking.controller;

import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;
import java.time.LocalDate;

public class NotificationRequest {
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
