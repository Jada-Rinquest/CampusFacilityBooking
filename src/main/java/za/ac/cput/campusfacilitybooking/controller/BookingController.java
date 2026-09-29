package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> create(@RequestBody BookingRequest request) {
        Booking booking = BookingFactory.createBooking(
                request.getBookingId(),
                request.getFacilityId(),
                request.getTimeSlotId(),
                request.getUserId(),
                request.getPurpose(),
                request.getBookingStatus()
        );

        return ResponseEntity.ok(bookingService.create(booking));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Booking> read(@PathVariable String id) {
        Booking booking = bookingService.read(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/update")
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.update(booking));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.delete(id));
    }
}

// Remove 'public' keyword from this class
