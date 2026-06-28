/* StudentFactory.java
   Factory Class for Student
   Author: Jada Rinquest 222871296
*/

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Student;

public class StudentFactory {

    public static Student createStudent(
            String studentId,
            String firstName,
            String lastName,
            String email,
            String studentNumber,
            Department department
    ) {

        return new Student.Builder()
                .setStudentId(studentId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setStudentNumber(studentNumber)
                .setDepartment(department)
                .build();
    }
}