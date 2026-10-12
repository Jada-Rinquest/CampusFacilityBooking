/* =========================================================
   CAMPUS FACILITY BOOKING
   MAINTENANCE REQUEST MANAGEMENT JAVASCRIPT
========================================================= */


/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const MAINTENANCE_API = `${API_BASE_URL}/maintenancerequest`;
const EQUIPMENT_API = `${API_BASE_URL}/equipment`;


/* =========================================================
   STATE
========================================================= */

let requests = [];
let equipmentList = [];

let editingRequestId = null;

let currentPageNumber = 1;

const PAGE_SIZE = 10;


/* =========================================================
   DOM ELEMENTS
========================================================= */

// New request
const newRequestBtn = document.getElementById("newRequestBtn");
const requestFormSection = document.getElementById("requestFormSection");
const closeFormBtn = document.getElementById("closeFormBtn");
const cancelRequestBtn = document.getElementById("cancelRequestBtn");

// Form
const requestForm = document.getElementById("requestForm");
const formTitle = document.getElementById("formTitle");
const requestIdInput = document.getElementById("requestId");
const equipmentSelect = document.getElementById("equipmentId");
const reportedByInput = document.getElementById("reportedBy");
const dateReportedInput = document.getElementById("dateReported");
const prioritySelect = document.getElementById("maintenancePriority");
const statusSelect = document.getElementById("maintenanceStatus");
const descriptionInput = document.getElementById("description");
const descriptionCount = document.getElementById("descriptionCount");
const submitRequestBtn = document.getElementById("submitRequestBtn");

// Table
const requestTableBody = document.getElementById("requestTableBody");
const emptyState = document.getElementById("emptyState");

// Filters
const requestSearch = document.getElementById("requestSearch");
const statusFilter = document.getElementById("statusFilter");
const priorityFilter = document.getElementById("priorityFilter");

// Refresh
const refreshBtn = document.getElementById("refreshBtn");

// Statistics
const totalRequests = document.getElementById("totalRequests");
const openRequests = document.getElementById("openRequests");
const inProgressRequests = document.getElementById("inProgressRequests");
const resolvedRequests = document.getElementById("resolvedRequests");

// Table count
const requestCount = document.getElementById("requestCount");

// Pagination
const previousPage = document.getElementById("previousPage");
const nextPage = document.getElementById("nextPage");
const currentPage = document.getElementById("currentPage");

// View modal
const viewRequestModal = document.getElementById("viewRequestModal");
const closeViewModalBtn = document.getElementById("closeViewModalBtn");

const viewRequestId = document.getElementById("viewRequestId");
const viewEquipment = document.getElementById("viewEquipment");
const viewReportedBy = document.getElementById("viewReportedBy");
const viewDateReported = document.getElementById("viewDateReported");
const viewPriority = document.getElementById("viewPriority");
const viewStatus = document.getElementById("viewStatus");
const viewDescription = document.getElementById("viewDescription");

// Mobile menu
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


/* =========================================================
   INITIALISE
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadInitialData();

    setupEventListeners();

    setTodayAsDefault();

});


/* =========================================================
   LOAD INITIAL DATA
========================================================= */

async function loadInitialData() {

    await loadEquipment();

    await loadRequests();

}


