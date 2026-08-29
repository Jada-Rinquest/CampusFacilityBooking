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
}