package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Facility;

public class FacilityFactory {

    public static Facility createFacility(String facilityId,
                                          String name,
                                          int capacity,
                                          String location,
                                          String departmentId,
                                          String facilityTypeId) {

        if (facilityId == null || facilityId.isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Facility name is required");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (departmentId == null || departmentId.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        if (facilityTypeId == null || facilityTypeId.isEmpty()) {
            throw new IllegalArgumentException("Facility Type ID is required");
        }

        return new Facility(
                facilityId,
                name,
                capacity,
                location,
                departmentId,
                facilityTypeId
        );
    }
}
