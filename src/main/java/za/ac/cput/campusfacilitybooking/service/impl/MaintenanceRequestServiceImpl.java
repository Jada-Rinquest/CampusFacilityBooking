package za.ac.cput.campusfacilitybooking.service.impl;

/* MaintenanceRequestServiceImpl.java
   MaintenanceRequestServiceImpl implementation class
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.repository.MaintenanceRequestRepository;
import za.ac.cput.campusfacilitybooking.service.IMaintenanceRequestService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MaintenanceRequestServiceImpl implements IMaintenanceRequestService {

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Override
    public MaintenanceRequest create(MaintenanceRequest maintenanceRequest) {
        return maintenanceRequestRepository.save(maintenanceRequest);
    }

    @Override
    public MaintenanceRequest read(String requestId) {
        return maintenanceRequestRepository.findById(requestId).orElse(null);
    }

    @Override
    public MaintenanceRequest update(MaintenanceRequest maintenanceRequest) {
        if (!maintenanceRequestRepository.existsById(maintenanceRequest.getRequestId())) {
            return null;
        }
        return maintenanceRequestRepository.save(maintenanceRequest);
    }

    @Override
    public boolean delete(String requestId) {
        if (!maintenanceRequestRepository.existsById(requestId)) {
            return false;
        }
        maintenanceRequestRepository.deleteById(requestId);
        return true;
    }

    @Override
    public Set<MaintenanceRequest> getAll() {
        return new HashSet<>(maintenanceRequestRepository.findAll());
    }

    @Override
    public List<MaintenanceRequest> findByStatus(MaintenanceStatus status) {
        return maintenanceRequestRepository.findByStatus(status);
    }

    @Override
    public List<MaintenanceRequest> findByPriority(MaintenancePriority priority) {
        return maintenanceRequestRepository.findByPriority(priority);
    }

    @Override
    public List<MaintenanceRequest> findByReportedById(String reportedById) {
        return maintenanceRequestRepository.findByReportedById(reportedById);
    }

    @Override
    public List<MaintenanceRequest> findByEquipmentId(String equipmentId) {
        return maintenanceRequestRepository.findByEquipmentEquipmentId(equipmentId);
    }

    @Override
    public List<MaintenanceRequest> findByDateReported(LocalDate dateReported) {
        return maintenanceRequestRepository.findByDateReported(dateReported);
    }
}
