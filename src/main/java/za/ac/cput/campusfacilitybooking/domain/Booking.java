package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility; // Changed from facilityId String

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot; // Changed from timeSlotId String

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Changed from userId String

    private String purpose;

    @Enumerated(EnumType.STRING)  // Stores as "PENDING", "APPROVED", etc.
    @ManyToOne
    @JoinColumn(name = "booking_status_id")
    private BookingStatus bookingStatus;

    protected Booking() {
    }

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.facility = builder.facility;
        this.timeSlot = builder.timeSlot;
        this.user = builder.user;
        this.purpose = builder.purpose;
        this.bookingStatus = builder.bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Facility getFacility() {
        return facility;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public User getUser() {
        return user;
    }

    public String getPurpose() {
        return purpose;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public static class Builder {

        private String bookingId;
        private Facility facility;
        private TimeSlot timeSlot;
        private User user;
        private String purpose;
        private BookingStatus bookingStatus;

        public Builder setBookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setFacility(Facility facility) {
            this.facility = facility;
            return this;
        }

        public Builder setTimeSlot(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setPurpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public Builder setBookingStatus(BookingStatus bookingStatus) {
            this.bookingStatus = bookingStatus;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", facility=" + facility +
                ", timeSlot=" + timeSlot +
                ", user=" + user +
                ", purpose='" + purpose + '\'' +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}