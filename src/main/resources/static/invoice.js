
/* =========================================================
   INVOICE MANAGEMENT JAVASCRIPT
   Campus Facility Booking System
========================================================= */

const API_BASE_URL = "http://localhost:8080";
const INVOICE_API = `${API_BASE_URL}/invoice`;
const BOOKING_API = `${API_BASE_URL}/booking`;

/* =========================================================
   ELEMENTS
========================================================= */

const newInvoiceBtn = document.getElementById("newInvoiceBtn");
const invoiceFormSection = document.getElementById("invoiceFormSection");
const closeFormBtn = document.getElementById("closeFormBtn");
const cancelInvoiceBtn = document.getElementById("cancelInvoiceBtn");

const invoiceForm = document.getElementById("invoiceForm");
const invoiceIdInput = document.getElementById("invoiceId");
const bookingSelect = document.getElementById("bookingId");
const amountInput = document.getElementById("amount");
const dueDateInput = document.getElementById("dueDate");
const statusSelect = document.getElementById("invoiceStatus");
const descriptionInput = document.getElementById("description");
const descriptionCount = document.getElementById("descriptionCount");
const submitInvoiceBtn = document.getElementById("submitInvoiceBtn");

const invoiceTableBody = document.getElementById("invoiceTableBody");
const emptyState = document.getElementById("emptyState");

const invoiceSearch = document.getElementById("invoiceSearch");
const statusFilter = document.getElementById("statusFilter");
const dateFilter = document.getElementById("dateFilter");
const refreshBtn = document.getElementById("refreshBtn");

const totalInvoices = document.getElementById("totalInvoices");
const paidInvoices = document.getElementById("paidInvoices");
const pendingInvoices = document.getElementById("pendingInvoices");
const overdueInvoices = document.getElementById("overdueInvoices");
const cancelledInvoices = document.getElementById("cancelledInvoices");
const invoiceCount = document.getElementById("invoiceCount");

const previousPage = document.getElementById("previousPage");
const nextPage = document.getElementById("nextPage");
const currentPage = document.getElementById("currentPage");

const viewInvoiceModal = document.getElementById("viewInvoiceModal");
const closeViewModalBtn = document.getElementById("closeViewModalBtn");

const viewInvoiceId = document.getElementById("viewInvoiceId");
const viewBooking = document.getElementById("viewBooking");
const viewAmount = document.getElementById("viewAmount");
const viewDueDate = document.getElementById("viewDueDate");
const viewStatus = document.getElementById("viewStatus");
const viewCreatedDate = document.getElementById("viewCreatedDate");
const viewDescription = document.getElementById("viewDescription");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");

/* =========================================================
   STATE
========================================================= */

let invoices = [];
let bookings = [];
let editingInvoiceId = null;
let currentPageNumber = 1;

const invoicesPerPage = 8;

const VALID_STATUSES = [
    "paid",
    "pending",
    "overdue",
    "cancelled"
];

/* =========================================================
   INITIALISE
========================================================= */

document.addEventListener("DOMContentLoaded", async function () {

    setupEventListeners();

    await Promise.all([
        loadInvoices(),
        loadBookings()
    ]);
});

/* =========================================================
   NORMALISE BACKEND DATA
========================================================= */

function getBookingId(invoice) {

    return invoice.booking?.bookingId ||
        invoice.bookingId ||
        "";
}

function getInvoiceStatus(invoice) {

    // Always prefer the status stored in MySQL.
    const storedStatus = String(
        invoice.invoiceStatus || ""
    ).trim().toLowerCase();

    if (VALID_STATUSES.includes(storedStatus)) {
        return storedStatus;
    }

    // Compatibility with older invoice records.
    return invoice.paid === true ? "paid" : "pending";
}

function isInvoicePaid(invoice) {
    return getInvoiceStatus(invoice) === "paid";
}

function getInvoiceDescription(invoice) {
    return invoice.description || "";
}

