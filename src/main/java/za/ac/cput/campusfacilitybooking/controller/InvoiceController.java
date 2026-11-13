
package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final BookingService bookingService;

    private static final Set<String> VALID_STATUSES =
            Set.of("PAID", "PENDING", "OVERDUE", "CANCELLED");

    @Autowired
    public InvoiceController(
            InvoiceService invoiceService,
            BookingService bookingService) {

        this.invoiceService = invoiceService;
        this.bookingService = bookingService;
    }

    /* =========================================================
       CREATE INVOICE
    ========================================================= */

    @PostMapping("/create")
    public ResponseEntity<Invoice> create(
            @RequestBody InvoiceRequest request) {

        if (request == null ||
                request.getInvoiceId() == null ||
                request.getInvoiceId().isBlank() ||
                request.getBookingId() == null ||
                request.getBookingId().isBlank()) {

            return ResponseEntity.badRequest().build();
        }

        // Prevent duplicate invoice IDs
        if (invoiceService.read(request.getInvoiceId()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Find the selected booking
        Booking booking = bookingService.read(
                request.getBookingId()
        );

        if (booking == null) {
            return ResponseEntity.badRequest().build();
        }

        // Set invoice dates
        LocalDate issueDate = request.getIssueDate() != null
                ? request.getIssueDate()
                : LocalDate.now();

        LocalDate dueDate = request.getDueDate() != null
                ? request.getDueDate()
                : LocalDate.now().plusDays(14);

        // Create invoice using existing factory
        Invoice invoice = InvoiceFactory.createInvoice(
                request.getInvoiceId(),
                booking,
                request.getAmount(),
                issueDate,
                dueDate
        );

        if (invoice == null) {
            return ResponseEntity.badRequest().build();
        }

        // New invoices start as Pending
        invoice.setInvoiceStatus("PENDING");

        Invoice savedInvoice = invoiceService.create(invoice);

        if (savedInvoice == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(savedInvoice);
    }

    /* =========================================================
       GET ALL INVOICES
    ========================================================= */

    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAll() {

        return ResponseEntity.ok(
                invoiceService.getAll()
        );
    }

    /* =========================================================
       GET INVOICE BY ID
    ========================================================= */

    @GetMapping("/read/{id}")
    public ResponseEntity<Invoice> read(
            @PathVariable String id) {

        Invoice invoice = invoiceService.read(id);

        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(invoice);
    }

    /* =========================================================
       UPDATE / EDIT INVOICE
    ========================================================= */

    @PutMapping("/update")
    public ResponseEntity<Invoice> update(
            @RequestBody Invoice invoice) {

        if (invoice == null ||
                invoice.getInvoiceId() == null ||
                invoice.getInvoiceId().isBlank()) {

            return ResponseEntity.badRequest().build();
        }

        // Find the existing invoice
        Invoice existingInvoice = invoiceService.read(
                invoice.getInvoiceId()
        );

        if (existingInvoice == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate invoice status
        String status = invoice.getInvoiceStatus();

        if (status == null ||
                !VALID_STATUSES.contains(
                        status.trim().toUpperCase()
                )) {

            return ResponseEntity.badRequest().build();
        }

        // Update amount and due date
        if (invoice.getAmount() <= 0 ||
                invoice.getDueDate() == null) {

            return ResponseEntity.badRequest().build();
        }

        existingInvoice.setAmount(invoice.getAmount());
        existingInvoice.setDueDate(invoice.getDueDate());

        // Preserve original issue date
        // unless a new date was supplied
        if (invoice.getIssueDate() != null) {
            existingInvoice.setIssueDate(invoice.getIssueDate());
        }

        // Update booking when a booking is supplied
        if (invoice.getBooking() != null &&
                invoice.getBooking().getBookingId() != null) {

            Booking booking = bookingService.read(
                    invoice.getBooking().getBookingId()
            );

            if (booking == null) {
                return ResponseEntity.badRequest().build();
            }

            existingInvoice.setBooking(booking);
        }

        // Save selected status
        // This also synchronizes the paid boolean
        existingInvoice.setInvoiceStatus(
                status.trim().toUpperCase()
        );

        Invoice updatedInvoice = invoiceService.update(
                existingInvoice
        );

        if (updatedInvoice == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedInvoice);
    }

    /* =========================================================
       MARK INVOICE AS PAID
    ========================================================= */

    @PutMapping("/mark-paid/{id}")
    public ResponseEntity<Invoice> markPaid(
            @PathVariable String id) {

        Invoice invoice = invoiceService.read(id);

        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        // Update both invoiceStatus and paid
        invoice.setInvoiceStatus("PAID");

        Invoice updatedInvoice = invoiceService.update(
                invoice
        );

        if (updatedInvoice == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedInvoice);
    }

    /* =========================================================
       DELETE INVOICE
    ========================================================= */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable String id) {

        Invoice existingInvoice = invoiceService.read(id);

        if (existingInvoice == null) {
            return ResponseEntity.notFound().build();
        }

        boolean deleted = invoiceService.delete(id);

        return ResponseEntity.ok(deleted);
    }
}
