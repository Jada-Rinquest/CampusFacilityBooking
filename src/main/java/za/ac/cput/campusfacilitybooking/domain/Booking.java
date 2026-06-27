package za.ac.cput.campusfacilitybooking.domain;

/* Author: Nuyra Swanson (221290524)
     Date: 21 June 2026 */

public class Booking {
    private String bookingId;
    private String facilityId;
    private String timeSlotId;
    private String requesterId;
    private String requesterType;
    private String purpose;
    private String status;

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.facilityId = builder.facilityId;
        this.timeSlotId = builder.timeSlotId;
        this.requesterId = builder.requesterId;
        this.requesterType = builder.requesterType;
        this.purpose = builder.purpose;
        this.status = builder.status;
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

    public String getRequesterId() {
        return requesterId;
    }

    public String getRequesterType() {
        return requesterType;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public static class Builder {
        private String bookingId;
        private String facilityId;
        private String timeSlotId;
        private String requesterId;
        private String requesterType;
        private String purpose;
        private String status;

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

        public Builder setRequesterId(String requesterId) {
            this.requesterId = requesterId;
            return this;
        }

        public Builder setRequesterType(String requesterType) {
            this.requesterType = requesterType;
            return this;
        }

        public Builder setPurpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
