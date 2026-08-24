package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    private String contactId;

    private String contact;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Contact() {
    }

    public Contact(String contactId, String contact,
                   String description, User user) {
        this.contactId = contactId;
        this.contact = contact;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId='" + contactId + '\'' +
                ", contact='" + contact + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }
}