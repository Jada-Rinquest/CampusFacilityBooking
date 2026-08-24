package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    private String addressId;

    private String address;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Address() {
    }

    public Address(String addressId, String address,
                   String description, User user) {
        this.addressId = addressId;
        this.address = address;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }
}