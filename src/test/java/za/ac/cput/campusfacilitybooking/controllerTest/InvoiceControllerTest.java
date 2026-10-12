package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.InvoiceController;
import za.ac.cput.campusfacilitybooking.controller.InvoiceRequest;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceControllerTest {

    private InvoiceService invoiceService;
    private BookingService bookingService;
    private InvoiceController controller;
    private Booking booking;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoiceService = Mockito.mock(InvoiceService.class);
        bookingService = Mockito.mock(BookingService.class);
        controller = new InvoiceController(invoiceService, bookingService);

        booking = BookingFactory.createBooking(
                "B001",
                "F001",
                "TS001",
                "U001",
                "Study Session",
                BookingStatus.PENDING
        );

        invoice = InvoiceFactory.createInvoice(
                "INV001",
                booking,
                150.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );
    }

    @Test
    void testCreate() {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceId("INV001");
        request.setBookingId("B001");      // ← Now this works!
        request.setAmount(150.00);
        request.setIssueDate(LocalDate.of(2026, 7, 12));
        request.setDueDate(LocalDate.of(2026, 8, 12));

        when(bookingService.read("B001")).thenReturn(booking);
        when(invoiceService.create(any(Invoice.class))).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV001", response.getBody().getInvoiceId());
        assertEquals(150.00, response.getBody().getAmount());
        assertNotNull(response.getBody().getBooking());
        assertEquals("B001", response.getBody().getBooking().getBookingId());
    }

    @Test
    void testCreateBookingNotFound() {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceId("INV001");
        request.setBookingId("B999");      // ← Now this works!
        request.setAmount(150.00);
        request.setIssueDate(LocalDate.of(2026, 7, 12));
        request.setDueDate(LocalDate.of(2026, 8, 12));

        when(bookingService.read("B999")).thenReturn(null);

        ResponseEntity<Invoice> response = controller.create(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRead() {
        when(invoiceService.read("INV001")).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.read("INV001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV001", response.getBody().getInvoiceId());
        assertEquals(150.00, response.getBody().getAmount());
    }

    @Test
    void testReadNotFound() {
        when(invoiceService.read("INV999")).thenReturn(null);

        ResponseEntity<Invoice> response = controller.read("INV999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        when(invoiceService.update(invoice)).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.update(invoice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV001", response.getBody().getInvoiceId());
    }

    @Test
    void testDelete() {
        when(invoiceService.delete("INV001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("INV001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(invoiceService).delete("INV001");
    }
}