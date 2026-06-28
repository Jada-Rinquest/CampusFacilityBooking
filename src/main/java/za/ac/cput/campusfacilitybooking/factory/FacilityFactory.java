package za.ac.cput.campusfacilitybooking.factory;
/* Author: Ayren Villet (223120030)
     Date: 28 June 2026 */
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Facility;

public class FacilityFactory {

    public static Facility createFacility(String facilityId,
                                          String name,
                                          String type,
                                          int capacity,
                                          String location,
                                          Department department) {

        if (facilityId == null || facilityId.isEmpty()) {
            return null;
        }

        if (name == null || name.isEmpty()) {
            return null;
        }

        if (type == null || type.isEmpty()) {
            return null;
        }

        if (capacity <= 0) {
            return null;
        }

        if (location == null || location.isEmpty()) {
            return null;
        }

        if (department == null) {
            return null;
        }

        return new Facility(
                facilityId,
                name,
                type,
                capacity,
                location,
                department
        );
    }
}


