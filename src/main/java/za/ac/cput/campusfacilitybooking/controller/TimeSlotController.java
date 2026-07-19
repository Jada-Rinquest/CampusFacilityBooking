package za.ac.cput.campusfacilitybooking.controller;

/* Author: Angelia Van der Westhuizen 221420649
     Date: 19 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.service.TimeSlotService;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotController {

    private final TimeSlotService service;

    @Autowired
    public TimeSlotController(TimeSlotService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TimeSlot> create(@RequestBody TimeSlot timeSlot) {
        TimeSlot createdTimeSlot = service.create(timeSlot);
        return ResponseEntity.ok(createdTimeSlot);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<TimeSlot> read(@PathVariable String id) {
        TimeSlot timeSlot = service.read(id);
        return ResponseEntity.ok(timeSlot);
    }

    @PutMapping("/update")
    public ResponseEntity<TimeSlot> update(@RequestBody TimeSlot timeSlot) {
        TimeSlot updatedTimeSlot = service.update(timeSlot);
        return ResponseEntity.ok(updatedTimeSlot);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }
}
