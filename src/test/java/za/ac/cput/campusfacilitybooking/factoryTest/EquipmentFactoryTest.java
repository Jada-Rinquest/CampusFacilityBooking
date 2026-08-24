package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.Equipment;
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
                "ES001"
        );

        assertNotNull(equipment);
        assertEquals("EQUIP001", equipment.getEquipmentId());
        assertEquals("Projector", equipment.getName());
        assertEquals("SN67890", equipment.getSerialNumber());
        assertEquals("F001", equipment.getFacilityId());
        assertEquals("ES001", equipment.getEquipmentStatusId());
    }

    @Test
    void testCreateEquipmentWithInvalidEquipmentId() {

        assertThrows(IllegalArgumentException.class, () ->
                EquipmentFactory.createEquipment(
                        "",
                        "Projector",
                        "SN67890",
                        "F001",
                        "ES001"
                )
        );
    }
}