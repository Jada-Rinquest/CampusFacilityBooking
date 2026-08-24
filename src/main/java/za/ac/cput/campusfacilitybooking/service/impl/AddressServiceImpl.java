package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.repository.AddressRepository;
import za.ac.cput.campusfacilitybooking.service.AddressService;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address create(Address address) {
        return repository.save(address);
    }

    @Override
    public Address read(String id) {
        Optional<Address> address = repository.findById(id);
        return address.orElse(null);
    }

    @Override
    public Address update(Address address) {
        return repository.save(address);
    }

    @Override
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}