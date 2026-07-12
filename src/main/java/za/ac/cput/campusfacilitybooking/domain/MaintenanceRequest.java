package za.ac.cput.campusfacilitybooking.domain;

/*Author: Milani Sani(230371574)
Date: 21 June 2026
 */

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "maintenance_requests")
public class MaintenanceRequest {

    @Id
    @Column(name = "request_id")
    private String requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Column(name = "reported_by_id")
    private String reportedById;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private MaintenancePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MaintenanceStatus status;

    @Column(name = "date_reported")
    private LocalDate dateReported;

    protected MaintenanceRequest() {
    }

    private MaintenanceRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.equipment = builder.equipment;
        this.reportedById = builder.reportedById;
        this.description = builder.description;
        this.priority = builder.priority;
        this.status = builder.status;
        this.dateReported = builder.dateReported;
    }

    public static class Builder {
        private String requestId;
        private Equipment equipment;
        private String reportedById;
        private String description;
        private MaintenancePriority priority = MaintenancePriority.MEDIUM;
        private MaintenanceStatus status = MaintenanceStatus.OPEN;
        private LocalDate dateReported;

        public Builder requestId(String requestId) { 
            this.requestId = requestId; 
            return this; }
        public Builder equipment(Equipment equipment) { 
            this.equipment = equipment; 
            return this; }
        public Builder reportedById(String reportedById) { 
            this.reportedById = reportedById; 
            return this; }
        public Builder description(String description) { 
            this.description = description; 
            return this; }
        public Builder priority(MaintenancePriority priority) { 
            this.priority = priority; 
            return this; }
        public Builder status(MaintenanceStatus status) { 
            this.status = status; 
            return this; }
        public Builder dateReported(LocalDate dateReported) { 
            this.dateReported = dateReported; 
            return this; }

        public MaintenanceRequest build() {
            Objects.requireNonNull(requestId, "requestId is required");
            Objects.requireNonNull(equipment, "equipment is required");
            Objects.requireNonNull(reportedById, "reportedById is required");
            Objects.requireNonNull(description, "description is required");
            return new MaintenanceRequest(this);
        }
    }

    public String getRequestId() { 
        return requestId; }
    public Equipment getEquipment() { 
        return equipment; }
    public String getReportedById() { 
        return reportedById; }
    public String getDescription() { 
        return description; }
    public MaintenancePriority getPriority() { 
        return priority; }
    public MaintenanceStatus getStatus() { 
        return status; }
    public LocalDate getDateReported() { 
        return dateReported; }

    @Override
    public String toString() {
        String equipmentId = (equipment != null) ? equipment.getEquipmentId() : null;
        return String.format("MaintenanceRequest(requestId=%s, equipmentId=%s, priority=%s, status=%s)", requestId, equipmentId, priority, status);
    }
}
