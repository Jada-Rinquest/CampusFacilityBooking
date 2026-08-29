package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.InvoiceController;
import za.ac.cput.campusfacilitybooking.controller.InvoiceRequest;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceControllerTest {

    private InvoiceService service;
    private InvoiceController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(InvoiceService.class);
        controller = new InvoiceController(service);
    }

    @Test
    void testCreate() {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceId("INV001");
        request.setBookingId("B001");
        request.setAmount(150.00);
        request.setIssueDate(LocalDate.of(2026, 7, 12));
        request.setDueDate(LocalDate.of(2026, 8, 12));

        Invoice invoice = InvoiceFactory.createInvoice(
                request.getInvoiceId(),
                request.getBookingId(),
                request.getAmount(),
                request.getIssueDate(),
                request.getDueDate()
        );

        when(service.create(any(Invoice.class))).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV001", response.getBody().getInvoiceId());
        assertEquals(150.00, response.getBody().getAmount());
    }

    @Test
    void testRead() {
        Invoice invoice = InvoiceFactory.createInvoice(
                "INV001",
                "B001",
                150.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );

        when(service.read("INV001")).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.read("INV001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INV001", response.getBody().getInvoiceId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("INV999")).thenReturn(null);

        ResponseEntity<Invoice> response = controller.read("INV999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Invoice invoice = InvoiceFactory.createInvoice(
                "INV001",
                "B001",
                200.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );

        when(service.update(invoice)).thenReturn(invoice);

        ResponseEntity<Invoice> response = controller.update(invoice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200.00, response.getBody().getAmount());
    }

    @Test
    void testDelete() {
        when(service.delete("INV001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("INV001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("INV001");
    }
}