package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    private String bookingId;

    private String facilityId;
    private String timeSlotId;
    private String userId;
    private String purpose;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    protected Booking() {
    }

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.facilityId = builder.facilityId;
        this.timeSlotId = builder.timeSlotId;
        this.userId = builder.userId;
        this.purpose = builder.purpose;
        this.bookingStatus = builder.bookingStatus;
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public static class Builder {
        private String bookingId;
        private String facilityId;
        private String timeSlotId;
        private String userId;
        private String purpose;
        private BookingStatus bookingStatus;

        public Builder setBookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setFacilityId(String facilityId) {
            this.facilityId = facilityId;
            return this;
        }

        public Builder setTimeSlotId(String timeSlotId) {
            this.timeSlotId = timeSlotId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
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
                ", facilityId='" + facilityId + '\'' +
                ", timeSlotId='" + timeSlotId + '\'' +
                ", userId='" + userId + '\'' +
                ", purpose='" + purpose + '\'' +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}