package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String departmentId;

    protected User() {
    }

    public User(String userId, String firstName, String lastName,
                String email, LocalDate dateOfBirth, String departmentId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.departmentId = departmentId;
    }

    public String getUserId() {
        return userId;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}