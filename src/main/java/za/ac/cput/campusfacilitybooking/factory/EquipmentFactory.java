package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Equipment;

public class EquipmentFactory {

    public static Equipment createEquipment(String equipmentId,
                                            String name,
                                            String serialNumber,
                                            String status,
                                            String facility) {

        return new Equipment.Builder()
                .setEquipmentId(equipmentId)
                .setName(name)
                .setSerialNumber(serialNumber)
                .setStatus(status)
                .setFacility(facility)
                .build();
    }
}