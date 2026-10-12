package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    private String contactId;
    private String contact;
    private String description;
    private String userId;

    protected Contact() {
    }

    public Contact(String contactId, String contact,
                   String description, String userId) {
        this.contactId = contactId;
        this.contact = contact;
        this.description = description;
        this.userId = userId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId='" + contactId + '\'' +
                ", contact='" + contact + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    private Contact(Builder builder) {
        this.contactId = builder.contactId;
        this.contact = builder.contact;
        this.description = builder.description;
        this.userId = builder.userId;
    }

    public static class Builder {
        private String contactId;
        private String contact;
        private String description;
        private String userId;

        public Builder setContactId(String contactId) {
            this.contactId = contactId;
            return this;
        }

        public Builder setContact(String contact) {
            this.contact = contact;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}