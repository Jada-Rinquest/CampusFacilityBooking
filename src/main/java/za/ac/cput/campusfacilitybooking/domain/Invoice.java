package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    private String invoiceId;

    private String bookingId;
    private double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;

    @OneToOne
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    private Booking booking;

    protected Invoice() {
    }

    public Invoice(String invoiceId, String bookingId,
                   double amount, LocalDate issueDate,
                   LocalDate dueDate) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}