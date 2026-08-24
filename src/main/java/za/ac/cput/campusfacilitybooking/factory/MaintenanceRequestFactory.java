
/*Author: Milani Sani(230371574)
Date: 28 June 2026
 */

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;

import java.time.LocalDate;

public class MaintenanceRequestFactory {

    public static MaintenanceRequest createMaintenanceRequest(
            String requestId,
            String equipmentId,
            String reportedBy,
            String description,
            LocalDate dateReported,
            String maintenancePriorityId,
            String maintenanceStatusId) {

        if (requestId == null || requestId.isEmpty()) {
            throw new IllegalArgumentException("Request ID is required");
        }

        if (equipmentId == null || equipmentId.isEmpty()) {
            throw new IllegalArgumentException("Equipment ID is required");
        }

        if (reportedBy == null || reportedBy.isEmpty()) {
            throw new IllegalArgumentException("Reported by is required");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (dateReported == null) {
            throw new IllegalArgumentException("Date reported is required");
        }

        if (maintenancePriorityId == null || maintenancePriorityId.isEmpty()) {
            throw new IllegalArgumentException("Maintenance priority ID is required");
        }

        if (maintenanceStatusId == null || maintenanceStatusId.isEmpty()) {
            throw new IllegalArgumentException("Maintenance status ID is required");
        }

        return new MaintenanceRequest(
                requestId,
                equipmentId,
                reportedBy,
                description,
                dateReported,
                maintenancePriorityId,
                maintenanceStatusId
        );
    }
}