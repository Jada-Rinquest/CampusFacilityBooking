package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Booking;
import za.ac.cput.campusfacilitybooking.domain.Invoice;
import za.ac.cput.campusfacilitybooking.domain.enums.BookingStatus;
import za.ac.cput.campusfacilitybooking.factory.BookingFactory;
import za.ac.cput.campusfacilitybooking.factory.InvoiceFactory;
import za.ac.cput.campusfacilitybooking.repository.InvoiceRepository;
import za.ac.cput.campusfacilitybooking.service.impl.InvoiceServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    private InvoiceRepository repository;
    private InvoiceServiceImpl service;
    private Invoice invoice;
    private Booking booking;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(InvoiceRepository.class);
        service = new InvoiceServiceImpl(repository);

        // Create a Booking first
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
                booking,  // ← Pass Booking object
                150.00,
                LocalDate.of(2026, 7, 12),
                LocalDate.of(2026, 8, 12)
        );
    }

    @Test
    void testCreate() {
        when(repository.save(invoice)).thenReturn(invoice);

        Invoice created = service.create(invoice);

        assertNotNull(created);
        assertEquals("INV001", created.getInvoiceId());
    }

    @Test
    void testRead() {
        when(repository.findById("INV001"))
                .thenReturn(Optional.of(invoice));

        Invoice found = service.read("INV001");

        assertNotNull(found);
        assertEquals(150.00, found.getAmount());
        assertNotNull(found.getBooking());
        assertEquals("B001", found.getBooking().getBookingId());
    }

    @Test
    void testUpdate() {
        when(repository.save(invoice)).thenReturn(invoice);

        Invoice updated = service.update(invoice);

        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        when(repository.existsById("INV001")).thenReturn(true);

        boolean deleted = service.delete("INV001");

        assertTrue(deleted);
        verify(repository).deleteById("INV001");
    }
}