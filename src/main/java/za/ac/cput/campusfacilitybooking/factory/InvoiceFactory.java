package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;

import java.time.LocalDate;

public class InvoiceFactory {

    public static Invoice createInvoice(String invoiceId,
                                        Booking booking,  // ← Changed from String to Booking
                                        double amount,
                                        LocalDate issueDate,
                                        LocalDate dueDate) {

        if (invoiceId == null || invoiceId.isEmpty()) {
            throw new IllegalArgumentException("Invoice ID is required");
        }

        if (booking == null) {
            throw new IllegalArgumentException("Booking is required");
        }

        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date is required");
        }

        if (dueDate == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        return new Invoice(
                invoiceId,
                booking,  // ← Pass Booking object
                amount,
                issueDate,
                dueDate
        );
    }
}