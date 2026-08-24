package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService service;

    @Autowired
    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Contact> create(@RequestBody ContactRequest request) {
        Contact contact = ContactFactory.createContact(
                request.getContactId(),
                request.getContact(),
                request.getDescription(),
                request.getUserId()
        );
        return ResponseEntity.ok(service.create(contact));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Contact> read(@PathVariable String id) {
        Contact contact = service.read(id);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);
    }

    @PutMapping("/update")
    public ResponseEntity<Contact> update(@RequestBody Contact contact) {
        return ResponseEntity.ok(service.update(contact));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

class ContactRequest {
    private String contactId;
    private String contact;
    private String description;
    private String userId;

    // Getters and Setters
    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}