package za.ac.cput.campusfacilitybooking.factory;
/* Author: Ayren Villet (223120030)
     Date: 28 June 2026 */
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;

import java.time.LocalDate;

public class InvoiceFactory {

    public static Invoice createInvoice(String invoiceId,
                                        Booking booking,
                                        double amount,
                                        LocalDate issueDate,
                                        LocalDate dueDate,
                                        boolean paid) {

        if (invoiceId == null || invoiceId.isEmpty()) {
            return null;
        }

        if (booking == null) {
            return null;
        }

        if (amount <= 0) {
            return null;
        }

        if (issueDate == null || dueDate == null) {
            return null;
        }

        if (dueDate.isBefore(issueDate)) {
            return null;
        }

        return new Invoice(
                invoiceId,
                booking,
                amount,
                issueDate,
                dueDate,
                paid
        );
    }
}