//Angelia Van der Westhuizen 12/07/2026
//221420649

package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.repository.TimeSlotRepository;
import za.ac.cput.campusfacilitybooking.service.TimeSlotService;

import java.util.Optional;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotRepository repository;

    public TimeSlotServiceImpl(TimeSlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public TimeSlot create(TimeSlot timeSlot) {
        return repository.save(timeSlot);
    }

    @Override
    public TimeSlot read(String id) {
        Optional<TimeSlot> timeSlot = repository.findById(id);
        return timeSlot.orElse(null);
    }

    @Override
    public TimeSlot update(TimeSlot timeSlot) {
        return repository.save(timeSlot);
    }

    @Override
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}