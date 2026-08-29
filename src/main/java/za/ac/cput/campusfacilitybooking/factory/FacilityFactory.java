package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;

public class FacilityFactory {

    public static Facility createFacility(String facilityId,
                                          String name,
                                          int capacity,
                                          String location,
                                          String departmentId,
                                          FacilityType facilityType) {

        if (facilityId == null || facilityId.isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Facility name is required");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (departmentId == null || departmentId.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        if (facilityType == null) {
            throw new IllegalArgumentException("Facility Type is required");
        }

        return new Facility(
                facilityId,
                name,
                capacity,
                location,
                departmentId,
                facilityType
        );
    }
}