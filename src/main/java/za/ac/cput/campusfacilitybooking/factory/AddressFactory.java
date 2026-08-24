package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.domain.User;

public class AddressFactory {

    public static Address createAddress(String addressId,
                                        String address,
                                        String description,
                                        User user) {

        if (addressId == null || addressId.isEmpty()) {
            throw new IllegalArgumentException("Address ID is required");
        }

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        return new Address(
                addressId,
                address,
                description,
                user
        );
    }
}
