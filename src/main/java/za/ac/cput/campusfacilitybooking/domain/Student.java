/* Student.java
   Student Entity
   Author: Jada Rinquest 222871296
   Date: 21 June 2026
*/
package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    private String studentId;
    private String studentNumber;
    private String userId;

    protected Student() {
    }

    public Student(String studentId, String studentNumber, String userId) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.userId = userId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}