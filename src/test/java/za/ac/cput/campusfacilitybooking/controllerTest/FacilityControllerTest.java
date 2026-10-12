package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.FacilityController;
import za.ac.cput.campusfacilitybooking.controller.FacilityRequest;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;
import za.ac.cput.campusfacilitybooking.factory.FacilityFactory;
import za.ac.cput.campusfacilitybooking.service.FacilityService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacilityControllerTest {

    private FacilityService service;
    private FacilityController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(FacilityService.class);
        controller = new FacilityController(service);
    }

    @Test
    void testCreate() {
        FacilityRequest request = new FacilityRequest();
        request.setFacilityId("F001");
        request.setName("Computer Lab");
        request.setCapacity(40);
        request.setLocation("Block A");
        request.setDepartmentId("D001");
        request.setFacilityType(FacilityType.LAB);

        Facility facility = FacilityFactory.createFacility(
                request.getFacilityId(),
                request.getName(),
                request.getCapacity(),
                request.getLocation(),
                request.getDepartmentId(),
                request.getFacilityType()
        );

        when(service.create(any(Facility.class))).thenReturn(facility);

        ResponseEntity<Facility> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("F001", response.getBody().getFacilityId());
        assertEquals(FacilityType.LAB, response.getBody().getFacilityType());
    }

    @Test
    void testRead() {
        Facility facility = FacilityFactory.createFacility(
                "F001",
                "Computer Lab",
                40,
                "Block A",
                "D001",
                FacilityType.LAB
        );

        when(service.read("F001")).thenReturn(facility);

        ResponseEntity<Facility> response = controller.read("F001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("F001", response.getBody().getFacilityId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("F999")).thenReturn(null);

        ResponseEntity<Facility> response = controller.read("F999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Facility facility = FacilityFactory.createFacility(
                "F001",
                "Updated Lab",
                50,
                "Block B",
                "D001",
                FacilityType.LAB
        );

        when(service.update(facility)).thenReturn(facility);

        ResponseEntity<Facility> response = controller.update(facility);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Lab", response.getBody().getName());
    }

    @Test
    void testDelete() {
        when(service.delete("F001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("F001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("F001");
    }
}