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
}