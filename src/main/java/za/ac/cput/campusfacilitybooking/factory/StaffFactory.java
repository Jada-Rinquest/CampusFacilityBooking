package za.ac.cput.campusfacilitybooking.factory;

/*Author: Milani Sani(230371574)
Date: 28A June 2026
 */

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;

public class StaffFactory {

    public static Staff createStaff(
            String staffId,
            String firstName,
            String lastName,
            String email,
            StaffRole role,
            Department department
    ) {

        return new Staff.Builder()
                .staffId(staffId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .department(department)
                .build();
    }
}