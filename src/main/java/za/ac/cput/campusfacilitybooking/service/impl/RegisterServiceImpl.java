
package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;

import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.service.RegisterService;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository repository;

    public RegisterServiceImpl(RegisterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Register create(Register register) {

        if (register == null) {
            throw new IllegalArgumentException(
                    "Registration details are required"
            );
        }

        if (register.getRegistrarId() == null ||
                register.getRegistrarId().isBlank()) {

            throw new IllegalArgumentException(
                    "Registration ID is required"
            );
        }

        if (register.getEmail() == null ||
                register.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email address is required"
            );
        }

        if (repository.existsById(register.getRegistrarId())) {
            throw new IllegalArgumentException(
                    "Registration ID already exists"
            );
        }

        return repository.save(register);
    }

    @Override
    public Register read(String id) {

        Optional<Register> register = repository.findById(id);

        return register.orElse(null);
    }

    @Override
    public Register update(Register register) {

        if (register == null ||
                register.getRegistrarId() == null) {

            throw new IllegalArgumentException(
                    "Valid registration details are required"
            );
        }

        if (!repository.existsById(register.getRegistrarId())) {
            throw new IllegalArgumentException(
                    "Registration record not found"
            );
        }

        return repository.save(register);
    }

    @Override
    public boolean delete(String id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }

    @Override
    public List<Register> getAll() {
        return repository.findAll();
    }
}
