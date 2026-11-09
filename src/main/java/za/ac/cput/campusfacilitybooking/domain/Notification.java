
package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.NotificationType;

import java.time.LocalDate;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private String notificationId;

    private String userId;
    private String message;
    private LocalDate sentDate;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    protected Notification() {
    }

    private Notification(Builder builder) {
        this.notificationId = builder.notificationId;
        this.userId = builder.userId;
        this.message = builder.message;
        this.sentDate = builder.sentDate;
        this.notificationType = builder.notificationType;
        this.read = builder.read;
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

    public LocalDate getSentDate() {
        return sentDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public static class Builder {

        private String notificationId;
        private String userId;
        private String message;
        private LocalDate sentDate;
        private NotificationType notificationType;
        private boolean read = false;

        public Builder setNotificationId(String notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
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

        public Builder setNotificationType(
                NotificationType notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public Builder setRead(boolean read) {
            this.read = read;
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
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", sentDate=" + sentDate +
                ", notificationType=" + notificationType +
                ", read=" + read +
                '}';
    }
}
