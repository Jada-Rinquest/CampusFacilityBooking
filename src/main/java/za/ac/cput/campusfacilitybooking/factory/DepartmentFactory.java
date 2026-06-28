/* DepartmentFactory.java
   Factory Class for Department
   Author: Jada Rinquest 222871296
*/
package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Department;

public class DepartmentFactory {

    public static Department createDepartment(
            String departmentId,
            String name,
            String building,
            String headOfDepartment
    ) {

        return new Department.Builder()
                .setDepartmentId(departmentId)
                .setName(name)
                .setBuilding(building)
                .setHeadOfDepartment(headOfDepartment)
                .build();
    }
}