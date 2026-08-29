package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;
import za.ac.cput.campusfacilitybooking.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService service;

    @Autowired
    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Login> create(@RequestBody LoginRequest request) {
        Login login = LoginFactory.createLogin(
                request.getLoginId(),
                request.getRegistrarId(),
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok(service.create(login));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Login> read(@PathVariable String id) {
        Login login = service.read(id);
        if (login == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(login);
    }

    @PutMapping("/update")
    public ResponseEntity<Login> update(@RequestBody Login login) {
        return ResponseEntity.ok(service.update(login));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Login> authenticate(@RequestBody LoginRequest request) {

        Login login = service.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (login == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(login);
    }
}

class LoginRequest {
    private String loginId;
    private String registrarId;
    private String username;
    private String password;

    // Getters and Setters
    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getRegistrarId() { return registrarId; }
    public void setRegistrarId(String registrarId) { this.registrarId = registrarId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}