package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.User;

public class StaffFactory {

    public static Staff createStaff(String staffId, User user) {

        if (staffId == null || staffId.isEmpty()) {
            throw new IllegalArgumentException("Staff ID is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        return new Staff.Builder()
                .setStaffId(staffId)
                .setUser(user)
                .build();
    }
}