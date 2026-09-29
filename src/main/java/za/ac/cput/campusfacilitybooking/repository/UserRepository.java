package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByDepartmentId(String departmentId);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByDateOfBirth(LocalDate dateOfBirth);
}