//Angelia Van der Westhuizen 12/07/2026
//221420649

package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.repository.EquipmentRepository;
import za.ac.cput.campusfacilitybooking.service.EquipmentService;

import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository repository;

    public EquipmentServiceImpl(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Equipment create(Equipment equipment) {
        return repository.save(equipment);
    }

    @Override
    public Equipment read(String id) {
        Optional<Equipment> equipment = repository.findById(id);
        return equipment.orElse(null);
    }

    @Override
    public Equipment update(Equipment equipment) {
        return repository.save(equipment);
    }

    @Override
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}