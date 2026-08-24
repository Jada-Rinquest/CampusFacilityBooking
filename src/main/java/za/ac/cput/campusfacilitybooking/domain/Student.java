/* Student.java
   Student Entity using Builder Pattern
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Student() {
    }

    public Student(String studentId, String studentNumber, User user) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.user = user;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public User getUser() {
        return user;
    }
    // Add this to Student.java
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", user=" + user +
                '}';
    }
}