package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;
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

class FacilityRequest {
    private String facilityId;
    private String name;
    private int capacity;
    private String location;
    private String departmentId;
    private FacilityType facilityType;

    // Getters and Setters
    public String getFacilityId() { return facilityId; }
    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public FacilityType getFacilityType() { return facilityType; }
    public void setFacilityType(FacilityType facilityType) { this.facilityType = facilityType; }
}