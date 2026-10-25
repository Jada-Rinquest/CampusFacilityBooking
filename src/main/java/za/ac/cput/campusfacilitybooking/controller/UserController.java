package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.enums.AccountType;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;
import za.ac.cput.campusfacilitybooking.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody UserRequest request) {
        if (AccountType.fromEmail(request.getEmail()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Use your CPUT email: @mycput.ac.za for students or @cput.ac.za for staff.");
        }
        if (service.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "An account with this email already exists.");
        }
        User user = UserFactory.createUser(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getDepartmentId()
        );
        return ResponseEntity.ok(service.create(user));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<User> read(@PathVariable String id) {
        User user = service.read(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        Optional<User> user = service.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(service.update(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
