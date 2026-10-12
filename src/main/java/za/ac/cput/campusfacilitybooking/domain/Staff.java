package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    private String staffId;
    private String userId;

    protected Staff() {
    }

    public Staff(String staffId, String userId) {
        this.staffId = staffId;
        this.userId = userId;
    }

    private Staff(Builder builder) {
        this.staffId = builder.staffId;
        this.userId = builder.userId;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getUserId() {
        return userId;
    }

    public static class Builder {
        private String staffId;
        private String userId;

        public Builder setStaffId(String staffId) {
            this.staffId = staffId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Staff build() {
            return new Staff(this);
        }
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}