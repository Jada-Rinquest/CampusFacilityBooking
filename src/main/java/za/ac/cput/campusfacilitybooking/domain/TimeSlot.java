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
    @Override
    public String toString() {
        return "TimeSlot{" +
                "timeSlotId='" + timeSlotId + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    private TimeSlot(Builder builder) {
        this.timeSlotId = builder.timeSlotId;
        this.date = builder.date;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static class Builder {
        private String timeSlotId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;

        public Builder setTimeSlotId(String timeSlotId) {
            this.timeSlotId = timeSlotId;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public TimeSlot build() {
            return new TimeSlot(this);
        }
    }
}