package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    private String addressId;
    private String address;
    private String description;
    private String userId;

    protected Address() {
    }

    public Address(String addressId, String address,
                   String description, String userId) {
        this.addressId = addressId;
        this.address = address;
        this.description = description;
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    private Address(Builder builder) {
        this.addressId = builder.addressId;
        this.address = builder.address;
        this.description = builder.description;
        this.userId = builder.userId;
    }

    public static class Builder {
        private String addressId;
        private String address;
        private String description;
        private String userId;

        public Builder setAddressId(String addressId) {
            this.addressId = addressId;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}