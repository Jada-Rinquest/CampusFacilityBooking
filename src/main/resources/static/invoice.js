/* =========================================================
   INVOICE MANAGEMENT JAVASCRIPT
   Campus Facility Booking System
========================================================= */


/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const INVOICE_API = `${API_BASE_URL}/invoice`;
const BOOKING_API = `${API_BASE_URL}/booking`;


/* =========================================================
   DOM ELEMENTS
========================================================= */

// New invoice
const newInvoiceBtn = document.getElementById("newInvoiceBtn");
const invoiceFormSection = document.getElementById("invoiceFormSection");
const closeFormBtn = document.getElementById("closeFormBtn");
const cancelInvoiceBtn = document.getElementById("cancelInvoiceBtn");

// Form
const invoiceForm = document.getElementById("invoiceForm");
const invoiceId = document.getElementById("invoiceId");
const bookingSelect = document.getElementById("bookingId");
const amountInput = document.getElementById("amount");
const dueDate = document.getElementById("dueDate");
const statusSelect = document.getElementById("invoiceStatus");
const descriptionInput = document.getElementById("description");
const descriptionCount = document.getElementById("descriptionCount");
const submitInvoiceBtn = document.getElementById("submitInvoiceBtn");

// Table
const invoiceTableBody = document.getElementById("invoiceTableBody");
const emptyState = document.getElementById("emptyState");

// Filters
const invoiceSearch = document.getElementById("invoiceSearch");
const statusFilter = document.getElementById("statusFilter");
const dateFilter = document.getElementById("dateFilter");

// Refresh
const refreshBtn = document.getElementById("refreshBtn");

// Statistics
const totalInvoices = document.getElementById("totalInvoices");
const paidInvoices = document.getElementById("paidInvoices");
const pendingInvoices = document.getElementById("pendingInvoices");
const overdueInvoices = document.getElementById("overdueInvoices");

// Table count
const invoiceCount = document.getElementById("invoiceCount");

// Pagination
const previousPage = document.getElementById("previousPage");
const nextPage = document.getElementById("nextPage");
const currentPage = document.getElementById("currentPage");

// View modal
const viewInvoiceModal = document.getElementById("viewInvoiceModal");
const closeViewModalBtn = document.getElementById("closeViewModalBtn");

const viewInvoiceId = document.getElementById("viewInvoiceId");
const viewBooking = document.getElementById("viewBooking");
const viewAmount = document.getElementById("viewAmount");
const viewDueDate = document.getElementById("viewDueDate");
const viewStatus = document.getElementById("viewStatus");
const viewCreatedDate = document.getElementById("viewCreatedDate");
const viewDescription = document.getElementById("viewDescription");

// Mobile menu
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


/* =========================================================
   APPLICATION STATE
========================================================= */

let invoices = [];
let bookings = [];

let editingInvoiceId = null;

let currentPageNumber = 1;

const invoicesPerPage = 8;


/* =========================================================
   INITIALISE PAGE
========================================================= */

document.addEventListener("DOMContentLoaded", async function () {

    setMinimumDate();

    await loadInitialData();

    setupEventListeners();

});


/* =========================================================
   INITIAL DATA
========================================================= */

async function loadInitialData() {

    try {

        await Promise.all([
            loadInvoices(),
            loadBookings()
        ]);

    } catch (error) {

        console.error("Error loading page data:", error);

        showNotification(
            "Unable to load invoice data. Make sure the Spring Boot backend is running.",
            "error"
        );

    }

}


