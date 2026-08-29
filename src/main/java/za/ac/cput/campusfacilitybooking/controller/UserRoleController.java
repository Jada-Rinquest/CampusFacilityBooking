package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;
import za.ac.cput.campusfacilitybooking.service.UserRoleService;

@RestController
@RequestMapping("/userrole")
public class UserRoleController {

    private final UserRoleService service;

    @Autowired
    public UserRoleController(UserRoleService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<UserRole> create(@RequestBody UserRoleRequest request) {
        UserRole userRole = UserRoleFactory.createUserRole(
                request.getUserRoleId(),
                request.getUserId(),
                request.getRole()
        );
        return ResponseEntity.ok(service.create(userRole));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<UserRole> read(@PathVariable String id) {
        UserRole userRole = service.read(id);
        if (userRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRole);
    }

    @PutMapping("/update")
    public ResponseEntity<UserRole> update(@RequestBody UserRole userRole) {
        return ResponseEntity.ok(service.update(userRole));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

class UserRoleRequest {
    private String userRoleId;
    private String userId;
    private Role role;

    // Getters and Setters
    public String getUserRoleId() { return userRoleId; }
    public void setUserRoleId(String userRoleId) { this.userRoleId = userRoleId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}