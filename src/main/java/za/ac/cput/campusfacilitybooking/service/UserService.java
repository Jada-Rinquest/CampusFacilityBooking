package za.ac.cput.campusfacilitybooking.service;

import za.ac.cput.campusfacilitybooking.domain.User;
import java.util.Optional;

public interface UserService extends IService<User, String> {
    Optional<User> findByEmail(String email);
}