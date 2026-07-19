//Angelia Van der Westhuizen 12/07/2026
//221420649

package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
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

        equipment = new Equipment.Builder()
                .setEquipmentId("EQUIP001")
                .setName("Projector")
                .setSerialNumber("SN67890")
                .setStatus("Available")
                .setFacility("Hall A")
                .build();
    }

    @Test
    void create() {
        when(repository.save(equipment)).thenReturn(equipment);

        Equipment created = service.create(equipment);

        assertNotNull(created);
        assertEquals("EQUIP001", created.getEquipmentId());
    }

    @Test
    void read() {
        when(repository.findById("EQUIP001"))
                .thenReturn(Optional.of(equipment));

        Equipment found = service.read("EQUIP001");

        assertNotNull(found);
        assertEquals("Projector", found.getName());
    }

    @Test
    void update() {
        when(repository.save(equipment)).thenReturn(equipment);

        Equipment updated = service.update(equipment);

        assertNotNull(updated);
    }

    @Test
    void delete() {
        when(repository.existsById("EQUIP001")).thenReturn(true);

        boolean deleted = service.delete("EQUIP001");

        assertTrue(deleted);

        verify(repository).deleteById("EQUIP001");
    }
}
