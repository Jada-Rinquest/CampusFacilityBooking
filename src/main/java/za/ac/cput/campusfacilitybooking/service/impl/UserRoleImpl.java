package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.UserRoleService;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository repository;

    public UserRoleServiceImpl(UserRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserRole create(UserRole userRole) {
        return repository.save(userRole);
    }

    @Override
    public UserRole read(String id) {
        Optional<UserRole> userRole = repository.findById(id);
        return userRole.orElse(null);
    }

    @Override
    public UserRole update(UserRole userRole) {
        return repository.save(userRole);
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