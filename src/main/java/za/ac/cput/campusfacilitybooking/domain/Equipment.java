/* Equipment.java
Equipment entity class
Author: Angelia Van der Westhuizen 221420649
Date: 21 June 2026
*/

package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    private String equipmentId;

    private String name;
    private String serialNumber;
    private String facilityId;
    private String equipmentStatusId;

    @ManyToOne
    @JoinColumn(name = "facility_id", insertable = false, updatable = false)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "equipment_status_id", insertable = false, updatable = false)
    private EquipmentStatus equipmentStatus;

    protected Equipment() {
    }

    public Equipment(String equipmentId, String name,
                     String serialNumber, String facilityId,
                     String equipmentStatusId) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.serialNumber = serialNumber;
        this.facilityId = facilityId;
        this.equipmentStatusId = equipmentStatusId;
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

    public String getEquipmentStatusId() {
        return equipmentStatusId;
    }
}