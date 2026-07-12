package za.ac.cput.campusfacilitybooking.service;

/* IStaffService.java
   IStaffService interface
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;

import java.util.List;

public interface IStaffService extends IService<Staff, String> {

    List<Staff> findByFirstName(String firstName);

    List<Staff> findByLastName(String lastName);

    List<Staff> findByRole(StaffRole role);

    List<Staff> findByDepartmentId(String departmentId);

    Staff findByEmail(String email);
}
