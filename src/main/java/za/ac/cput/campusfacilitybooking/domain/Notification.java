package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private String notificationId;

    private String userId;
    private String message;
    private LocalDateTime sentDate;
    private String notificationTypeId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_type_id",
            insertable = false, updatable = false)
    private NotificationType notificationType;

    protected Notification() {
    }

    public Notification(String notificationId, String userId,
                        String message, LocalDateTime sentDate,
                        String notificationTypeId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.sentDate = sentDate;
        this.notificationTypeId = notificationTypeId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }
}