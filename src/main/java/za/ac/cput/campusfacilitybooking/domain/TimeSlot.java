package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_slot")
public class TimeSlot {

    @Id
    private String timeSlotId;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    protected TimeSlot() {
    }

    public TimeSlot(String timeSlotId, LocalDate date,
                    LocalTime startTime, LocalTime endTime) {
        this.timeSlotId = timeSlotId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}