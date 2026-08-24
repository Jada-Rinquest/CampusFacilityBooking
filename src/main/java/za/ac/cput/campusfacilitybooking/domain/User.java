package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @OneToOne(mappedBy = "user")
    private Student student;

    @OneToOne(mappedBy = "user")
    private Staff staff;

    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    // Fixed: Notification relationship
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

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

    public Department getDepartment() {
        return department;
    }

    public Student getStudent() {
        return student;
    }

    public Staff getStaff() {
        return staff;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
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