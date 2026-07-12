/* Equipment.java
Equipment entity class
Author: Angelia Van der Westhuizen 221420649
Date: 21 June 2026
*/

package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents equipment located in a facility.
 */
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @Column(name = "equipment_id")
    private String equipmentId;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "facility")
    private String facility;

    protected Equipment() {
    }

    private Equipment(Builder builder) {
        this.equipmentId = builder.equipmentId;
        this.name = builder.name;
        this.serialNumber = builder.serialNumber;
        this.status = builder.status;
        this.facility = builder.facility;
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

    public String getStatus() {
        return status;
    }

    public String getFacility() {
        return facility;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId='" + equipmentId + '\'' +
                ", name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", status='" + status + '\'' +
                ", facility='" + facility + '\'' +
                '}';
    }

    public static class Builder {
        private String equipmentId;
        private String name;
        private String serialNumber;
        private String status;
        private String facility;

        public Builder setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public Builder equipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder setFacility(String facility) {
            this.facility = facility;
            return this;
        }

        public Builder facility(String facility) {
            this.facility = facility;
            return this;
        }

        public Equipment build() {
            return new Equipment(this);
        }
    }
}

