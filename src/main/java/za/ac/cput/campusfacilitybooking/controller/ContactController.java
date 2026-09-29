package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;
import za.ac.cput.campusfacilitybooking.service.ContactService;

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

    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(service.getAll());
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
