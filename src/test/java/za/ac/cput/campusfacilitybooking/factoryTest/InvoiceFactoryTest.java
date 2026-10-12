package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceFactoryTest {

    @Test
    void testCreateInvoice() {
        // Create a Booking first
        Booking booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        Invoice invoice = InvoiceFactory.createInvoice(
                "INV001",
                booking,  // ← Pass Booking object
                150.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );

        assertNotNull(invoice);
        assertEquals("INV001", invoice.getInvoiceId());
        assertEquals(150.00, invoice.getAmount());
        assertEquals(LocalDate.of(2026, 7, 12), invoice.getIssueDate());
        assertEquals(LocalDate.of(2026, 8, 12), invoice.getDueDate());
        assertNotNull(invoice.getBooking());
        assertEquals("B001", invoice.getBooking().getBookingId());
    }

    @Test
    void testCreateInvoiceWithInvalidInvoiceId() {
        Booking booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "",
                        booking,
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNullBooking() {
        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV002",
                        null,
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNegativeAmount() {
        Booking booking = BookingFactory.createBooking(
                "B002",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV003",
                        booking,
                        -50.00,
                        LocalDate.of(2026, 7, 12),
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNullIssueDate() {
        Booking booking = BookingFactory.createBooking(
                "B003",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV004",
                        booking,
                        150.00,
                        null,
                        LocalDate.of(2026, 8, 12)
                )
        );
    }

    @Test
    void testCreateInvoiceWithNullDueDate() {
        Booking booking = BookingFactory.createBooking(
                "B004",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        assertThrows(IllegalArgumentException.class, () ->
                InvoiceFactory.createInvoice(
                        "INV005",
                        booking,
                        150.00,
                        LocalDate.of(2026, 7, 12),
                        null
                )
        );
    }
}