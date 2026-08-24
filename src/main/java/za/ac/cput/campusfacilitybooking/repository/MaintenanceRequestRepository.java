package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, String> {

    // Changed from findByStatus to findByMaintenanceStatus
    List<MaintenanceRequest> findByMaintenanceStatus(MaintenanceStatus maintenanceStatus);

    // Changed from findByPriority to findByMaintenancePriority
    List<MaintenanceRequest> findByMaintenancePriority(MaintenancePriority maintenancePriority);

    List<MaintenanceRequest> findByReportedBy(String reportedBy);

    List<MaintenanceRequest> findByEquipmentId(String equipmentId);

    List<MaintenanceRequest> findByDateReported(LocalDate dateReported);
}