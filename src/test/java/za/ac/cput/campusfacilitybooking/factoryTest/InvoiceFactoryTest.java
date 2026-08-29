package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceFactoryTest {

    @Test
    void testCreateInvoice() {
        Invoice invoice = InvoiceFactory.createInvoice(
                "INV001",
                "B001",
                150.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );

        assertNotNull(invoice);
        assertEquals("INV001", invoice.getInvoiceId());
        assertEquals("B001", invoice.getBookingId());
        assertEquals(150.00, invoice.getAmount());
        assertEquals(LocalDate.of(2026, 7, 12), invoice.getIssueDate());
        assertEquals(LocalDate.of(2026, 8, 12), invoice.getDueDate());
    }

    @Test
    void testCreateInvoiceWithInvalidInvoiceId() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "",
                        "B001",
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithInvalidBookingId() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV002",
                        "",
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV003",
                        "B001",
                        -50.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNullIssueDate() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV004",
                        "B001",
                        150.00,
                        null,
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNullDueDate() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV005",
                        "B001",
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        null
                )
        );
    }
}