/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {

    /* New invoice */

    if (newInvoiceBtn) {

        newInvoiceBtn.addEventListener("click", openInvoiceForm);

    }


    /* Close form */

    if (closeFormBtn) {

        closeFormBtn.addEventListener(
            "click",
            closeInvoiceForm
        );

    }


    /* Cancel form */

    if (cancelInvoiceBtn) {

        cancelInvoiceBtn.addEventListener(
            "click",
            closeInvoiceForm
        );

    }


    /* Invoice form */

    if (invoiceForm) {

        invoiceForm.addEventListener(
            "submit",
            handleInvoiceSubmit
        );

    }


    /* Description character counter */

    if (descriptionInput) {

        descriptionInput.addEventListener(
            "input",
            updateDescriptionCounter
        );

    }


    /* Search */

    if (invoiceSearch) {

        invoiceSearch.addEventListener(
            "input",
            function () {

                currentPageNumber = 1;

                renderInvoices();

            }
        );

    }


    /* Status filter */

    if (statusFilter) {

        statusFilter.addEventListener(
            "change",
            function () {

                currentPageNumber = 1;

                renderInvoices();

            }
        );

    }


    /* Date filter */

    if (dateFilter) {

        dateFilter.addEventListener(
            "change",
            function () {

                currentPageNumber = 1;

                renderInvoices();

            }
        );

    }


    /* Refresh */

    if (refreshBtn) {

        refreshBtn.addEventListener(
            "click",
            async function () {

                await loadInvoices();

                showNotification(
                    "Invoices refreshed successfully.",
                    "success"
                );

            }
        );

    }


    /* Pagination */

    if (previousPage) {

        previousPage.addEventListener(
            "click",
            function () {

                if (currentPageNumber > 1) {

                    currentPageNumber--;

                    renderInvoices();

                }

            }
        );

    }


    if (nextPage) {

        nextPage.addEventListener(
            "click",
            function () {

                currentPageNumber++;

                renderInvoices();

            }
        );

    }


    /* View modal */

    if (closeViewModalBtn) {

        closeViewModalBtn.addEventListener(
            "click",
            closeViewModal
        );

    }


    /* Close modal when clicking outside */

    if (viewInvoiceModal) {

        viewInvoiceModal.addEventListener(
            "click",
            function (event) {

                if (event.target === viewInvoiceModal) {

                    closeViewModal();

                }

            }
        );

    }


    /* Mobile menu */

    if (mobileMenuBtn) {

        mobileMenuBtn.addEventListener(
            "click",
            function () {

                sidebar.classList.toggle("open");

            }
        );

    }

}


/* =========================================================
   OPEN INVOICE FORM
========================================================= */

function openInvoiceForm() {

    editingInvoiceId = null;

    invoiceForm.reset();

    if (descriptionCount) {

        descriptionCount.textContent = "0 / 500";

    }

    document.getElementById("formTitle").textContent =
        "Create an Invoice";

    submitInvoiceBtn.innerHTML =
        '<i class="fa-solid fa-file-invoice"></i> Create Invoice';

    invoiceFormSection.classList.remove("hidden");

    invoiceFormSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });

}


/* =========================================================
   CLOSE INVOICE FORM
========================================================= */

function closeInvoiceForm() {

    invoiceFormSection.classList.add("hidden");

    invoiceForm.reset();

    editingInvoiceId = null;

    if (descriptionCount) {

        descriptionCount.textContent = "0 / 500";

    }

}


/* =========================================================
   LOAD INVOICES
========================================================= */

async function loadInvoices() {

    try {

        const response = await fetch(
            `${INVOICE_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Invoice request failed: ${response.status}`
            );

        }

        invoices = await response.json();

        renderInvoices();

        updateStatistics();

    } catch (error) {

        console.error(
            "Error loading invoices:",
            error
        );

        invoices = [];

        renderInvoices();

    }

}


/* =========================================================
   LOAD BOOKINGS
========================================================= */

async function loadBookings() {

    try {

        const response = await fetch(
            `${BOOKING_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Booking request failed: ${response.status}`
            );

        }

        bookings = await response.json();

        populateBookingDropdown();

    } catch (error) {

        console.error(
            "Error loading bookings:",
            error
        );

    }

}


/* =========================================================
   POPULATE BOOKING DROPDOWN
========================================================= */