/* =========================================================
   LOAD INVOICES
========================================================= */

async function loadInvoices() {

    try {

        const response = await fetch(`${INVOICE_API}/all`, {
            cache: "no-store"
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error("Invalid invoice response");
        }

        invoices = data;

        renderInvoices();
        updateStatistics();

    } catch (error) {

        console.error("Error loading invoices:", error);

        showNotification(
            "Unable to load invoices.",
            "error"
        );

        throw error;
    }
}

/* =========================================================
   LOAD BOOKINGS
========================================================= */

async function loadBookings() {

    try {

        const response = await fetch(`${BOOKING_API}/all`);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        bookings = await response.json();

        populateBookingDropdown();

    } catch (error) {

        console.error("Error loading bookings:", error);

        showNotification(
            "Unable to load bookings.",
            "error"
        );
    }
}

function populateBookingDropdown() {

    if (!bookingSelect) return;

    bookingSelect.innerHTML =
        '<option value="">Select a booking</option>';

    bookings.forEach(function (booking) {

        const option = document.createElement("option");

        option.value = booking.bookingId;

        option.textContent =
            `${booking.bookingId} - ${booking.purpose || "No description"}`;

        bookingSelect.appendChild(option);
    });
}

/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {

    newInvoiceBtn?.addEventListener(
        "click",
        openInvoiceForm
    );

    closeFormBtn?.addEventListener(
        "click",
        closeInvoiceForm
    );

    cancelInvoiceBtn?.addEventListener(
        "click",
        closeInvoiceForm
    );

    invoiceForm?.addEventListener(
        "submit",
        handleInvoiceSubmit
    );

    descriptionInput?.addEventListener(
        "input",
        updateDescriptionCounter
    );

    invoiceSearch?.addEventListener(
        "input",
        resetAndRender
    );

    statusFilter?.addEventListener(
        "change",
        resetAndRender
    );

    dateFilter?.addEventListener(
        "change",
        resetAndRender
    );

    refreshBtn?.addEventListener("click", async function () {

        try {
            await loadInvoices();
        } catch (error) {
            // loadInvoices already displays the error.
        }
    });

    previousPage?.addEventListener("click", function () {

        if (currentPageNumber > 1) {
            currentPageNumber--;
            renderInvoices();
        }
    });

    nextPage?.addEventListener("click", function () {

        const pages = Math.ceil(
            getFilteredInvoices().length / invoicesPerPage
        );

        if (currentPageNumber < pages) {
            currentPageNumber++;
            renderInvoices();
        }
    });

    closeViewModalBtn?.addEventListener(
        "click",
        closeViewModal
    );

    viewInvoiceModal?.addEventListener("click", function (event) {

        if (event.target === viewInvoiceModal) {
            closeViewModal();
        }
    });

    mobileMenuBtn?.addEventListener("click", function () {

        sidebar?.classList.toggle("open");
    });

    document.addEventListener("keydown", function (event) {

        if (event.key === "Escape") {
            closeViewModal();
        }
    });

    // Handle table buttons.
    invoiceTableBody?.addEventListener("click", function (event) {

        const button = event.target.closest(
            "button[data-action]"
        );

        if (!button || button.disabled) return;

        const id = button.dataset.id;
        const action = button.dataset.action;

        if (action === "view") {
            viewInvoice(id);
        }

        if (action === "edit") {
            editInvoice(id);
        }

        if (action === "paid") {
            markAsPaid(id);
        }

        if (action === "delete") {
            deleteInvoice(id);
        }
    });
}

function resetAndRender() {

    currentPageNumber = 1;
    renderInvoices();
}

/* =========================================================
   OPEN CREATE FORM
========================================================= */

function openInvoiceForm() {

    editingInvoiceId = null;

    invoiceForm.reset();

    invoiceIdInput.readOnly = false;

    statusSelect.value = "pending";

    document.getElementById("formTitle").textContent =
        "Create an Invoice";

    submitInvoiceBtn.innerHTML =
        '<i class="fa-solid fa-file-invoice"></i> Create Invoice';

    updateDescriptionCounter();

    invoiceFormSection.classList.remove("hidden");

    invoiceFormSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

/* =========================================================
   CLOSE FORM
========================================================= */

function closeInvoiceForm() {

    invoiceFormSection.classList.add("hidden");

    invoiceForm.reset();

    editingInvoiceId = null;

    invoiceIdInput.readOnly = false;

    updateDescriptionCounter();
}

/* =========================================================
   CREATE OR UPDATE INVOICE
========================================================= */

async function handleInvoiceSubmit(event) {

    event.preventDefault();

    const id = invoiceIdInput.value.trim();
    const bookingId = bookingSelect.value;
    const amount = Number(amountInput.value);
    const dueDate = dueDateInput.value;

    const selectedStatus = statusSelect.value
        .trim()
        .toLowerCase();

    if (
        !id ||
        !bookingId ||
        !dueDate ||
        !Number.isFinite(amount) ||
        amount <= 0 ||
        !VALID_STATUSES.includes(selectedStatus)
    ) {

        showNotification(
            "Please complete all required fields correctly.",
            "error"
        );

        return;
    }

    const original = editingInvoiceId
        ? invoices.find(i => i.invoiceId === editingInvoiceId)
        : null;

    const booking = bookings.find(
        b => String(b.bookingId) === String(bookingId)
    );

    if (!booking) {

        showNotification(
            "The selected booking could not be found.",
            "error"
        );

        return;
    }

    // UPDATE: send the status to the Java backend.
    // CREATE: use the existing InvoiceRequest endpoint.

    const invoiceData = editingInvoiceId
        ? {
            invoiceId: editingInvoiceId,
            booking: {
                bookingId: bookingId
            },
            amount: amount,
            issueDate: original?.issueDate || todayIso(),
            dueDate: dueDate,
            invoiceStatus: selectedStatus.toUpperCase()
        }
        : {
            invoiceId: id,
            bookingId: bookingId,
            amount: amount,
            issueDate: todayIso(),
            dueDate: dueDate
        };

    const wasEditing = Boolean(editingInvoiceId);

    try {

        submitInvoiceBtn.disabled = true;

        submitInvoiceBtn.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';

        const response = await fetch(
            wasEditing
                ? `${INVOICE_API}/update`
                : `${INVOICE_API}/create`,
            {
                method: wasEditing ? "PUT" : "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(invoiceData)
            }
        );

        if (!response.ok) {

            const details = await response.text();

            throw new Error(
                `HTTP ${response.status}: ${details}`
            );
        }

        const savedInvoice = await response.json();

        // The current create endpoint starts invoices as Pending.
        // For another selected status, update the created invoice.

        if (!wasEditing && selectedStatus !== "pending") {

            const updateResponse = await fetch(
                `${INVOICE_API}/update`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        invoiceId: savedInvoice.invoiceId || id,
                        booking: {
                            bookingId: bookingId
                        },
                        amount: amount,
                        issueDate: savedInvoice.issueDate || todayIso(),
                        dueDate: dueDate,
                        invoiceStatus: selectedStatus.toUpperCase()
                    })
                }
            );

            if (!updateResponse.ok) {

                const details = await updateResponse.text();

                throw new Error(
                    `Invoice created, but status update failed: ${details}`
                );
            }
        }

        // Reload and verify what the backend actually saved.
        await loadInvoices();

        const saved = invoices.find(
            i => String(i.invoiceId) === String(id)
        );

        if (!saved) {
            throw new Error("Saved invoice not found after refresh.");
        }

        if (getInvoiceStatus(saved) !== selectedStatus) {

            throw new Error(
                "The database returned a different invoice status."
            );
        }

        closeInvoiceForm();

        showNotification(
            wasEditing
                ? "Invoice updated successfully."
                : "Invoice created successfully.",
            "success"
        );

    } catch (error) {

        console.error("Error saving invoice:", error);

        showNotification(
            "Invoice could not be fully saved. Check the console.",
            "error"
        );

    } finally {

        submitInvoiceBtn.disabled = false;

        submitInvoiceBtn.innerHTML = editingInvoiceId
            ? '<i class="fa-solid fa-floppy-disk"></i> Update Invoice'
            : '<i class="fa-solid fa-file-invoice"></i> Create Invoice';
    }
}

