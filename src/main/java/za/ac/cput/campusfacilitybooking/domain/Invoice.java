package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    private String invoiceId;

    private double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;

    @OneToOne
    @JoinColumn(name = "booking_id")  // This creates the foreign key relationship
    private Booking booking;          // Use Booking object, not String

    protected Invoice() {
    }

    public Invoice(String invoiceId, Booking booking,
                   double amount, LocalDate issueDate,
                   LocalDate dueDate) {
        this.invoiceId = invoiceId;
        this.booking = booking;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", amount=" + amount +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                '}';
    }
}