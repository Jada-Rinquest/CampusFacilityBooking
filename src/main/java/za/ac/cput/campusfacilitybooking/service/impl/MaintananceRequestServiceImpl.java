package za.ac.cput.campusfacilitybooking.service.impl;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.repository.MaintananceRequestRepository;
import za.ac.cput.campusfacilitybooking.repository.MaintenanceRequestRepository;
import za.ac.cput.campusfacilitybooking.service.MaintenanceRequestService;

import java.util.Optional;

@Service
public class MaintenanceRequestServiceImpl implements MaintenanceRequestService {

    private final MaintananceRequestRepository repository;

    public MaintenanceRequestServiceImpl(MaintenanceRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public MaintenanceRequest create(MaintenanceRequest maintenanceRequest) {
        return repository.save(maintenanceRequest);
    }

    @Override
    public MaintenanceRequest read(String id) {
        Optional<MaintenanceRequest> maintenanceRequest = repository.findById(id);
        return maintenanceRequest.orElse(null);
    }

    @Override
    public MaintenanceRequest update(MaintenanceRequest maintenanceRequest) {
        return repository.save(maintenanceRequest);
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