/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {

    if (newRequestBtn) {

        newRequestBtn.addEventListener(
            "click",
            openRequestForm
        );

    }


    if (closeFormBtn) {

        closeFormBtn.addEventListener(
            "click",
            closeRequestForm
        );

    }


    if (cancelRequestBtn) {

        cancelRequestBtn.addEventListener(
            "click",
            closeRequestForm
        );

    }


    if (requestForm) {

        requestForm.addEventListener(
            "submit",
            handleRequestSubmit
        );

    }


    if (descriptionInput) {

        descriptionInput.addEventListener(
            "input",
            updateDescriptionCounter
        );

    }


    if (requestSearch) {

        requestSearch.addEventListener(
            "input",
            () => {

                currentPageNumber = 1;

                renderRequests();

            }
        );

    }


    if (statusFilter) {

        statusFilter.addEventListener(
            "change",
            () => {

                currentPageNumber = 1;

                renderRequests();

            }
        );

    }


    if (priorityFilter) {

        priorityFilter.addEventListener(
            "change",
            () => {

                currentPageNumber = 1;

                renderRequests();

            }
        );

    }


    if (refreshBtn) {

        refreshBtn.addEventListener(
            "click",
            loadInitialData
        );

    }


    if (closeViewModalBtn) {

        closeViewModalBtn.addEventListener(
            "click",
            closeViewModal
        );

    }


    if (viewRequestModal) {

        viewRequestModal.addEventListener(
            "click",
            (event) => {

                if (event.target === viewRequestModal) {

                    closeViewModal();

                }

            }
        );

    }


    if (previousPage) {

        previousPage.addEventListener(
            "click",
            () => {

                if (currentPageNumber > 1) {

                    currentPageNumber--;

                    renderRequests();

                }

            }
        );

    }


    if (nextPage) {

        nextPage.addEventListener(
            "click",
            () => {

                currentPageNumber++;

                renderRequests();

            }
        );

    }


    if (mobileMenuBtn) {

        mobileMenuBtn.addEventListener(
            "click",
            () => {

                sidebar.classList.toggle("open");

            }
        );

    }

}


/* =========================================================
   OPEN FORM
========================================================= */

function openRequestForm() {

    editingRequestId = null;

    requestForm.reset();

    requestIdInput.disabled = false;

    formTitle.textContent = "Log a Maintenance Request";

    submitRequestBtn.innerHTML = `
        <i class="fa-solid fa-screwdriver-wrench"></i>
        Submit Request
    `;

    setTodayAsDefault();

    updateDescriptionCounter();

    requestFormSection.classList.remove("hidden");

    requestFormSection.scrollIntoView({

        behavior: "smooth",
        block: "start"

    });

}


/* =========================================================
   CLOSE FORM
========================================================= */

function closeRequestForm() {

    requestFormSection.classList.add("hidden");

    requestForm.reset();

    editingRequestId = null;

    requestIdInput.disabled = false;

    updateDescriptionCounter();

}


/* =========================================================
   LOAD MAINTENANCE REQUESTS
========================================================= */

async function loadRequests() {

    try {

        /*
         * Change this URL if your controller uses
         * a different endpoint for findAll().
         */

        const response = await fetch(
            `${MAINTENANCE_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Maintenance request failed: ${response.status}`
            );

        }

        requests = await response.json();

        renderRequests();

        updateStatistics();

    } catch (error) {

        console.error(
            "Error loading maintenance requests:",
            error
        );

        requests = [];

        renderRequests();

        updateStatistics();

    }

}


/* =========================================================
   LOAD EQUIPMENT
========================================================= */

async function loadEquipment() {

    try {

        /*
         * Change /all if your EquipmentController
         * uses a different endpoint.
         */

        const response = await fetch(
            `${EQUIPMENT_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Equipment request failed: ${response.status}`
            );

        }

        equipmentList = await response.json();

        populateEquipmentDropdown();

    } catch (error) {

        console.error(
            "Error loading equipment:",
            error
        );

        equipmentList = [];

    }

}


/* =========================================================
   POPULATE EQUIPMENT DROPDOWN
========================================================= */

function populateEquipmentDropdown() {

    if (!equipmentSelect) {

        return;

    }


    equipmentSelect.innerHTML = `
        <option value="">
            Select equipment
        </option>
    `;


    equipmentList.forEach((item) => {

        const option = document.createElement("option");

        option.value = item.equipmentId;

        option.textContent =
            `${item.name} (${item.equipmentId})`;

        equipmentSelect.appendChild(option);

    });

}


/* =========================================================
   SUBMIT (CREATE OR UPDATE)
========================================================= */

