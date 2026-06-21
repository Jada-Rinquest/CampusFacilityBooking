/* Student.java
   Student Entity using Builder Pattern
   Author: Jada Rinquest 222871296
   Date: 21 June 2026
*/
package za.ac.cput.campusfacilitybooking.domain;

public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String studentNumber;
    private Department department;

    // Private constructor
    private Student() {}

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public Department getDepartment() {
        return department;
    }

    // Builder
    public static class Builder {

        private String studentId;
        private String firstName;
        private String lastName;
        private String email;
        private String studentNumber;
        private Department department;

        public Builder setStudentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder setDepartment(Department department) {
            this.department = department;
            return this;
        }

        public Student build() {
            Student student = new Student();

            student.studentId = this.studentId;
            student.firstName = this.firstName;
            student.lastName = this.lastName;
            student.email = this.email;
            student.studentNumber = this.studentNumber;
            student.department = this.department;

            return student;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", department=" + department +
                '}';
    }
}