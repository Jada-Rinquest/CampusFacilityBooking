package za.ac.cput.campusfacilitybooking.domain;

/*Author: Milani Sani(230371574)
Date: 21 June 2026
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
import java.util.Objects;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @Column(name = "staff_id", nullable = false, unique = true)
    private String staffId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private StaffRole role;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "departmentId")
    private Department department;

    protected Staff() {}

    private Staff(Builder builder) {
        this.staffId = builder.staffId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.role = builder.role;
        this.department = builder.department;
    }

    public static class Builder {
        private String staffId;
        private String firstName;
        private String lastName;
        private String email;
        private StaffRole role;
        private Department department;

        public Builder staffId(String staffId) {
            this.staffId = staffId;
            return this;
        }
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder role(StaffRole role) {
            this.role = role;
            return this;
        }
        public Builder department(Department department) {
            this.department = department;
            return this;
        }

        public Staff build() {
            Objects.requireNonNull(staffId, "staffId is required");
            Objects.requireNonNull(firstName, "firstName is required");
            Objects.requireNonNull(lastName, "lastName is required");
            Objects.requireNonNull(role, "role is required");
            return new Staff(this);
        }
    }

    public String getStaffId() {
        return staffId;
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
    public StaffRole getRole() {
        return role;
    }
    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("Staff(staffId=%s, firstName=%s, lastName=%s, role=%s)",
                staffId, firstName, lastName, role);
    }
}
