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

    private Facility(Builder builder) {
        this.facilityId = builder.facilityId;
        this.name = builder.name;
        this.capacity = builder.capacity;
        this.location = builder.location;
        this.departmentId = builder.departmentId;
        this.facilityType = builder.facilityType;
    }

    public static class Builder {
        private String facilityId;
        private String name;
        private int capacity;
        private String location;
        private String departmentId;
        private FacilityType facilityType;

        public Builder setFacilityId(String facilityId) {
            this.facilityId = facilityId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder setFacilityType(FacilityType facilityType) {
            this.facilityType = facilityType;
            return this;
        }

        public Facility build() {
            return new Facility(this);
        }
    }
}