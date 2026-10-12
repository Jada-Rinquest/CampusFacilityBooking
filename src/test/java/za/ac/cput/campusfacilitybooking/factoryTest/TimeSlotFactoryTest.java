package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.factory.TimeSlotFactory;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotFactoryTest {

    @Test
    void testCreateTimeSlot() {

        TimeSlot timeSlot =
                TimeSlotFactory.createTimeSlot(
                        "TS001",
                        LocalDate.of(2026, 6, 28),
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        assertNotNull(timeSlot);
        assertEquals("TS001", timeSlot.getTimeSlotId());
        assertEquals(
                LocalDate.of(2026, 6, 28),
                timeSlot.getDate()
        );
        assertEquals(
                LocalTime.of(9, 0),
                timeSlot.getStartTime()
        );
        assertEquals(
                LocalTime.of(11, 0),
                timeSlot.getEndTime()
        );
    }

    @Test
    void testCreateTimeSlotWithInvalidTime() {

        assertThrows(IllegalArgumentException.class, () ->
                TimeSlotFactory.createTimeSlot(
                        "TS002",
                        LocalDate.of(2026, 6, 28),
                        LocalTime.of(11, 0),
                        LocalTime.of(9, 0)
                )
        );
    }

    @Test
    void testCreateTimeSlotWithInvalidTimeSlotId() {

        assertThrows(IllegalArgumentException.class, () ->
                TimeSlotFactory.createTimeSlot(
                        "",
                        LocalDate.of(2026, 6, 28),
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                )
        );
    }
}