package za.ac.cput.campusfacilitybooking.domain;

/* Author: Nuyra Swanson (221290524)
     Date: 21 June 2026 */

public class Notification {
    private String notificationId;
    private String recipientId;
    private String message;
    private String type;
    private String sentDate;
    private boolean isRead;

    private Notification(Builder builder) {
        this.notificationId = builder.notificationId;
        this.recipientId = builder.recipientId;
        this.message = builder.message;
        this.type = builder.type;
        this.sentDate = builder.sentDate;
        this.isRead = builder.isRead;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getSentDate() {
        return sentDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public static class Builder {
        private String notificationId;
        private String recipientId;
        private String message;
        private String type;
        private String sentDate;
        private boolean isRead;

        public Builder setNotificationId(String notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder setRecipientId(String recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setSentDate(String sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public Builder setIsRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
