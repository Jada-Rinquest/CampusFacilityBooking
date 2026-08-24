package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    private String bookingId;

    private String facilityId;
    private String timeSlotId;
    private String userId;
    private String purpose;
    private String bookingStatusId;

    @ManyToOne
    @JoinColumn(name = "facility_id", insertable = false, updatable = false)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "booking_status_id",
            insertable = false, updatable = false)
    private BookingStatus bookingStatus;

    protected Booking() {
    }

    public Booking(String bookingId, String facilityId,
                   String timeSlotId, String userId,
                   String purpose, String bookingStatusId) {
        this.bookingId = bookingId;
        this.facilityId = facilityId;
        this.timeSlotId = timeSlotId;
        this.userId = userId;
        this.purpose = purpose;
        this.bookingStatusId = bookingStatusId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getBookingStatusId() {
        return bookingStatusId;
    }
}