package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;

@Entity
@Table(name = "facility")
public class Facility {

    @Id
    private String facilityId;
    private String name;
    private int capacity;
    private String location;
    private String departmentId;

    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;

    protected Facility() {
    }

    public Facility(String facilityId, String name, int capacity,
                    String location, String departmentId,
                    FacilityType facilityType) {
        this.facilityId = facilityId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.departmentId = departmentId;
        this.facilityType = facilityType;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "facilityId='" + facilityId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", facilityType=" + facilityType +
                '}';
    }
}