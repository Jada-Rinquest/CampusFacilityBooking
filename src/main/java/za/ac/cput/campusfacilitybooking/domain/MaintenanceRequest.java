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

    private MaintenanceRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.equipmentId = builder.equipmentId;
        this.reportedBy = builder.reportedBy;
        this.description = builder.description;
        this.dateReported = builder.dateReported;
        this.maintenancePriority = builder.maintenancePriority;
        this.maintenanceStatus = builder.maintenanceStatus;
    }

    public static class Builder {
        private String requestId;
        private String equipmentId;
        private String reportedBy;
        private String description;
        private LocalDate dateReported;
        private MaintenancePriority maintenancePriority;
        private MaintenanceStatus maintenanceStatus;

        public Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public Builder setReportedBy(String reportedBy) {
            this.reportedBy = reportedBy;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDateReported(LocalDate dateReported) {
            this.dateReported = dateReported;
            return this;
        }

        public Builder setMaintenancePriority(MaintenancePriority maintenancePriority) {
            this.maintenancePriority = maintenancePriority;
            return this;
        }

        public Builder setMaintenanceStatus(MaintenanceStatus maintenanceStatus) {
            this.maintenanceStatus = maintenanceStatus;
            return this;
        }

        public MaintenanceRequest build() {
            return new MaintenanceRequest(this);
        }
    }
}