package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.factory.EquipmentFactory;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentFactoryTest {

    @Test
    void createEquipment() {

        Equipment equipment = EquipmentFactory.createEquipment(
                "EQUIP001",
                "Projector",
                "SN67890",
                "Available",
                "Hall A"
        );


        assertNotNull(equipment);

        assertEquals(
                "Projector",
                equipment.getName()
        );
    }
}