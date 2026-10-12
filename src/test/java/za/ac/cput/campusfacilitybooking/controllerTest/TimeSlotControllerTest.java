package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Angelia Van der Westhuizen 221420649
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.TimeSlotController;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;
import za.ac.cput.campusfacilitybooking.factory.TimeSlotFactory;
import za.ac.cput.campusfacilitybooking.service.TimeSlotService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TimeSlotControllerTest {

    private TimeSlotService service;
    private TimeSlotController controller;
    private TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(TimeSlotService.class);
        controller = new TimeSlotController(service);

        timeSlot = TimeSlotFactory.createTimeSlot(
                "TS001",
                LocalDate.of(2026, 7, 19),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0)
        );
    }

    @Test
    void testCreate() {
        when(service.create(timeSlot)).thenReturn(timeSlot);

        ResponseEntity<TimeSlot> response = controller.create(timeSlot);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TS001", response.getBody().getTimeSlotId());
    }

    @Test
    void testRead() {
        when(service.read("TS001")).thenReturn(timeSlot);

        ResponseEntity<TimeSlot> response = controller.read("TS001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.of(2026, 7, 19), response.getBody().getDate());
    }

    @Test
    void testReadNotFound() {
        when(service.read("TS999")).thenReturn(null);

        ResponseEntity<TimeSlot> response = controller.read("TS999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        when(service.update(timeSlot)).thenReturn(timeSlot);

        ResponseEntity<TimeSlot> response = controller.update(timeSlot);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDelete() {
        when(service.delete("TS001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("TS001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("TS001");
    }
}