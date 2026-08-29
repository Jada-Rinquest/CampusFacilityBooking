package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance_request")
public class MaintenanceRequest {

    @Id
    private String requestId;
    private String equipmentId;
    private String reportedBy;
    private String description;
    private LocalDate dateReported;

    @Enumerated(EnumType.STRING)
    private MaintenancePriority maintenancePriority;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;

    protected MaintenanceRequest() {
    }

    public MaintenanceRequest(String requestId, String equipmentId,
                              String reportedBy, String description,
                              LocalDate dateReported,
                              MaintenancePriority maintenancePriority,
                              MaintenanceStatus maintenanceStatus) {
        this.requestId = requestId;
        this.equipmentId = equipmentId;
        this.reportedBy = reportedBy;
        this.description = description;
        this.dateReported = dateReported;
        this.maintenancePriority = maintenancePriority;
        this.maintenanceStatus = maintenanceStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateReported() {
        return dateReported;
    }

    public MaintenancePriority getMaintenancePriority() {
        return maintenancePriority;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    @Override
    public String toString() {
        return "MaintenanceRequest{" +
                "requestId='" + requestId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", reportedBy='" + reportedBy + '\'' +
                ", description='" + description + '\'' +
                ", dateReported=" + dateReported +
                ", maintenancePriority=" + maintenancePriority +
                ", maintenanceStatus=" + maintenanceStatus +
                '}';
    }
}