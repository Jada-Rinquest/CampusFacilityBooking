/* Equipment.java
Equipment entity class
Author: Angelia Van der Westhuizen 221420649
Date: 21 June 2026
*/

package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.EquipmentStatus;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    private String equipmentId;
    private String name;
    private String serialNumber;
    private String facilityId;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus equipmentStatus;

    protected Equipment() {
    }

    public Equipment(String equipmentId, String name,
                     String serialNumber, String facilityId,
                     EquipmentStatus equipmentStatus) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.serialNumber = serialNumber;
        this.facilityId = facilityId;
        this.equipmentStatus = equipmentStatus;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId='" + equipmentId + '\'' +
                ", name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", facilityId='" + facilityId + '\'' +
                ", equipmentStatus=" + equipmentStatus +
                '}';
    }

    private Equipment(Builder builder) {
        this.equipmentId = builder.equipmentId;
        this.name = builder.name;
        this.serialNumber = builder.serialNumber;
        this.facilityId = builder.facilityId;
        this.equipmentStatus = builder.equipmentStatus;
    }

    public static class Builder {
        private String equipmentId;
        private String name;
        private String serialNumber;
        private String facilityId;
        private EquipmentStatus equipmentStatus;

        public Builder setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder setFacilityId(String facilityId) {
            this.facilityId = facilityId;
            return this;
        }

        public Builder setEquipmentStatus(EquipmentStatus equipmentStatus) {
            this.equipmentStatus = equipmentStatus;
            return this;
        }

        public Equipment build() {
            return new Equipment(this);
        }
    }
}