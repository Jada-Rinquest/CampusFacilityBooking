
/*
 * Author: Ayren Villet (223120030)
 * Date: 12 July 2026
 */

package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.repository.InvoiceRepository;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    public InvoiceServiceImpl(InvoiceRepository repository) {
        this.repository = repository;
    }

    /* =========================================================
       CREATE INVOICE
    ========================================================= */

    @Override
    @Transactional
    public Invoice create(Invoice invoice) {

        if (invoice == null ||
                invoice.getInvoiceId() == null ||
                invoice.getInvoiceId().isBlank()) {

            return null;
        }

        if (repository.existsById(invoice.getInvoiceId())) {
            return null;
        }

        // Save invoice with its selected status
        return repository.save(invoice);
    }

    /* =========================================================
       READ INVOICE
    ========================================================= */

    @Override
    @Transactional(readOnly = true)
    public Invoice read(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        return repository.findById(id).orElse(null);
    }

    /* =========================================================
       UPDATE / EDIT INVOICE
    ========================================================= */

    @Override
    @Transactional
    public Invoice update(Invoice invoice) {

        if (invoice == null ||
                invoice.getInvoiceId() == null ||
                invoice.getInvoiceId().isBlank()) {

            return null;
        }

        // Check whether invoice exists
        if (!repository.existsById(invoice.getInvoiceId())) {
            return null;
        }

        // Save changes to amount, due date, booking
        // and invoice status
        return repository.save(invoice);
    }

    /* =========================================================
       DELETE INVOICE
    ========================================================= */

    @Override
    @Transactional
    public boolean delete(String id) {

        if (id == null || id.isBlank()) {
            return false;
        }

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }

    /* =========================================================
       GET ALL INVOICES
    ========================================================= */

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getAll() {

        return repository.findAll();
    }
}
