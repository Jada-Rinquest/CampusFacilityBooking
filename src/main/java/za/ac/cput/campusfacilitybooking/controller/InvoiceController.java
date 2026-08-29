package za.ac.cput.campusfacilitybooking.controller;

/* Author: Ayren Villet (223120030)
   Date: 28 June 2026 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.time.LocalDate;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService service;

    @Autowired
    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> create(@RequestBody InvoiceRequest request) {
        Invoice invoice = InvoiceFactory.createInvoice(
                request.getInvoiceId(),
                request.getBookingId(),
                request.getAmount(),
                request.getIssueDate(),
                request.getDueDate()
        );
        return ResponseEntity.ok(service.create(invoice));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Invoice> read(@PathVariable String id) {
        Invoice invoice = service.read(id);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/update")
    public ResponseEntity<Invoice> update(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(service.update(invoice));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

class InvoiceRequest {
    private String invoiceId;
    private String bookingId;
    private double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;

    // Getters and Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}