/* =========================================================
   RENDER INVOICE TABLE
========================================================= */

function renderInvoices() {

    if (!invoiceTableBody) return;

    const filtered = getFilteredInvoices();

    const totalPages = Math.ceil(
        filtered.length / invoicesPerPage
    );

    currentPageNumber = Math.max(
        1,
        Math.min(currentPageNumber, totalPages || 1)
    );

    const start =
        (currentPageNumber - 1) * invoicesPerPage;

    const pageInvoices = filtered.slice(
        start,
        start + invoicesPerPage
    );

    invoiceTableBody.innerHTML = "";

    if (emptyState) {

        emptyState.style.display =
            filtered.length === 0 ? "block" : "none";
    }

    pageInvoices.forEach(function (invoice) {

        invoiceTableBody.appendChild(
            createInvoiceRow(invoice)
        );
    });

    updateInvoiceCount(filtered.length);
    updatePagination(filtered.length);
}

/* =========================================================
   FILTER INVOICES
========================================================= */

function getFilteredInvoices() {

    const search =
        invoiceSearch?.value.trim().toLowerCase() || "";

    const status =
        statusFilter?.value.toLowerCase() || "all";

    const dateFilterValue =
        dateFilter?.value || "all";

    return invoices.filter(function (invoice) {

        const invoiceStatus = getInvoiceStatus(invoice);

        const searchText = [
            invoice.invoiceId,
            getBookingId(invoice),
            getInvoiceDescription(invoice)
        ].join(" ").toLowerCase();

        const searchMatch = searchText.includes(search);

        const statusMatch =
            status === "all" || invoiceStatus === status;

        let dateMatch = true;

        if (dateFilterValue !== "all" && invoice.dueDate) {

            const today = new Date();
            today.setHours(0, 0, 0, 0);

            const due = new Date(
                invoice.dueDate + "T00:00:00"
            );

            const days = Math.round(
                (due - today) / 86400000
            );

            if (dateFilterValue === "today") {
                dateMatch = days === 0;
            }

            if (dateFilterValue === "week") {
                dateMatch = days >= 0 && days <= 7;
            }

            if (dateFilterValue === "month") {
                dateMatch = days >= 0 && days <= 30;
            }
        }

        return searchMatch && statusMatch && dateMatch;
    });
}