function populateBookingDropdown() {

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
   DESCRIPTION CHARACTER COUNTER
========================================================= */

function updateDescriptionCounter() {

    if (!descriptionInput || !descriptionCount) {

        return;

    }

    const length = descriptionInput.value.length;

    descriptionCount.textContent = `${length} / 500`;

}


/* =========================================================
   MINIMUM DATE
========================================================= */

function setMinimumDate() {

    if (!dueDate) {

        return;

    }

    const today = new Date();

    const year = today.getFullYear();

    const month = String(today.getMonth() + 1).padStart(2, "0");

    const day = String(today.getDate()).padStart(2, "0");

    dueDate.min = `${year}-${month}-${day}`;

}


/* =========================================================
   CREATE / UPDATE INVOICE
========================================================= */

async function handleInvoiceSubmit(event) {

    event.preventDefault();


    const id = invoiceId.value.trim();
    const bookingId = bookingSelect.value;
    const amount = amountInput.value;
    const due = dueDate.value;
    const status = statusSelect.value;
    const description = descriptionInput.value.trim();


    /* Validation */

    if (!id) {

        showNotification(
            "Please enter an Invoice ID.",
            "error"
        );

        return;

    }


    if (!bookingId) {

        showNotification(
            "Please select a booking.",
            "error"
        );

        return;

    }


    if (!amount || parseFloat(amount) <= 0) {

        showNotification(
            "Please enter a valid amount.",
            "error"
        );

        return;

    }


    if (!due) {

        showNotification(
            "Please select a due date.",
            "error"
        );

        return;

    }


    if (!status) {

        showNotification(
            "Please select an invoice status.",
            "error"
        );

        return;

    }


    const invoiceData = {

        invoiceId: id,
        bookingId: bookingId,
        amount: parseFloat(amount),
        dueDate: due,
        invoiceStatus: status,
        description: description || ""

    };


    try {

        submitInvoiceBtn.disabled = true;

        submitInvoiceBtn.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';


        let response;


        /* UPDATE */

        if (editingInvoiceId) {

            response = await fetch(
                `${INVOICE_API}/update`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(invoiceData)
                }
            );

        }

        /* CREATE */

        else {

            response = await fetch(
                `${INVOICE_API}/create`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(invoiceData)
                }
            );

        }


        if (!response.ok) {

            const errorText = await response.text();

            throw new Error(
                errorText || `Server error: ${response.status}`
            );

        }


        const savedInvoice = await response.json();

        console.log("Saved invoice:", savedInvoice);


        showNotification(
            editingInvoiceId
                ? "Invoice updated successfully."
                : "Invoice created successfully.",
            "success"
        );


        closeInvoiceForm();

        await loadInvoices();


    } catch (error) {

        console.error("Error saving invoice:", error);

        showNotification(
            "Unable to save the invoice.",
            "error"
        );


    } finally {

        submitInvoiceBtn.disabled = false;

        submitInvoiceBtn.innerHTML =
            editingInvoiceId
                ? '<i class="fa-solid fa-floppy-disk"></i> Update Invoice'
                : '<i class="fa-solid fa-file-invoice"></i> Create Invoice';

    }

}


/* =========================================================
   RENDER INVOICES
========================================================= */

function renderInvoices() {

    const filteredInvoices = getFilteredInvoices();


    if (filteredInvoices.length === 0) {

        invoiceTableBody.innerHTML = "";

        emptyState.style.display = "block";

        updateInvoiceCount(0);

        updatePagination(0);

        return;

    }


    emptyState.style.display = "none";


    const totalPages = Math.ceil(
        filteredInvoices.length / invoicesPerPage
    );


    if (currentPageNumber > totalPages) {

        currentPageNumber = totalPages;

    }


    const startIndex = (currentPageNumber - 1) * invoicesPerPage;

    const endIndex = startIndex + invoicesPerPage;

    const pageInvoices = filteredInvoices.slice(startIndex, endIndex);


    invoiceTableBody.innerHTML = "";


    pageInvoices.forEach(function (invoice) {

        const row = createInvoiceRow(invoice);

        invoiceTableBody.appendChild(row);

    });


    updateInvoiceCount(filteredInvoices.length);

    updatePagination(filteredInvoices.length);

}


/* =========================================================
   FILTER INVOICES
========================================================= */

function getFilteredInvoices() {

    const search = invoiceSearch.value.trim().toLowerCase();

    const selectedStatus = statusFilter.value;

    const selectedDate = dateFilter.value;


    return invoices.filter(function (invoice) {

        const searchMatch =

            !search ||

            String(invoice.invoiceId).toLowerCase().includes(search) ||

            String(invoice.bookingId).toLowerCase().includes(search) ||

            String(invoice.description).toLowerCase().includes(search);


        const statusMatch =

            selectedStatus === "all" ||

            invoice.invoiceStatus === selectedStatus;


        let dateMatch = true;

        if (selectedDate !== "all") {

            const today = new Date();

            const due = new Date(invoice.dueDate);

            const diffDays = Math.ceil(
                (due - today) / (1000 * 60 * 60 * 24)
            );

            if (selectedDate === "today") {

                dateMatch = diffDays === 0;

            } else if (selectedDate === "week") {

                dateMatch = diffDays >= 0 && diffDays <= 7;

            } else if (selectedDate === "month") {

                dateMatch = diffDays >= 0 && diffDays <= 30;

            }

        }


        return searchMatch && statusMatch && dateMatch;

    });

}


