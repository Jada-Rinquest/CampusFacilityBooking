package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Equipment;

public class EquipmentFactory {

    public static Equipment createEquipment(String equipmentId,
                                            String name,
                                            String serialNumber,
                                            String facilityId,
                                            String equipmentStatusId) {

        if (equipmentId == null || equipmentId.isEmpty()) {
            throw new IllegalArgumentException("Equipment ID is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Equipment name is required");
        }

        if (serialNumber == null || serialNumber.isEmpty()) {
            throw new IllegalArgumentException("Serial number is required");
        }

        if (facilityId == null || facilityId.isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        if (equipmentStatusId == null || equipmentStatusId.isEmpty()) {
            throw new IllegalArgumentException("Equipment status ID is required");
        }

        return new Equipment(
                equipmentId,
                name,
                serialNumber,
                facilityId,
                equipmentStatusId
        );
    }
}