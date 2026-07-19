package za.ac.cput.campusfacilitybooking.controller;

/* Author: Milani Sani (230371574)
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService service;

    @Autowired
    public StaffController(StaffService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Staff> create(@RequestBody Staff staff) {
        Staff createdStaff = service.create(staff);
        return ResponseEntity.ok(createdStaff);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Staff> read(@PathVariable String id) {
        Staff staff = service.read(id);

        if (staff == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(staff);
    }

    @PutMapping("/update")
    public ResponseEntity<Staff> update(@RequestBody Staff staff) {
        Staff updatedStaff = service.update(staff);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }
}
