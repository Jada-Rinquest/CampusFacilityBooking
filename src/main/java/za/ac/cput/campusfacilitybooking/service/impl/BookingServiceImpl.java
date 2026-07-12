package za.ac.cput.campusfacilitybooking.service.impl;

/* Author: Nuyra Swanson (221290524)
     Date: 12 July 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.repository.BookingRepository;
import za.ac.cput.campusfacilitybooking.service.BookingService;


import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    @Autowired
    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Booking create(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public Booking read(String bookingId) {
        Optional<Booking> booking = repository.findById(bookingId);
        return booking.orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public boolean delete(String bookingId) {
        if (repository.existsById(bookingId)) {
            repository.deleteById(bookingId);
            return true;
        }
        return false;
    }
}
