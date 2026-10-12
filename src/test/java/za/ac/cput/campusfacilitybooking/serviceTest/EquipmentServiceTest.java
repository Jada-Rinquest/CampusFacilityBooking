package za.ac.cput.campusfacilitybooking.serviceTest;

//Angelia Van der Westhuizen 12/07/2026
//221420649

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.domain.enums.EquipmentStatus;
import za.ac.cput.campusfacilitybooking.factory.EquipmentFactory;
import za.ac.cput.campusfacilitybooking.repository.EquipmentRepository;
import za.ac.cput.campusfacilitybooking.service.EquipmentService;
import za.ac.cput.campusfacilitybooking.service.impl.EquipmentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentServiceTest {

    private EquipmentRepository repository;
    private EquipmentService service;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(EquipmentRepository.class);
        service = new EquipmentServiceImpl(repository);

        equipment = EquipmentFactory.createEquipment(
                "EQUIP001",
                "Projector",
                "SN67890",
                "F001",
                EquipmentStatus.AVAILABLE
        );
    }

    @Test
    void testCreate() {
        when(repository.save(equipment)).thenReturn(equipment);

        Equipment created = service.create(equipment);

        assertNotNull(created);
        assertEquals("EQUIP001", created.getEquipmentId());
    }

    @Test
    void testRead() {
        when(repository.findById("EQUIP001"))
                .thenReturn(Optional.of(equipment));

        Equipment found = service.read("EQUIP001");

        assertNotNull(found);
        assertEquals("Projector", found.getName());
    }

    @Test
    void testUpdate() {
        when(repository.save(equipment)).thenReturn(equipment);

        Equipment updated = service.update(equipment);

        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        when(repository.existsById("EQUIP001")).thenReturn(true);

        boolean deleted = service.delete("EQUIP001");

        assertTrue(deleted);
        verify(repository).deleteById("EQUIP001");
    }
}