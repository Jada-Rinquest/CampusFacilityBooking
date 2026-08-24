package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.domain.User;

public class ContactFactory {

    public static Contact createContact(String contactId,
                                        String contact,
                                        String description,
                                        User user) {

        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("Contact ID is required");
        }

        if (contact == null || contact.isEmpty()) {
            throw new IllegalArgumentException("Contact is required");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        return new Contact(
                contactId,
                contact,
                description,
                user
        );
    }
}
