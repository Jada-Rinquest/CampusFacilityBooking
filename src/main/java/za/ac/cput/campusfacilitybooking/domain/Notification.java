package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;

import java.time.LocalDate;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private String notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Changed from userId String

    private String message;
    private LocalDate sentDate;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    protected Notification() {
    }

    private Notification(Builder builder) {
        this.notificationId = builder.notificationId;
        this.user = builder.user;
        this.message = builder.message;
        this.sentDate = builder.sentDate;
        this.notificationType = builder.notificationType;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public static class Builder {

        private String notificationId;
        private User user;
        private String message;
        private LocalDate sentDate;
        private NotificationType notificationType;

        public Builder setNotificationId(String notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setSentDate(LocalDate sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                ", sentDate=" + sentDate +
                ", notificationType=" + notificationType +
                '}';
    }
}