async function handleRequestSubmit(event) {

    event.preventDefault();


    const payload = {

        requestId: requestIdInput.value.trim(),
        equipmentId: equipmentSelect.value,
        reportedBy: reportedByInput.value.trim(),
        description: descriptionInput.value.trim(),
        dateReported: dateReportedInput.value,
        maintenancePriority: prioritySelect.value,
        maintenanceStatus: statusSelect.value

    };


    if (!payload.requestId ||
        !payload.equipmentId ||
        !payload.reportedBy ||
        !payload.description) {

        showNotification(
            "Please complete all required fields.",
            "error"
        );

        return;

    }


    submitRequestBtn.disabled = true;


    try {

        let response;


        if (editingRequestId) {

            response = await fetch(
                `${MAINTENANCE_API}/update`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                }
            );

        } else {

            response = await fetch(
                `${MAINTENANCE_API}/create`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                }
            );

        }


        if (!response.ok) {

            throw new Error(
                `Save failed: ${response.status}`
            );

        }


        showNotification(
            editingRequestId
                ? "Maintenance request updated successfully."
                : "Maintenance request created successfully."
        );


        closeRequestForm();

        await loadRequests();

    } catch (error) {

        console.error(
            "Error saving maintenance request:",
            error
        );

        showNotification(
            "Could not save the maintenance request.",
            "error"
        );

    } finally {

        submitRequestBtn.disabled = false;

    }

}


/* =========================================================
   RENDER TABLE
========================================================= */

function renderRequests() {

    const filtered = getFilteredRequests();


    const totalPages =
        Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));


    if (currentPageNumber > totalPages) {

        currentPageNumber = totalPages;

    }


    const start = (currentPageNumber - 1) * PAGE_SIZE;

    const pageItems = filtered.slice(start, start + PAGE_SIZE);


    requestTableBody.innerHTML = "";


    if (filtered.length === 0) {

        emptyState.style.display = "block";

    } else {

        emptyState.style.display = "none";

        pageItems.forEach((request) => {

            requestTableBody.appendChild(
                createRequestRow(request)
            );

        });

    }


    updateRequestCount(filtered.length);

    updatePagination(filtered.length);

}


/* =========================================================
   FILTER
========================================================= */

function getFilteredRequests() {

    const term =
        requestSearch
            ? requestSearch.value.trim().toLowerCase()
            : "";

    const status =
        statusFilter ? statusFilter.value : "all";

    const priority =
        priorityFilter ? priorityFilter.value : "all";


    return requests.filter((request) => {

        const matchesTerm =
            term === "" ||
            String(request.requestId || "")
                .toLowerCase().includes(term) ||
            String(request.description || "")
                .toLowerCase().includes(term) ||
            String(request.reportedBy || "")
                .toLowerCase().includes(term) ||
            String(request.equipmentId || "")
                .toLowerCase().includes(term);


        const matchesStatus =
            status === "all" ||
            request.maintenanceStatus === status;


        const matchesPriority =
            priority === "all" ||
            request.maintenancePriority === priority;


        return matchesTerm &&
            matchesStatus &&
            matchesPriority;

    });

}


/* =========================================================
   CREATE TABLE ROW
========================================================= */

function createRequestRow(request) {

    const row = document.createElement("tr");


    const equipmentName =
        findEquipmentName(request.equipmentId);


    row.innerHTML = `

        <td>
            <span class="maintenance-id">
                ${escapeHTML(request.requestId)}
            </span>
        </td>

        <td>
            ${escapeHTML(equipmentName)}
        </td>

        <td>
            ${escapeHTML(request.reportedBy)}
        </td>

        <td>
            ${escapeHTML(request.dateReported)}
        </td>

        <td>
            <span class="priority-badge ${priorityClass(request.maintenancePriority)}">
                ${escapeHTML(readable(request.maintenancePriority))}
            </span>
        </td>

        <td>
            <span class="status-badge ${statusClass(request.maintenanceStatus)}">
                ${escapeHTML(readable(request.maintenanceStatus))}
            </span>
        </td>

        <td>
            <div class="table-actions">

                <button
                        type="button"
                        class="table-action view"
                        title="View">
                    <i class="fa-solid fa-eye"></i>
                </button>

                <button
                        type="button"
                        class="table-action edit"
                        title="Edit">
                    <i class="fa-solid fa-pen"></i>
                </button>

                <button
                        type="button"
                        class="table-action delete"
                        title="Delete">
                    <i class="fa-solid fa-trash"></i>
                </button>

            </div>
        </td>

    `;


    const buttons = row.querySelectorAll(".table-action");


    buttons[0].addEventListener(
        "click",
        () => viewRequest(request.requestId)
    );

    buttons[1].addEventListener(
        "click",
        () => editRequest(request.requestId)
    );

    buttons[2].addEventListener(
        "click",
        () => deleteRequest(request.requestId)
    );


    return row;

}


