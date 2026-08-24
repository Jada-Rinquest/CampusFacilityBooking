package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    private String staffId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Staff() {
    }

    public Staff(String staffId, User user) {
        this.staffId = staffId;
        this.user = user;
    }

    private Staff(Builder builder) {
        this.staffId = builder.staffId;
        this.user = builder.user;
    }

    public String getStaffId() {
        return staffId;
    }

    public User getUser() {
        return user;
    }

    public static class Builder {
        private String staffId;
        private User user;

        public Builder setStaffId(String staffId) {
            this.staffId = staffId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
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
                ", user=" + user +
                '}';
    }
}