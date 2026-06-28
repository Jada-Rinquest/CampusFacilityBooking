package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.factory.TimeSlotFactory;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotFactoryTest {

    @Test
    void createTimeSlot() {

        TimeSlot timeSlot =
                TimeSlotFactory.createTimeSlot(
                        "TS001",
                        "2026-06-28",
                        "09:00",
                        "11:00"
                );

        assertNotNull(timeSlot);

        assertEquals(
                "2026-06-28",
                timeSlot.getDate()
        );
    }
}