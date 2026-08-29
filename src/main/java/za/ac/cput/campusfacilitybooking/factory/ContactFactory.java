package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Contact;

public class ContactFactory {

    public static Contact createContact(String contactId,
                                        String contact,
                                        String description,
                                        String userId) {

        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("Contact ID is required");
        }

        if (contact == null || contact.isEmpty()) {
            throw new IllegalArgumentException("Contact is required");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        return new Contact(
                contactId,
                contact,
                description,
                userId
        );
    }
}