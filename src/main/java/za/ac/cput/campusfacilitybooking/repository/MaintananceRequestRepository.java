package za.ac.cput.campusfacilitybooking.repository;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintananceRequestRepository extends JpaRepository<MaintenanceRequest, String> {

    List<MaintenanceRequest> findByStatus(MaintenanceStatus status);

    List<MaintenanceRequest> findByPriority(MaintenancePriority priority);

    List<MaintenanceRequest> findByReportedById(String reportedById);

    List<MaintenanceRequest> findByEquipmentEquipmentId(String equipmentId);

    List<MaintenanceRequest> findByDateReported(LocalDate dateReported);
}
