//Angelia Van der Westhuizen 12/07/2026
//221420649

package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.repository.TimeSlotRepository;
import za.ac.cput.campusfacilitybooking.service.TimeSlotService;
import za.ac.cput.campusfacilitybooking.service.impl.TimeSlotServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TimeSlotServiceTest {

    private TimeSlotRepository repository;
    private TimeSlotService service;
    private TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TimeSlotRepository.class);
        service = new TimeSlotServiceImpl(repository);

        timeSlot = new TimeSlot.Builder()
                .setTimeSlotId("TS001")
                .setDate("2026-07-12")
                .setStartTime("09:00")
                .setEndTime("13:00")
                .build();
    }

    @Test
    void create() {
        when(repository.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot created = service.create(timeSlot);

        assertNotNull(created);
        assertEquals("TS001", created.getTimeSlotId());
    }

    @Test
    void read() {
        when(repository.findById("TS001"))
                .thenReturn(Optional.of(timeSlot));

        TimeSlot found = service.read("TS001");

        assertNotNull(found);
        assertEquals("2026-07-12", found.getDate());
    }

    @Test
    void update() {
        when(repository.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot updated = service.update(timeSlot);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(repository.existsById("TS001")).thenReturn(true);

        boolean deleted = service.delete("TS001");

        assertTrue(deleted);

        verify(repository).deleteById("TS001");
    }
}