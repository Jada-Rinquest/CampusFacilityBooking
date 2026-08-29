package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.factory.AddressFactory;
import za.ac.cput.campusfacilitybooking.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService service;

    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Address> create(@RequestBody AddressRequest request) {
        Address address = AddressFactory.createAddress(
                request.getAddressId(),
                request.getAddress(),
                request.getDescription(),
                request.getUserId()
        );
        return ResponseEntity.ok(service.create(address));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Address> read(@PathVariable String id) {
        Address address = service.read(id);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(address);
    }

    @PutMapping("/update")
    public ResponseEntity<Address> update(@RequestBody Address address) {
        return ResponseEntity.ok(service.update(address));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

class AddressRequest {
    private String addressId;
    private String address;
    private String description;
    private String userId;

    // Getters and Setters
    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}