/* StudentFactory.java
   Factory Class for Student
   Author: Jada Rinquest 222871296
*/

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Student;

public class StudentFactory {

    public static Student createStudent(String studentId,
                                        String studentNumber,
                                        String userId) {

        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }

        if (studentNumber == null || studentNumber.isEmpty()) {
            throw new IllegalArgumentException("Student number is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        return new Student(
                studentId,
                studentNumber,
                userId
        );
    }
}