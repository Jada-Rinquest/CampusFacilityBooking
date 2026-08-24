package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.service.RegisterService;

import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository repository;

    public RegisterServiceImpl(RegisterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Register create(Register register) {
        return repository.save(register);
    }

    @Override
    public Register read(String id) {
        Optional<Register> register = repository.findById(id);
        return register.orElse(null);
    }

    @Override
    public Register update(Register register) {
        return repository.save(register);
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