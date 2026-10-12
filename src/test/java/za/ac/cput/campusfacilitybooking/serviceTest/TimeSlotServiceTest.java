package za.ac.cput.campusfacilitybooking.serviceTest;

//Angelia Van der Westhuizen 12/07/2026
//221420649

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.factory.TimeSlotFactory;
import za.ac.cput.campusfacilitybooking.repository.TimeSlotRepository;
import za.ac.cput.campusfacilitybooking.service.TimeSlotService;
import za.ac.cput.campusfacilitybooking.service.impl.TimeSlotServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
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

        timeSlot = TimeSlotFactory.createTimeSlot(
                "TS001",
                LocalDate.of(2026, 7, 12),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0)
        );
    }

    @Test
    void testCreate() {
        when(repository.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot created = service.create(timeSlot);

        assertNotNull(created);
        assertEquals("TS001", created.getTimeSlotId());
    }

    @Test
    void testRead() {
        when(repository.findById("TS001"))
                .thenReturn(Optional.of(timeSlot));

        TimeSlot found = service.read("TS001");

        assertNotNull(found);
        assertEquals(LocalDate.of(2026, 7, 12), found.getDate());
    }

    @Test
    void testUpdate() {
        when(repository.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot updated = service.update(timeSlot);

        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        when(repository.existsById("TS001")).thenReturn(true);

        boolean deleted = service.delete("TS001");

        assertTrue(deleted);
        verify(repository).deleteById("TS001");
    }
}