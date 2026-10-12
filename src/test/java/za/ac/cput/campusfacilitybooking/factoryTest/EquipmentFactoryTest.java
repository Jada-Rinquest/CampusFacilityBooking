package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.domain.enums.EquipmentStatus;
import za.ac.cput.campusfacilitybooking.factory.EquipmentFactory;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentFactoryTest {

    @Test
    void testCreateEquipment() {
        Equipment equipment = EquipmentFactory.createEquipment(
                "EQUIP001",
                "Projector",
                "SN67890",
                "F001",
                EquipmentStatus.AVAILABLE
        );

        assertNotNull(equipment);
        assertEquals("EQUIP001", equipment.getEquipmentId());
        assertEquals("Projector", equipment.getName());
        assertEquals("SN67890", equipment.getSerialNumber());
        assertEquals("F001", equipment.getFacilityId());
        assertEquals(EquipmentStatus.AVAILABLE, equipment.getEquipmentStatus());
    }

    @Test
    void testCreateEquipmentWithInvalidEquipmentId() {
        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "",
                        "Projector",
                        "SN67890",
                        "F001",
                        EquipmentStatus.AVAILABLE
                )
        );
    }

    @Test
    void testCreateEquipmentWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "EQUIP002",
                        "",
                        "SN67890",
                        "F001",
                        EquipmentStatus.AVAILABLE
                )
        );
    }

    @Test
    void testCreateEquipmentWithInvalidSerialNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "EQUIP003",
                        "Projector",
                        "",
                        "F001",
                        EquipmentStatus.AVAILABLE
                )
        );
    }

    @Test
    void testCreateEquipmentWithInvalidFacilityId() {
        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "EQUIP004",
                        "Projector",
                        "SN67890",
                        "",
                        EquipmentStatus.AVAILABLE
                )
        );
    }

    @Test
    void testCreateEquipmentWithNullEquipmentStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "EQUIP005",
                        "Projector",
                        "SN67890",
                        "F001",
                        null
                )
        );
    }
}