
package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;

import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.UserRoleService;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository repository;

    public UserRoleServiceImpl(UserRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserRole create(UserRole userRole) {

        validateUserRole(userRole);

        if (repository.existsById(userRole.getUserRoleId())) {
            throw new IllegalArgumentException(
                    "User role ID already exists"
            );
        }

        return repository.save(userRole);
    }

    @Override
    public UserRole read(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public UserRole update(UserRole userRole) {

        validateUserRole(userRole);

        if (!repository.existsById(userRole.getUserRoleId())) {
            throw new IllegalArgumentException(
                    "User role record not found"
            );
        }

        return repository.save(userRole);
    }

    @Override
    public boolean delete(String id) {

        if (id == null || id.isBlank()) {
            return false;
        }

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    @Override
    public List<UserRole> getAll() {
        return repository.findAll();
    }

    private void validateUserRole(UserRole userRole) {

        if (userRole == null) {
            throw new IllegalArgumentException(
                    "User role details are required"
            );
        }

        if (userRole.getUserRoleId() == null ||
                userRole.getUserRoleId().isBlank()) {
            throw new IllegalArgumentException(
                    "User role ID is required"
            );
        }

        if (userRole.getUserId() == null ||
                userRole.getUserId().isBlank()) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }

        if (userRole.getRole() == null) {
            throw new IllegalArgumentException(
                    "User role is required"
            );
        }
    }
}
