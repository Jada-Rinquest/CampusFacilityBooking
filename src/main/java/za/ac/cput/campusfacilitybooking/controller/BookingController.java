package za.ac.cput.campusfacilitybooking.controller;

/* Author: Nuyra Swanson (221290524)
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        return ResponseEntity.ok(service.create(booking));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Booking> read(@PathVariable String id) {
        Booking booking = service.read(id);

        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/update")
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        return ResponseEntity.ok(service.update(booking));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }


}
