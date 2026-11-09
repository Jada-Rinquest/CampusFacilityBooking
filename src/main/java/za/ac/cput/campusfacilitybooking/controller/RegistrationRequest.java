package za.ac.cput.campusfacilitybooking.controller;

import java.time.LocalDate;

/*
 * Everything the sign-up form sends to POST /auth/register.
 * IDs and the role are not accepted from the client: the server creates them.
 */
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String accountType;     // STUDENT or STAFF - must match the email domain
    private String studentNumber;   // students only
    private String departmentId;
    private LocalDate dateOfBirth;  // optional

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
