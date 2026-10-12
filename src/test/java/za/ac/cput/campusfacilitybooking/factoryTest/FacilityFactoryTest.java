package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;
import za.ac.cput.campusfacilitybooking.factory.FacilityFactory;

import static org.junit.jupiter.api.Assertions.*;

class FacilityFactoryTest {

    @Test
    void testCreateFacility() {
        Facility facility = FacilityFactory.createFacility(
                "F001",
                "Computer Lab",
                40,
                "Block A",
                "D001",
                FacilityType.LAB
        );

        assertNotNull(facility);
        assertEquals("F001", facility.getFacilityId());
        assertEquals("Computer Lab", facility.getName());
        assertEquals(40, facility.getCapacity());
        assertEquals("Block A", facility.getLocation());
        assertEquals("D001", facility.getDepartmentId());
        assertEquals(FacilityType.LAB, facility.getFacilityType());
    }

    @Test
    void testCreateFacilityWithInvalidFacilityId() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "",
                        "Computer Lab",
                        40,
                        "Block A",
                        "D001",
                        FacilityType.LAB
                )
        );
    }

    @Test
    void testCreateFacilityWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "F002",
                        "",
                        40,
                        "Block A",
                        "D001",
                        FacilityType.LAB
                )
        );
    }

    @Test
    void testCreateFacilityWithInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "F003",
                        "Computer Lab",
                        0,
                        "Block A",
                        "D001",
                        FacilityType.LAB
                )
        );
    }

    @Test
    void testCreateFacilityWithInvalidLocation() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "F004",
                        "Computer Lab",
                        40,
                        "",
                        "D001",
                        FacilityType.LAB
                )
        );
    }

    @Test
    void testCreateFacilityWithInvalidDepartmentId() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "F005",
                        "Computer Lab",
                        40,
                        "Block A",
                        "",
                        FacilityType.LAB
                )
        );
    }

    @Test
    void testCreateFacilityWithNullFacilityType() {
        assertThrows(IllegalArgumentException.class, () ->
                FacilityFactory.createFacility(
                        "F006",
                        "Computer Lab",
                        40,
                        "Block A",
                        "D001",
                        null
                )
        );
    }
}