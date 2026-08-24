package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotFactory {

    public static TimeSlot createTimeSlot(String timeSlotId,
                                          LocalDate date,
                                          LocalTime startTime,
                                          LocalTime endTime) {

        if (timeSlotId == null || timeSlotId.isEmpty()) {
            throw new IllegalArgumentException("Time Slot ID is required");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("End time is required");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        return new TimeSlot(
                timeSlotId,
                date,
                startTime,
                endTime
        );
    }
}