/* =========================================================
   CREATE INVOICE TABLE ROW
========================================================= */

function createInvoiceRow(invoice) {

    const row = document.createElement("tr");

    const status = getInvoiceStatus(invoice);
    const paid = isInvoicePaid(invoice);

    const id = escapeHTML(invoice.invoiceId);
    const bookingId = escapeHTML(getBookingId(invoice));

    const amount = Number(
        invoice.amount || 0
    ).toFixed(2);

    const statusLabel =
        status.charAt(0).toUpperCase() + status.slice(1);

    row.innerHTML = `
        <td>
            <span class="invoice-id">${id}</span>
        </td>

        <td>${bookingId}</td>

        <td>
            <span class="amount ${status}">
                R ${amount}
            </span>
        </td>

        <td>
            ${escapeHTML(formatDate(invoice.dueDate))}
        </td>

        <td>
            <span class="status-badge ${status}">
                ${statusLabel}
            </span>
        </td>

        <td>
            <div class="table-actions">

                <button
                    type="button"
                    class="table-action"
                    title="View invoice"
                    data-action="view">

                    <i class="fa-regular fa-eye"></i>
                </button>

                <button
                    type="button"
                    class="table-action"
                    title="Edit invoice"
                    data-action="edit">

                    <i class="fa-solid fa-pen"></i>
                </button>

                ${!paid && status !== "cancelled" ? `
                    <button
                        type="button"
                        class="table-action paid-action"
                        title="Mark as paid"
                        data-action="paid">

                        <i class="fa-solid fa-check"></i>
                    </button>
                ` : ""}

                <button
                    type="button"
                    class="table-action delete"
                    title="Delete invoice"
                    data-action="delete">

                    <i class="fa-solid fa-trash"></i>
                </button>

            </div>
        </td>
    `;

    row.querySelectorAll(
        "button[data-action]"
    ).forEach(function (button) {

        button.dataset.id = invoice.invoiceId;
    });

    return row;
}

