package za.ac.cput.campusfacilitybooking.repository;

/* StaffRepository.java
   StaffRepository interface
   Author: Milani Sani (230371574)
   Date: 21 June 2026
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    List<Staff> findByFirstName(String firstName);

    List<Staff> findByLastName(String lastName);

    List<Staff> findByRole(StaffRole role);

    List<Staff> findByDepartmentDepartmentId(String departmentId);

    Staff findByEmail(String email);
}
