package za.ac.cput.campusfacilitybooking.controller;

import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;

public class BookingRequest {
    private String bookingId;
    private String facilityId;
    private String timeSlotId;
    private String userId;
    private String purpose;
    private BookingStatus bookingStatus;
    private String bookingStatusId;

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getFacilityId() { return facilityId; }
    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
    public String getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public BookingStatus getBookingStatus() { 
        if (bookingStatus == null) {
            if ("BS002".equalsIgnoreCase(bookingStatusId)) return BookingStatus.APPROVED;
            if ("BS003".equalsIgnoreCase(bookingStatusId)) return BookingStatus.CANCELLED;
            return BookingStatus.PENDING;
        }
        return bookingStatus; 
    }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getBookingStatusId() { return bookingStatusId; }
    public void setBookingStatusId(String bookingStatusId) { this.bookingStatusId = bookingStatusId; }
}
