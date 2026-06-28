package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.TimeSlot;

public class TimeSlotFactory {

    public static TimeSlot createTimeSlot(String timeSlotId,
                                          String date,
                                          String startTime,
                                          String endTime) {

        return new TimeSlot.Builder()
                .setTimeSlotId(timeSlotId)
                .setDate(date)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .build();
    }
}