/* =========================================================
   VIEW
========================================================= */

async function viewRequest(requestId) {

    try {

        const response = await fetch(
            `${MAINTENANCE_API}/read/${requestId}`
        );

        if (!response.ok) {

            throw new Error(
                `Read failed: ${response.status}`
            );

        }

        const request = await response.json();


        viewRequestId.textContent =
            request.requestId || "-";

        viewEquipment.textContent =
            findEquipmentName(request.equipmentId);

        viewReportedBy.textContent =
            request.reportedBy || "-";

        viewDateReported.textContent =
            request.dateReported || "-";

        viewPriority.textContent =
            readable(request.maintenancePriority);

        viewStatus.textContent =
            readable(request.maintenanceStatus);

        viewDescription.textContent =
            request.description || "-";


        viewRequestModal.classList.add("active");

    } catch (error) {

        console.error(
            "Error reading maintenance request:",
            error
        );

        showNotification(
            "Could not load the maintenance request.",
            "error"
        );

    }

}


/* =========================================================
   CLOSE VIEW MODAL
========================================================= */

function closeViewModal() {

    viewRequestModal.classList.remove("active");

}


/* =========================================================
   EDIT
========================================================= */

function editRequest(requestId) {

    const request = requests.find(

        (item) => item.requestId === requestId

    );


    if (!request) {

        return;

    }


    editingRequestId = requestId;


    requestIdInput.value = request.requestId;
    requestIdInput.disabled = true;

    equipmentSelect.value = request.equipmentId || "";
    reportedByInput.value = request.reportedBy || "";
    dateReportedInput.value = request.dateReported || "";
    prioritySelect.value = request.maintenancePriority || "";
    statusSelect.value = request.maintenanceStatus || "OPEN";
    descriptionInput.value = request.description || "";


    formTitle.textContent = "Edit Maintenance Request";

    submitRequestBtn.innerHTML = `
        <i class="fa-solid fa-floppy-disk"></i>
        Update Request
    `;


    updateDescriptionCounter();


    requestFormSection.classList.remove("hidden");

    requestFormSection.scrollIntoView({

        behavior: "smooth",
        block: "start"

    });

}


/* =========================================================
   DELETE
========================================================= */

async function deleteRequest(requestId) {

    const confirmed = window.confirm(
        `Delete maintenance request ${requestId}?`
    );


    if (!confirmed) {

        return;

    }


    try {

        const response = await fetch(
            `${MAINTENANCE_API}/delete/${requestId}`,
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
            "Maintenance request deleted."
        );

        await loadRequests();

    } catch (error) {

        console.error(
            "Error deleting maintenance request:",
            error
        );

        showNotification(
            "Could not delete the maintenance request.",
            "error"
        );

    }

}


/* =========================================================
   STATISTICS
========================================================= */

function updateStatistics() {

    const open = requests.filter(
        (r) => r.maintenanceStatus === "OPEN"
    ).length;

    const inProgress = requests.filter(
        (r) => r.maintenanceStatus === "IN_PROGRESS"
    ).length;

    const resolved = requests.filter(
        (r) => r.maintenanceStatus === "RESOLVED"
    ).length;


    if (totalRequests) {

        totalRequests.textContent = requests.length;

    }

    if (openRequests) {

        openRequests.textContent = open;

    }

    if (inProgressRequests) {

        inProgressRequests.textContent = inProgress;

    }

    if (resolvedRequests) {

        resolvedRequests.textContent = resolved;

    }

}


