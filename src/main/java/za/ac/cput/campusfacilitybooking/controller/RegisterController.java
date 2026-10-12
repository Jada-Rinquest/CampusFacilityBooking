package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.service.RegisterService;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService service;

    @Autowired
    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Register> create(@RequestBody RegisterRequest request) {
        Register register = RegisterFactory.createRegister(
                request.getRegistrarId(),
                request.getEmail(),
                request.getDateRegistered()
        );
        return ResponseEntity.ok(service.create(register));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Register>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Register> read(@PathVariable String id) {
        Register register = service.read(id);
        if (register == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(register);
    }

    @PutMapping("/update")
    public ResponseEntity<Register> update(@RequestBody Register register) {
        return ResponseEntity.ok(service.update(register));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
