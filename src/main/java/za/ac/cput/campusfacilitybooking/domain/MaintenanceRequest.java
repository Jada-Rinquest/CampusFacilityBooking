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
    private String maintenancePriorityId;
    private String maintenanceStatusId;

    @ManyToOne
    @JoinColumn(name = "equipment_id", insertable = false, updatable = false)
    private Equipment equipment;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "maintenance_priority_id", insertable = false, updatable = false)
    private MaintenancePriority maintenancePriority;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "maintenance_status_id", insertable = false, updatable = false)
    private MaintenanceStatus maintenanceStatus;

    protected MaintenanceRequest() {
    }

    public MaintenanceRequest(String requestId, String equipmentId,
                              String reportedBy, String description,
                              LocalDate dateReported,
                              String maintenancePriorityId,
                              String maintenanceStatusId) {
        this.requestId = requestId;
        this.equipmentId = equipmentId;
        this.reportedBy = reportedBy;
        this.description = description;
        this.dateReported = dateReported;
        this.maintenancePriorityId = maintenancePriorityId;
        this.maintenanceStatusId = maintenanceStatusId;
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

    public String getMaintenancePriorityId() {
        return maintenancePriorityId;
    }

    public String getMaintenanceStatusId() {
        return maintenanceStatusId;
    }
    @Override
    public String toString() {
        return "MaintenanceRequest{" +
                "requestId='" + requestId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", reportedBy='" + reportedBy + '\'' +
                ", description='" + description + '\'' +
                ", dateReported=" + dateReported +
                ", maintenancePriorityId='" + maintenancePriorityId + '\'' +
                ", maintenanceStatusId='" + maintenanceStatusId + '\'' +
                '}';
    }
}