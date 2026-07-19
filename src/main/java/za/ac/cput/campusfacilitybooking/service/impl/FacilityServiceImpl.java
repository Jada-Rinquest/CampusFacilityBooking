/* Author: Ayren Villet (223120030)
     Date: 12 July 2026 */
package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.repository.FacilityRepository;
import za.ac.cput.campusfacilitybooking.service.FacilityService;
import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository repository;

    public FacilityServiceImpl(FacilityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Facility create(Facility facility) {
        return repository.save(facility);
    }

    @Override
    public Facility read(String id) {
        Optional<Facility> facility = repository.findById(id);
        return facility.orElse(null);
    }

    @Override
    public Facility update(Facility facility) {
        return repository.save(facility);
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