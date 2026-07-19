package za.ac.cput.campusfacilitybooking.controller;

/* Author: Milani Sani (230371574)
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.service.MaintenanceRequestService;

@RestController
@RequestMapping("/maintenancerequest")
public class MaintenanceRequestController {

    private final MaintenanceRequestService service;

    @Autowired
    public MaintenanceRequestController(MaintenanceRequestService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<MaintenanceRequest> create(@RequestBody MaintenanceRequest maintenanceRequest) {
        MaintenanceRequest createdRequest = service.create(maintenanceRequest);
        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<MaintenanceRequest> read(@PathVariable String id) {
        MaintenanceRequest maintenanceRequest = service.read(id);

        if (maintenanceRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maintenanceRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<MaintenanceRequest> update(@RequestBody MaintenanceRequest maintenanceRequest) {
        MaintenanceRequest updatedRequest = service.update(maintenanceRequest);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }
}