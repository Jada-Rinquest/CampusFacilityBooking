/* StudentFactory.java
   Factory Class for Student
   Author: Jada Rinquest 222871296
*/

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.domain.User;

public class StudentFactory {

    public static Student createStudent(String studentId,
                                        String studentNumber,
                                        User user) {

        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }

        if (studentNumber == null || studentNumber.isEmpty()) {
            throw new IllegalArgumentException("Student number is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        return new Student(
                studentId,
                studentNumber,
                user
        );
    }
}