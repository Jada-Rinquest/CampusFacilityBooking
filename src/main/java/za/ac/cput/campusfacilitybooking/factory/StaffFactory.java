
/*Author: Milani Sani(230371574)
Date: 28 June 2026
 */

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

        return new Staff(staffId, user);
    }
}