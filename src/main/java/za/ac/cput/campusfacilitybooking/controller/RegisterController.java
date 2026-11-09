package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.service.RegisterService;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService service;

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    // Temporarily retained for the existing registration flow.
    // Must be replaced by an atomic registration operation
    // that generates the registration ID on the server.
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody RegisterRequest request) {

        if (request == null ||
                request.getEmail() == null ||
                request.getEmail().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Email address is required");
        }

        if (request.getRegistrarId() == null ||
                request.getRegistrarId().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Registration ID is required");
        }

        Register register = RegisterFactory.createRegister(
                request.getRegistrarId(),
                request.getEmail().trim(),
                request.getDateRegistered()
        );

        if (register == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid registration details");
        }

        try {
            Register saved = service.create(register);

            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body("Invalid registration details");
        }
    }
}
