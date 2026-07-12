package za.ac.cput.campusfacilitybooking.service;

/* IMaintenanceRequestService.java
   IMaintenanceRequestService interface
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;
import java.util.List;

public interface IMaintenanceRequestService extends IService<MaintenanceRequest, String> {

    List<MaintenanceRequest> findByStatus(MaintenanceStatus status);

    List<MaintenanceRequest> findByPriority(MaintenancePriority priority);

    List<MaintenanceRequest> findByReportedById(String reportedById);

    List<MaintenanceRequest> findByEquipmentId(String equipmentId);

    List<MaintenanceRequest> findByDateReported(LocalDate dateReported);
}
