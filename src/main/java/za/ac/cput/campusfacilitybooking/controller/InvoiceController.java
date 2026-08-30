package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.service.BookingService;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.time.LocalDate;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final BookingService bookingService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, BookingService bookingService) {
        this.invoiceService = invoiceService;
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> create(@RequestBody InvoiceRequest request) {

        Booking booking = bookingService.read(request.getBookingId());
        if (booking == null) {
            return ResponseEntity.badRequest().build();
        }

        Invoice invoice = InvoiceFactory.createInvoice(
                request.getInvoiceId(),
                booking,
                request.getAmount(),
                request.getIssueDate(),
                request.getDueDate()
        );

        return ResponseEntity.ok(invoiceService.create(invoice));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Invoice> read(@PathVariable String id) {
        Invoice invoice = invoiceService.read(id);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/update")
    public ResponseEntity<Invoice> update(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.update(invoice));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(invoiceService.delete(id));
    }
}

// =========================================================
// INVOICE REQUEST CLASS - NOT PUBLIC (package-private)
// =========================================================

class InvoiceRequest {  // ← REMOVED 'public' keyword
    private String invoiceId;
    private String bookingId;
    private double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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
}