/* Author: Ayren Villet (223120030)
     Date: 12 July 2026 */
package za.ac.cput.campusfacilitybooking.service.impl;
import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.repository.InvoiceRepository;
import za.ac.cput.campusfacilitybooking.service.InvoiceService;

import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    public InvoiceServiceImpl(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice create(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public Invoice read(String id) {
        Optional<Invoice> invoice = repository.findById(id);
        return invoice.orElse(null);
    }

    @Override
    public Invoice update(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public boolean delete(String id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }
}