package za.ac.cput.campusfacilitybooking.factory;

/*Author: Milani Sani(230371574)
Date: 28 June 2026
 */

import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;

public class MaintenanceRequestFactory {

    public static MaintenanceRequest createMaintenanceRequest(
            String requestId,
            String equipmentId,
            String reportedById,
            String description,
            MaintenancePriority priority,
            MaintenanceStatus status,
            LocalDate dateReported
    ) {

        return new MaintenanceRequest.Builder()
                .requestId(requestId)
                .equipmentId(equipmentId)
                .reportedById(reportedById)
                .description(description)
                .priority(priority)
                .status(status)
                .dateReported(dateReported)
                .build();
    }
}