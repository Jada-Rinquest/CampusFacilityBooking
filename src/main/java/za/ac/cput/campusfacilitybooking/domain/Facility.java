package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;

import java.util.List;

@Entity
@Table(name = "facility")
public class Facility {

    @Id
    private String facilityId;

    private String name;
    private int capacity;
    private String location;
    private String departmentId;
    private String facilityTypeId;

    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "facility_type_id", insertable = false, updatable = false)
    private FacilityType facilityType;

    @OneToMany(mappedBy = "facility")
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "facility")
    private List<Booking> bookings;

    protected Facility() {
    }

    public Facility(String facilityId, String name, int capacity,
                    String location, String departmentId,
                    String facilityTypeId) {
        this.facilityId = facilityId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.departmentId = departmentId;
        this.facilityTypeId = facilityTypeId;
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

    public String getFacilityTypeId() {
        return facilityTypeId;
    }
    @Override
    public String toString() {
        return "Facility{" +
                "facilityId='" + facilityId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", facilityTypeId='" + facilityTypeId + '\'' +
                '}';
    }
}