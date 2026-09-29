package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserId(String userId);

    List<UserRole> findByRole(Role role);

    Optional<UserRole> findByUserIdAndRole(String userId, Role role);
}