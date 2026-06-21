/* Department.java
   Department Entity using Builder Pattern
   Author: Jada Rinquest 222871296
   Date: 21 June 2026
*/
package za.ac.cput.campusfacilitybooking.domain;

public class Department {

    private String departmentId;
    private String name;
    private String building;
    private String headOfDepartment;

    // Private constructor
    private Department() {}

    // Getters
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

    // Builder Class
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
            Department department = new Department();

            department.departmentId = this.departmentId;
            department.name = this.name;
            department.building = this.building;
            department.headOfDepartment = this.headOfDepartment;

            return department;
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