/* =========================================================
   CREATE TABLE ROW
========================================================= */

function createInvoiceRow(invoice) {

    const row = document.createElement("tr");


    const status = invoice.invoiceStatus || "pending";

    const amount = parseFloat(invoice.amount || 0).toFixed(2);

    const isPaid = status === "paid";
    const isOverdue = status === "overdue";
    const isPending = status === "pending";

    let amountClass = "amount";

    if (isPaid) amountClass += " paid";
    else if (isOverdue) amountClass += " overdue";
    else if (isPending) amountClass += " pending";


    row.innerHTML = `

        <td>

            <span class="invoice-id">
                ${escapeHTML(invoice.invoiceId)}
            </span>

        </td>


        <td>

            ${escapeHTML(invoice.bookingId)}

        </td>


        <td>

            <span class="${amountClass}">
                R ${amount}
            </span>

        </td>


        <td>

            ${formatDate(invoice.dueDate)}

        </td>


        <td>

            <span class="status-badge ${status}">

                ${status}

            </span>

        </td>


        <td>

            <div class="table-actions">

                <button
                    type="button"
                    class="table-action"
                    title="View invoice"
                    onclick="viewInvoice('${invoice.invoiceId}')">

                    <i class="fa-regular fa-eye"></i>

                </button>


                <button
                    type="button"
                    class="table-action"
                    title="Edit invoice"
                    onclick="editInvoice('${invoice.invoiceId}')">

                    <i class="fa-solid fa-pen"></i>

                </button>

                ${!isPaid ? `
                <button
                    type="button"
                    class="table-action paid-action"
                    title="Mark as paid"
                    onclick="markAsPaid('${invoice.invoiceId}')">

                    <i class="fa-solid fa-check"></i>

                </button>
                ` : ''}

                <button
                    type="button"
                    class="table-action delete"
                    title="Delete invoice"
                    onclick="deleteInvoice('${invoice.invoiceId}')">

                    <i class="fa-solid fa-trash"></i>

                </button>

            </div>

        </td>

    `;


    return row;

}


/* =========================================================
   VIEW INVOICE
========================================================= */

function viewInvoice(invoiceId) {

    const invoice = invoices.find(
        function (item) {

            return item.invoiceId === invoiceId;

        }
    );


    if (!invoice) {

        return;

    }


    viewInvoiceId.textContent = invoice.invoiceId;

    viewBooking.textContent = invoice.bookingId;

    viewAmount.textContent = `R ${parseFloat(invoice.amount || 0).toFixed(2)}`;

    viewDueDate.textContent = formatDate(invoice.dueDate);

    viewStatus.textContent = invoice.invoiceStatus || "pending";

    viewCreatedDate.textContent = formatDate(invoice.createdDate) || "-";

    viewDescription.textContent = invoice.description || "-";


    viewInvoiceModal.classList.add("active");

}


/* =========================================================
   CLOSE VIEW MODAL
========================================================= */

function closeViewModal() {

    viewInvoiceModal.classList.remove("active");

}


/* =========================================================
   EDIT INVOICE
========================================================= */

