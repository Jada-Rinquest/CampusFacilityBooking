package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.factory.FacilityFactory;
import za.ac.cput.campusfacilitybooking.service.FacilityService;

@RestController
@RequestMapping("/facility")
public class FacilityController {

    private final FacilityService service;

    @Autowired
    public FacilityController(FacilityService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Facility> create(@RequestBody FacilityRequest request) {
        Facility facility = FacilityFactory.createFacility(
                request.getFacilityId(),
                request.getName(),
                request.getCapacity(),
                request.getLocation(),
                request.getDepartmentId(),
                request.getFacilityType()
        );
        return ResponseEntity.ok(service.create(facility));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Facility>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Facility> read(@PathVariable String id) {
        Facility facility = service.read(id);
        if (facility == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facility);
    }

    @PutMapping("/update")
    public ResponseEntity<Facility> update(@RequestBody Facility facility) {
        return ResponseEntity.ok(service.update(facility));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
