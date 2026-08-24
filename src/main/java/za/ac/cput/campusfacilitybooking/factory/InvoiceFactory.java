
/* Author: Ayren Villet (223120030)
     Date: 28 June 2026 */

package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Invoice;

import java.time.LocalDate;

public class InvoiceFactory {

    public static Invoice createInvoice(String invoiceId,
                                        String bookingId,
                                        double amount,
                                        LocalDate issueDate,
                                        LocalDate dueDate) {

        if (invoiceId == null || invoiceId.isEmpty()) {
            throw new IllegalArgumentException("Invoice ID is required");
        }

        if (bookingId == null || bookingId.isEmpty()) {
            throw new IllegalArgumentException("Booking ID is required");
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
                bookingId,
                amount,
                issueDate,
                dueDate
        );
    }
}