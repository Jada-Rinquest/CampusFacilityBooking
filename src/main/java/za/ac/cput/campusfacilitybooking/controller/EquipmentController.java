package za.ac.cput.campusfacilitybooking.controller;

/* Author: Angelia Van der Westhuizen 221420649
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.service.EquipmentService;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService service;

    @Autowired
    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Equipment> create(@RequestBody Equipment equipment) {
        Equipment createdEquipment = service.create(equipment);
        return ResponseEntity.ok(createdEquipment);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Equipment> read(@PathVariable String id) {
        Equipment equipment = service.read(id);
        return ResponseEntity.ok(equipment);
    }

    @PutMapping("/update")
    public ResponseEntity<Equipment> update(@RequestBody Equipment equipment) {
        Equipment updatedEquipment = service.update(equipment);
        return ResponseEntity.ok(updatedEquipment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }
}