package za.ac.cput.campusfacilitybooking.service.impl;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.repository.StaffRepository;
import za.ac.cput.campusfacilitybooking.service.StaffService;

import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository repository;

    public StaffServiceImpl(StaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Staff create(Staff staff) {
        return repository.save(staff);
    }

    @Override
    public Staff read(String id) {
        Optional<Staff> staff = repository.findById(id);
        return staff.orElse(null);
    }

    @Override
    public Staff update(Staff staff) {
        return repository.save(staff);
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