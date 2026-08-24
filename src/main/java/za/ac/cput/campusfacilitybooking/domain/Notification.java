package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private String notificationId;

    private String userId;
    private String message;
    private LocalDate sentDate;
    private String notificationTypeId;

    protected Notification() {
    }

    private Notification(Builder builder) {
        this.notificationId = builder.notificationId;
        this.userId = builder.userId;
        this.message = builder.message;
        this.sentDate = builder.sentDate;
        this.notificationTypeId = builder.notificationTypeId;
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

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public static class Builder {

        private String notificationId;
        private String userId;
        private String message;
        private LocalDate sentDate;
        private String notificationTypeId;

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

        public Builder setNotificationTypeId(String notificationTypeId) {
            this.notificationTypeId = notificationTypeId;
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
                ", notificationTypeId='" + notificationTypeId + '\'' +
                '}';
    }
}