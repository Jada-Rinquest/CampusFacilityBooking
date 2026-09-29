package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Login;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

    Optional<Login> findByUsername(String username);

    Optional<Login> findByRegistrarId(String registrarId);
}