/* DepartmentFactory.java
   Factory Class for Department
   Author: Jada Rinquest 222871296
*/
package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Department;

public class DepartmentFactory {

    public static Department createDepartment(String departmentId,
                                              String name,
                                              String building,
                                              String headOfDepartment) {

        if (departmentId == null || departmentId.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Department name is required");
        }

        if (building == null || building.isEmpty()) {
            throw new IllegalArgumentException("Building is required");
        }

        if (headOfDepartment == null || headOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Head of Department is required");
        }

        return new Department.Builder()
                .setDepartmentId(departmentId)
                .setName(name)
                .setBuilding(building)
                .setHeadOfDepartment(headOfDepartment)
                .build();
    }
}