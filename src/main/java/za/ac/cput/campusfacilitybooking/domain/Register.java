package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "register")
public class Register {

    @Id
    private String registrarId;

    private String email;
    private LocalDate dateRegistered;

    protected Register() {
    }

    public Register(String registrarId, String email,
                    LocalDate dateRegistered) {
        this.registrarId = registrarId;
        this.email = email;
        this.dateRegistered = dateRegistered;
    }

    public String getRegistrarId() {
        return registrarId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }
    @Override
    public String toString() {
        return "Register{" +
                "registrarId='" + registrarId + '\'' +
                ", email='" + email + '\'' +
                ", dateRegistered=" + dateRegistered +
                '}';
    }

    private Register(Builder builder) {
        this.registrarId = builder.registrarId;
        this.email = builder.email;
        this.dateRegistered = builder.dateRegistered;
    }

    public static class Builder {
        private String registrarId;
        private String email;
        private LocalDate dateRegistered;

        public Builder setRegistrarId(String registrarId) {
            this.registrarId = registrarId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setDateRegistered(LocalDate dateRegistered) {
            this.dateRegistered = dateRegistered;
            return this;
        }

        public Register build() {
            return new Register(this);
        }
    }
}