/* =========================================================
   VIEW INVOICE
========================================================= */

function viewInvoice(id) {

    const invoice = invoices.find(
        i => String(i.invoiceId) === String(id)
    );

    if (!invoice) {

        showNotification("Invoice not found.", "error");
        return;
    }

    viewInvoiceId.textContent = invoice.invoiceId;

    viewBooking.textContent = getBookingId(invoice);

    viewAmount.textContent =
        `R ${Number(invoice.amount).toFixed(2)}`;

    viewDueDate.textContent =
        formatDate(invoice.dueDate);

    const status = getInvoiceStatus(invoice);

    viewStatus.textContent =
        status.charAt(0).toUpperCase() + status.slice(1);

    viewCreatedDate.textContent =
        formatDate(invoice.issueDate);

    viewDescription.textContent =
        getInvoiceDescription(invoice) || "-";

    viewInvoiceModal.classList.add("active");
}

function closeViewModal() {

    viewInvoiceModal?.classList.remove("active");
}

/* =========================================================
   EDIT INVOICE
========================================================= */

function editInvoice(id) {

    const invoice = invoices.find(
        i => String(i.invoiceId) === String(id)
    );

    if (!invoice) {

        showNotification("Invoice not found.", "error");
        return;
    }

    editingInvoiceId = invoice.invoiceId;

    invoiceIdInput.value = invoice.invoiceId;
    invoiceIdInput.readOnly = true;

    bookingSelect.value = getBookingId(invoice);

    amountInput.value = invoice.amount;

    dueDateInput.value = invoice.dueDate;

    // IMPORTANT: show the actual saved status.
    statusSelect.value = getInvoiceStatus(invoice);

    if (descriptionInput) {

        descriptionInput.value =
            getInvoiceDescription(invoice);
    }

    updateDescriptionCounter();

    document.getElementById("formTitle").textContent =
        "Update Invoice";

    submitInvoiceBtn.innerHTML =
        '<i class="fa-solid fa-floppy-disk"></i> Update Invoice';

    invoiceFormSection.classList.remove("hidden");

    invoiceFormSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

/* =========================================================
   MARK INVOICE AS PAID
========================================================= */

async function markAsPaid(id) {

    const invoice = invoices.find(
        i => String(i.invoiceId) === String(id)
    );

    if (!invoice) {
        showNotification("Invoice not found.", "error");
        return;
    }

    if (getInvoiceStatus(invoice) === "cancelled") {

        showNotification(
            "Cancelled invoices cannot be marked as paid directly.",
            "error"
        );

        return;
    }

    if (!confirm(`Mark invoice ${id} as paid?`)) {
        return;
    }

    try {

        const response = await fetch(
            `${INVOICE_API}/mark-paid/${encodeURIComponent(id)}`,
            {
                method: "PUT"
            }
        );

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        await loadInvoices();

        const updated = invoices.find(
            i => String(i.invoiceId) === String(id)
        );

        if (!updated || getInvoiceStatus(updated) !== "paid") {
            throw new Error("Paid status was not saved.");
        }

        showNotification(
            "Invoice marked as paid successfully.",
            "success"
        );

    } catch (error) {

        console.error("Mark as paid failed:", error);

        showNotification(
            "Unable to confirm invoice payment update.",
            "error"
        );
    }
}

/* =========================================================
   DELETE INVOICE
========================================================= */

async function deleteInvoice(id) {

    if (!confirm(
        `Are you sure you want to delete invoice ${id}?`
    )) {
        return;
    }

    try {

        const response = await fetch(
            `${INVOICE_API}/delete/${encodeURIComponent(id)}`,
            {
                method: "DELETE"
            }
        );

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const deleted = await response.json();

        if (deleted !== true) {
            throw new Error("Server did not delete invoice.");
        }

        await loadInvoices();

        showNotification(
            "Invoice deleted successfully.",
            "success"
        );

    } catch (error) {

        console.error("Delete invoice failed:", error);

        showNotification(
            "Unable to delete invoice.",
            "error"
        );
    }
}

/* =========================================================
   UPDATE DASHBOARD STATISTICS
========================================================= */

function updateStatistics() {

    const totalCount = invoices.length;

    const countStatus = function (status) {

        return invoices.filter(function (invoice) {

            return getInvoiceStatus(invoice) === status;

        }).length;
    };

    const paidCount = countStatus("paid");
    const pendingCount = countStatus("pending");
    const overdueCount = countStatus("overdue");
    const cancelledCount = countStatus("cancelled");

    if (totalInvoices) {
        totalInvoices.textContent = totalCount;
    }

    if (paidInvoices) {
        paidInvoices.textContent = paidCount;
    }

    if (pendingInvoices) {
        pendingInvoices.textContent = pendingCount;
    }

    if (overdueInvoices) {
        overdueInvoices.textContent = overdueCount;
    }

    if (cancelledInvoices) {
        cancelledInvoices.textContent = cancelledCount;
    }
}

/* =========================================================
   PAGINATION
========================================================= */

function updateInvoiceCount(count) {

    if (invoiceCount) {

        invoiceCount.textContent =
            `Showing ${count} invoice${count === 1 ? "" : "s"}`;
    }
}

function updatePagination(count) {

    const pages = Math.ceil(
        count / invoicesPerPage
    );

    if (currentPage) {
        currentPage.textContent = currentPageNumber;
    }

    if (previousPage) {
        previousPage.disabled = currentPageNumber <= 1;
    }

    if (nextPage) {

        nextPage.disabled =
            pages === 0 || currentPageNumber >= pages;
    }
}

/* =========================================================
   HELPERS
========================================================= */

function todayIso() {

    const date = new Date();

    const year = date.getFullYear();

    const month = String(
        date.getMonth() + 1
    ).padStart(2, "0");

    const day = String(
        date.getDate()
    ).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function formatDate(value) {

    if (!value) return "-";

    const date = new Date(
        value + "T00:00:00"
    );

    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}

function updateDescriptionCounter() {

    if (descriptionCount && descriptionInput) {

        descriptionCount.textContent =
            `${descriptionInput.value.length} / 500`;
    }
}

function showNotification(message, type = "success") {

    document.querySelector(
        ".invoice-notification"
    )?.remove();

    const notification = document.createElement("div");

    notification.className =
        `invoice-notification ${type}`;

    notification.style.cssText = `
        position: fixed;
        top: 25px;
        right: 25px;
        z-index: 9999;
        background: white;
        border: 1px solid #e5e7eb;
        border-radius: 10px;
        padding: 14px 18px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.12);
        font-size: 13px;
        font-weight: 600;
        color: ${type === "success" ? "#16a34a" : "#dc2626"};
    `;

    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(function () {
        notification.remove();
    }, 3500);
}

function escapeHTML(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
