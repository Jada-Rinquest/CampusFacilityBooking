package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User read(String id) {
        Optional<User> user = repository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User update(User user) {
        return repository.save(user);
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