/* =========================================================
   HELPERS
========================================================= */

function findEquipmentName(equipmentId) {

    const item = equipmentList.find(

        (equipment) => equipment.equipmentId === equipmentId

    );


    return item
        ? `${item.name} (${item.equipmentId})`
        : (equipmentId || "-");

}


function readable(value) {

    if (!value) {

        return "-";

    }


    return String(value)
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(/\b\w/g, (c) => c.toUpperCase());

}


function statusClass(status) {

    if (status === "OPEN") {

        return "open";

    }

    if (status === "IN_PROGRESS") {

        return "in-progress";

    }

    if (status === "RESOLVED") {

        return "resolved";

    }

    return "";

}


function priorityClass(priority) {

    if (priority === "LOW") {

        return "low";

    }

    if (priority === "MEDIUM") {

        return "medium";

    }

    if (priority === "HIGH") {

        return "high";

    }

    return "";

}


function setTodayAsDefault() {

    if (!dateReportedInput) {

        return;

    }


    const today = new Date()
        .toISOString()
        .split("T")[0];


    dateReportedInput.value = today;

}


function updateDescriptionCounter() {

    if (!descriptionInput || !descriptionCount) {

        return;

    }


    descriptionCount.textContent =
        `${descriptionInput.value.length} / 500`;

}


function updateRequestCount(count) {

    if (!requestCount) {

        return;

    }


    requestCount.textContent =
        `Showing ${count} request${count === 1 ? "" : "s"}`;

}


function updatePagination(totalItems) {

    const totalPages =
        Math.max(1, Math.ceil(totalItems / PAGE_SIZE));


    if (currentPage) {

        currentPage.textContent = currentPageNumber;

    }

    if (previousPage) {

        previousPage.disabled = currentPageNumber <= 1;

    }

    if (nextPage) {

        nextPage.disabled = currentPageNumber >= totalPages;

    }

}


/* =========================================================
   NOTIFICATION
========================================================= */

function showNotification(
    message,
    type = "success"
) {

    const existing =
        document.querySelector(
            ".maintenance-notification"
        );


    if (existing) {

        existing.remove();

    }


    const notification =
        document.createElement("div");


    notification.className =
        `maintenance-notification ${type}`;


    const icon =
        type === "success"
            ? "fa-circle-check"
            : "fa-circle-exclamation";


    notification.innerHTML = `

        <i class="fa-solid ${icon}"></i>

        <span>
            ${escapeHTML(message)}
        </span>

    `;


    notification.style.cssText = `

        position: fixed;
        top: 24px;
        right: 24px;

        display: flex;
        align-items: center;
        gap: 10px;

        padding: 14px 20px;

        border-radius: 12px;

        background: ${type === "success" ? "#16a34a" : "#dc2626"};

        color: #ffffff;

        font-size: 14px;
        font-weight: 600;

        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.18);

        z-index: 4000;

        animation: notificationIn 0.25s ease;

    `;


    document.body.appendChild(notification);


    setTimeout(() => {

        notification.remove();

    }, 3000);

}


/* =========================================================
   ESCAPE HTML
========================================================= */

function escapeHTML(value) {

    if (value === null ||
        value === undefined) {

        return "";

    }


    return String(value)
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#039;"
        );

}


/* =========================================================
   NOTIFICATION ANIMATION
========================================================= */

const notificationStyle =
    document.createElement("style");

notificationStyle.textContent = `

    @keyframes notificationIn {

        from {

            opacity: 0;

            transform:
                translateX(20px);

        }

        to {

            opacity: 1;

            transform:
                translateX(0);

        }

    }

`;

document.head.appendChild(
    notificationStyle
);
