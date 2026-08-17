package za.ac.cput.campusfacilitybooking.domain;

/* Author: Nuyra Swanson (221290524)
     Date: 21 June 2026 */

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    protected Booking() {

    }

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.facilityId = builder.facilityId;
        this.timeSlotId = builder.timeSlotId;
        this.userId = builder.userId;
        this.purpose = builder.purpose;
        this.bookingStatusId = builder.bookingStatusId;
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

    public static class Builder {

        private String bookingId;
        private String facilityId;
        private String timeSlotId;
        private String userId;
        private String purpose;
        private String bookingStatusId;

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

        public Builder setBookingStatusId(String bookingStatusId) {
            this.bookingStatusId = bookingStatusId;
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
                ", bookingStatusId='" + bookingStatusId + '\'' +
                '}';
    }
}