
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

    // NEW: Store the actual invoice status in MySQL
    @Column(name = "invoice_status", length = 20)
    private String invoiceStatus = "PENDING";

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    /* =========================================================
       CONSTRUCTORS
    ========================================================= */

    protected Invoice() {
    }

    public Invoice(String invoiceId,
                   Booking booking,
                   double amount,
                   LocalDate issueDate,
                   LocalDate dueDate) {

        this.invoiceId = invoiceId;
        this.booking = booking;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.paid = false;
        this.invoiceStatus = "PENDING";
    }

    /* =========================================================
       GETTERS AND SETTERS
    ========================================================= */

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

    /* =========================================================
       PAID STATUS
    ========================================================= */

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {

        this.paid = paid;

        if (paid) {
            this.invoiceStatus = "PAID";
        } else if ("PAID".equalsIgnoreCase(this.invoiceStatus)) {
            this.invoiceStatus = "PENDING";
        }
    }

    /* =========================================================
       INVOICE STATUS
       PAID / PENDING / OVERDUE / CANCELLED
    ========================================================= */

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {

        if (invoiceStatus == null ||
                invoiceStatus.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invoice status cannot be empty"
            );
        }

        String status = invoiceStatus.trim().toUpperCase();

        if (!status.equals("PAID") &&
                !status.equals("PENDING") &&
                !status.equals("OVERDUE") &&
                !status.equals("CANCELLED")) {

            throw new IllegalArgumentException(
                    "Invalid invoice status: " + invoiceStatus
            );
        }

        this.invoiceStatus = status;

        // Keep the existing paid field synchronized
        this.paid = status.equals("PAID");
    }

    /* =========================================================
       TO STRING
    ========================================================= */

    @Override
    public String toString() {

        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", amount=" + amount +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", paid=" + paid +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                '}';
    }

    /* =========================================================
       BUILDER CONSTRUCTOR
    ========================================================= */

    private Invoice(Builder builder) {

        this.invoiceId = builder.invoiceId;
        this.amount = builder.amount;
        this.issueDate = builder.issueDate;
        this.dueDate = builder.dueDate;
        this.booking = builder.booking;

        // Set status and synchronize paid
        setInvoiceStatus(builder.invoiceStatus);
    }

    /* =========================================================
       BUILDER CLASS
    ========================================================= */

    public static class Builder {

        private String invoiceId;
        private double amount;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private Booking booking;

        private boolean paid = false;

        private String invoiceStatus = "PENDING";

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

            if (paid) {
                this.invoiceStatus = "PAID";
            } else if ("PAID".equals(this.invoiceStatus)) {
                this.invoiceStatus = "PENDING";
            }

            return this;
        }

        public Builder setInvoiceStatus(String invoiceStatus) {

            if (invoiceStatus == null) {
                throw new IllegalArgumentException(
                        "Invoice status cannot be null"
                );
            }

            this.invoiceStatus =
                    invoiceStatus.trim().toUpperCase();

            this.paid = "PAID".equals(this.invoiceStatus);

            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }
    }
}
