package za.ac.cput.campusfacilitybooking.repository;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    List<Staff> findByFirstName(String firstName);

    List<Staff> findByLastName(String lastName);

    List<Staff> findByRole(StaffRole role);

    List<Staff> findByDepartmentDepartmentId(String departmentId);

    Optional<Staff> findByEmail(String email);
}