function editInvoice(invoiceId) {

    const invoice = invoices.find(
        function (item) {

            return item.invoiceId === invoiceId;

        }
    );


    if (!invoice) {

        return;

    }


    editingInvoiceId = invoice.invoiceId;


    invoiceIdInput.value = invoice.invoiceId;

    bookingSelect.value = invoice.bookingId;

    amountInput.value = invoice.amount;

    dueDate.value = invoice.dueDate;

    statusSelect.value = invoice.invoiceStatus || "pending";

    descriptionInput.value = invoice.description || "";


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
   MARK AS PAID
========================================================= */

async function markAsPaid(invoiceId) {

    const confirmed = confirm(
        `Mark invoice ${invoiceId} as paid?`
    );


    if (!confirmed) {

        return;

    }


    try {

        const response = await fetch(
            `${INVOICE_API}/update-status/${invoiceId}?status=paid`,
            {
                method: "PUT"
            }
        );


        if (!response.ok) {

            throw new Error(
                `Update failed: ${response.status}`
            );

        }


        showNotification(
            "Invoice marked as paid.",
            "success"
        );


        await loadInvoices();


    } catch (error) {

        console.error(
            "Error updating invoice status:",
            error
        );


        showNotification(
            "Unable to update invoice status.",
            "error"
        );

    }

}


/* =========================================================
   DELETE INVOICE
========================================================= */

async function deleteInvoice(invoiceId) {

    const confirmed = confirm(
        `Are you sure you want to delete invoice ${invoiceId}?`
    );


    if (!confirmed) {

        return;

    }


    try {

        const response = await fetch(
            `${INVOICE_API}/delete/${invoiceId}`,
            {
                method: "DELETE"
            }
        );


        if (!response.ok) {

            throw new Error(
                `Delete failed: ${response.status}`
            );

        }


        showNotification(
            "Invoice deleted successfully.",
            "success"
        );


        await loadInvoices();


    } catch (error) {

        console.error(
            "Error deleting invoice:",
            error
        );


        showNotification(
            "Unable to delete the invoice.",
            "error"
        );

    }

}


/* =========================================================
   UPDATE STATISTICS
========================================================= */

function updateStatistics() {

    totalInvoices.textContent = invoices.length;


    paidInvoices.textContent = invoices.filter(
        function (invoice) {

            return invoice.invoiceStatus === "paid";

        }
    ).length;


    pendingInvoices.textContent = invoices.filter(
        function (invoice) {

            return invoice.invoiceStatus === "pending";

        }
    ).length;


    overdueInvoices.textContent = invoices.filter(
        function (invoice) {

            return invoice.invoiceStatus === "overdue";

        }
    ).length;

}


/* =========================================================
   FORMAT DATE
========================================================= */

function formatDate(dateString) {

    if (!dateString) {

        return "-";

    }


    const date = new Date(dateString);

    if (isNaN(date.getTime())) {

        return dateString;

    }


    const day = String(date.getDate()).padStart(2, "0");

    const month = String(date.getMonth() + 1).padStart(2, "0");

    const year = date.getFullYear();


    return `${day}/${month}/${year}`;

}


/* =========================================================
   UPDATE INVOICE COUNT
========================================================= */

function updateInvoiceCount(count) {

    if (!invoiceCount) {

        return;

    }

    invoiceCount.textContent =
        `Showing ${count} invoice${count === 1 ? "" : "s"}`;

}


/* =========================================================
   PAGINATION
========================================================= */

function updatePagination(totalItems) {

    const totalPages = Math.ceil(totalItems / invoicesPerPage);

    currentPage.textContent = currentPageNumber;

    previousPage.disabled = currentPageNumber <= 1;

    nextPage.disabled = currentPageNumber >= totalPages || totalPages === 0;

}


/* =========================================================
   NOTIFICATION
========================================================= */

function showNotification(message, type = "success") {

    const existing = document.querySelector(".invoice-notification");

    if (existing) {

        existing.remove();

    }


    const notification = document.createElement("div");

    notification.className = `invoice-notification ${type}`;

    const icon = type === "success" ? "fa-circle-check" : "fa-circle-exclamation";

    notification.innerHTML = `

        <i class="fa-solid ${icon}"></i>

        <span>
            ${escapeHTML(message)}
        </span>

    `;


    notification.style.cssText = `

        position: fixed;
        top: 25px;
        right: 25px;
        z-index: 9999;

        display: flex;
        align-items: center;
        gap: 10px;

        padding: 14px 18px;

        border-radius: 10px;

        background: white;

        box-shadow: 0 10px 30px rgba(0,0,0,0.12);

        border: 1px solid #e5e7eb;

        color: ${
        type === "success"
            ? "#16a34a"
            : "#dc2626"
    };

        font-size: 13px;
        font-weight: 600;

        animation: notificationIn 0.25s ease;

    `;


    document.body.appendChild(notification);


    setTimeout(function () {

        notification.remove();

    }, 3500);

}


/* =========================================================
   ESCAPE HTML
========================================================= */

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


/* =========================================================
   NOTIFICATION ANIMATION
========================================================= */

const notificationStyle = document.createElement("style");

notificationStyle.textContent = `

    @keyframes notificationIn {

        from {

            opacity: 0;

            transform: translateX(20px);

        }

        to {

            opacity: 1;

            transform: translateX(0);

        }

    }

`;

document.head.appendChild(notificationStyle);