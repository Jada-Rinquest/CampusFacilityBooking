package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Register;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, String> {

    Optional<Register> findByEmail(String email);

    List<Register> findByDateRegistered(LocalDate dateRegistered);
}