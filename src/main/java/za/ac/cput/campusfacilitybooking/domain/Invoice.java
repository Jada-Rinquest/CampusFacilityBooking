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
    private boolean paid = false;

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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    private Invoice(Builder builder) {
        this.invoiceId = builder.invoiceId;
        this.amount = builder.amount;
        this.issueDate = builder.issueDate;
        this.dueDate = builder.dueDate;
        this.booking = builder.booking;
        this.paid = builder.paid;
    }

    public static class Builder {
        private String invoiceId;
        private double amount;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private Booking booking;
        private boolean paid;

        public Builder setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setIssueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder setBooking(Booking booking) {
            this.booking = booking;
            return this;
        }

        public Builder setPaid(boolean paid) {
            this.paid = paid;
            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }
    }
}