package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.repository.LoginRepository;
import za.ac.cput.campusfacilitybooking.service.LoginService;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginRepository repository;

    public LoginServiceImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public Login create(Login login) {
        return repository.save(login);
    }

    @Override
    public Login read(String id) {
        Optional<Login> login = repository.findById(id);
        return login.orElse(null);
    }

    @Override
    public Login update(Login login) {
        return repository.save(login);
    }

    @Override
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Login authenticate(String username, String password) {

        Login login = repository.findByUsername(username)
                .orElse(null);

        if (login != null && login.getPassword().equals(password)) {
            return login;
        }

        return null;
    }
}