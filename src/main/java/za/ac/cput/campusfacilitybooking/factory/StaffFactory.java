package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Staff;

public class StaffFactory {

    public static Staff createStaff(String staffId, String userId) {

        if (staffId == null || staffId.isEmpty()) {
            throw new IllegalArgumentException("Staff ID is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        return new Staff.Builder()
                .setStaffId(staffId)
                .setUserId(userId)
                .build();
    }
}