//Jada Rinquest 12/07/2026
//222871296

package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.repository.DepartmentRepository;
import za.ac.cput.campusfacilitybooking.service.DepartmentService;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department create(Department department) {
        return repository.save(department);
    }

    @Override
    public Department read(String id) {
        Optional<Department> department = repository.findById(id);
        return department.orElse(null);
    }

    @Override
    public Department update(Department department) {
        return repository.save(department);
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
