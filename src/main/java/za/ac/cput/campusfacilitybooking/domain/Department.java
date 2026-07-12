/* Department.java
   Department Entity using Builder Pattern
   Author: Jada Rinquest 222871296
   Date: 21 June 2026
*/
package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "department")
public class Department {

    @Id
    private String departmentId;

    private String name;
    private String building;
    private String headOfDepartment;

    protected Department() {
    }

    private Department(Builder builder) {
        this.departmentId = builder.departmentId;
        this.name = builder.name;
        this.building = builder.building;
        this.headOfDepartment = builder.headOfDepartment;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public static class Builder {

        private String departmentId;
        private String name;
        private String building;
        private String headOfDepartment;

        public Builder setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setBuilding(String building) {
            this.building = building;
            return this;
        }

        public Builder setHeadOfDepartment(String headOfDepartment) {
            this.headOfDepartment = headOfDepartment;
            return this;
        }

        public Department build() {
            return new Department(this);
        }
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", building='" + building + '\'' +
                ", headOfDepartment='" + headOfDepartment + '\'' +
                '}';
    }
}