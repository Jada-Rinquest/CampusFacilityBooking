/* =========================================================
   CAMPUS FACILITY BOOKING
   EQUIPMENT MANAGEMENT JAVASCRIPT
========================================================= */


/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const EQUIPMENT_API = `${API_BASE_URL}/equipment`;
const FACILITY_API = `${API_BASE_URL}/facility`;


/* =========================================================
   STATE
========================================================= */

let equipmentItems = [];
let facilities = [];

let editingEquipmentId = null;

let currentPageNumber = 1;

const PAGE_SIZE = 10;


/* =========================================================
   DOM ELEMENTS
========================================================= */

// New equipment
const newEquipmentBtn = document.getElementById("newEquipmentBtn");
const equipmentFormSection = document.getElementById("equipmentFormSection");
const closeFormBtn = document.getElementById("closeFormBtn");
const cancelEquipmentBtn = document.getElementById("cancelEquipmentBtn");

// Form
const equipmentForm = document.getElementById("equipmentForm");
const formTitle = document.getElementById("formTitle");
const equipmentIdInput = document.getElementById("equipmentId");
const nameInput = document.getElementById("name");
const serialNumberInput = document.getElementById("serialNumber");
const facilitySelect = document.getElementById("facilityId");
const statusSelect = document.getElementById("equipmentStatus");
const submitEquipmentBtn = document.getElementById("submitEquipmentBtn");

// Table
const equipmentTableBody = document.getElementById("equipmentTableBody");
const emptyState = document.getElementById("emptyState");

// Filters
const equipmentSearch = document.getElementById("equipmentSearch");
const statusFilter = document.getElementById("statusFilter");
const facilityFilter = document.getElementById("facilityFilter");

// Refresh
const refreshBtn = document.getElementById("refreshBtn");

// Statistics
const totalEquipment = document.getElementById("totalEquipment");
const availableEquipment = document.getElementById("availableEquipment");
const inUseEquipment = document.getElementById("inUseEquipment");
const maintenanceEquipment = document.getElementById("maintenanceEquipment");

// Table count
const equipmentCount = document.getElementById("equipmentCount");

// Pagination
const previousPage = document.getElementById("previousPage");
const nextPage = document.getElementById("nextPage");
const currentPage = document.getElementById("currentPage");

// View modal
const viewEquipmentModal = document.getElementById("viewEquipmentModal");
const closeViewModalBtn = document.getElementById("closeViewModalBtn");

const viewEquipmentId = document.getElementById("viewEquipmentId");
const viewName = document.getElementById("viewName");
const viewSerialNumber = document.getElementById("viewSerialNumber");
const viewFacility = document.getElementById("viewFacility");
const viewStatus = document.getElementById("viewStatus");

// Mobile menu
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


/* =========================================================
   INITIALISE
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadInitialData();

    setupEventListeners();

});


/* =========================================================
   LOAD INITIAL DATA
========================================================= */

async function loadInitialData() {

    await loadFacilities();

    await loadEquipment();

}


/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {

    if (newEquipmentBtn) {

        newEquipmentBtn.addEventListener(
            "click",
            openEquipmentForm
        );

    }


    if (closeFormBtn) {

        closeFormBtn.addEventListener(
            "click",
            closeEquipmentForm
        );

    }


    if (cancelEquipmentBtn) {

        cancelEquipmentBtn.addEventListener(
            "click",
            closeEquipmentForm
        );

    }


    if (equipmentForm) {

        equipmentForm.addEventListener(
            "submit",
            handleEquipmentSubmit
        );

    }


    if (equipmentSearch) {

        equipmentSearch.addEventListener(
            "input",
            () => {

                currentPageNumber = 1;

                renderEquipment();

            }
        );

    }


    if (statusFilter) {

        statusFilter.addEventListener(
            "change",
            () => {

                currentPageNumber = 1;

                renderEquipment();

            }
        );

    }


    if (facilityFilter) {

        facilityFilter.addEventListener(
            "change",
            () => {

                currentPageNumber = 1;

                renderEquipment();

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


    if (viewEquipmentModal) {

        viewEquipmentModal.addEventListener(
            "click",
            (event) => {

                if (event.target === viewEquipmentModal) {

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

                    renderEquipment();

                }

            }
        );

    }


    if (nextPage) {

        nextPage.addEventListener(
            "click",
            () => {

                currentPageNumber++;

                renderEquipment();

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

function openEquipmentForm() {

    editingEquipmentId = null;

    equipmentForm.reset();

    equipmentIdInput.disabled = false;

    formTitle.textContent = "Register Equipment";

    submitEquipmentBtn.innerHTML = `
        <i class="fa-solid fa-toolbox"></i>
        Save Equipment
    `;

    equipmentFormSection.classList.remove("hidden");

    equipmentFormSection.scrollIntoView({

        behavior: "smooth",
        block: "start"

    });

}


/* =========================================================
   CLOSE FORM
========================================================= */

function closeEquipmentForm() {

    equipmentFormSection.classList.add("hidden");

    equipmentForm.reset();

    editingEquipmentId = null;

    equipmentIdInput.disabled = false;

}


/* =========================================================
   LOAD EQUIPMENT
========================================================= */

async function loadEquipment() {

    try {

        /*
         * Change this URL if your controller uses
         * a different endpoint for findAll().
         */

        const response = await fetch(
            `${EQUIPMENT_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Equipment request failed: ${response.status}`
            );

        }

        equipmentItems = await response.json();

        renderEquipment();

        updateStatistics();

    } catch (error) {

        console.error(
            "Error loading equipment:",
            error
        );

        equipmentItems = [];

        renderEquipment();

        updateStatistics();

    }

}


/* =========================================================
   LOAD FACILITIES
========================================================= */

async function loadFacilities() {

    try {

        /*
         * Change /all if your FacilityController
         * uses a different endpoint.
         */

        const response = await fetch(
            `${FACILITY_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Facility request failed: ${response.status}`
            );

        }

        facilities = await response.json();

        populateFacilityDropdowns();

    } catch (error) {

        console.error(
            "Error loading facilities:",
            error
        );

        facilities = [];

    }

}


/* =========================================================
   POPULATE FACILITY DROPDOWNS
========================================================= */

function populateFacilityDropdowns() {

    if (facilitySelect) {

        facilitySelect.innerHTML = `
            <option value="">
                Select a facility
            </option>
        `;

        facilities.forEach((facility) => {

            const option = document.createElement("option");

            option.value = facility.facilityId;

            option.textContent =
                `${facility.name} (${facility.facilityId})`;

            facilitySelect.appendChild(option);

        });

    }


    if (facilityFilter) {

        facilityFilter.innerHTML = `
            <option value="all">
                All Facilities
            </option>
        `;

        facilities.forEach((facility) => {

            const option = document.createElement("option");

            option.value = facility.facilityId;

            option.textContent = facility.name;

            facilityFilter.appendChild(option);

        });

    }

}


/* =========================================================
   SUBMIT (CREATE OR UPDATE)
========================================================= */

async function handleEquipmentSubmit(event) {

    event.preventDefault();


    const payload = {

        equipmentId: equipmentIdInput.value.trim(),
        name: nameInput.value.trim(),
        serialNumber: serialNumberInput.value.trim(),
        facilityId: facilitySelect.value,
        equipmentStatus: statusSelect.value

    };


    if (!payload.equipmentId ||
        !payload.name ||
        !payload.serialNumber ||
        !payload.facilityId) {

        showNotification(
            "Please complete all required fields.",
            "error"
        );

        return;

    }


    submitEquipmentBtn.disabled = true;


    try {

        let response;


        if (editingEquipmentId) {

            response = await fetch(
                `${EQUIPMENT_API}/update`,
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
                `${EQUIPMENT_API}/create`,
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
            editingEquipmentId
                ? "Equipment updated successfully."
                : "Equipment registered successfully."
        );


        closeEquipmentForm();

        await loadEquipment();

    } catch (error) {

        console.error(
            "Error saving equipment:",
            error
        );

        showNotification(
            "Could not save the equipment.",
            "error"
        );

    } finally {

        submitEquipmentBtn.disabled = false;

    }

}


/* =========================================================
   RENDER TABLE
========================================================= */

function renderEquipment() {

    const filtered = getFilteredEquipment();


    const totalPages =
        Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));


    if (currentPageNumber > totalPages) {

        currentPageNumber = totalPages;

    }


    const start = (currentPageNumber - 1) * PAGE_SIZE;

    const pageItems = filtered.slice(start, start + PAGE_SIZE);


    equipmentTableBody.innerHTML = "";


    if (filtered.length === 0) {

        emptyState.style.display = "block";

    } else {

        emptyState.style.display = "none";

        pageItems.forEach((item) => {

            equipmentTableBody.appendChild(
                createEquipmentRow(item)
            );

        });

    }


    updateEquipmentCount(filtered.length);

    updatePagination(filtered.length);

}


/* =========================================================
   FILTER
========================================================= */

function getFilteredEquipment() {

    const term =
        equipmentSearch
            ? equipmentSearch.value.trim().toLowerCase()
            : "";

    const status =
        statusFilter ? statusFilter.value : "all";

    const facility =
        facilityFilter ? facilityFilter.value : "all";


    return equipmentItems.filter((item) => {

        const matchesTerm =
            term === "" ||
            String(item.equipmentId || "")
                .toLowerCase().includes(term) ||
            String(item.name || "")
                .toLowerCase().includes(term) ||
            String(item.serialNumber || "")
                .toLowerCase().includes(term);


        const matchesStatus =
            status === "all" ||
            item.equipmentStatus === status;


        const matchesFacility =
            facility === "all" ||
            item.facilityId === facility;


        return matchesTerm &&
            matchesStatus &&
            matchesFacility;

    });

}


/* =========================================================
   CREATE TABLE ROW
========================================================= */

function createEquipmentRow(item) {

    const row = document.createElement("tr");


    row.innerHTML = `

        <td>
            <span class="equipment-id">
                ${escapeHTML(item.equipmentId)}
            </span>
        </td>

        <td>
            ${escapeHTML(item.name)}
        </td>

        <td>
            ${escapeHTML(item.serialNumber)}
        </td>

        <td>
            ${escapeHTML(findFacilityName(item.facilityId))}
        </td>

        <td>
            <span class="status-badge ${statusClass(item.equipmentStatus)}">
                ${escapeHTML(readable(item.equipmentStatus))}
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
        () => viewEquipment(item.equipmentId)
    );

    buttons[1].addEventListener(
        "click",
        () => editEquipment(item.equipmentId)
    );

    buttons[2].addEventListener(
        "click",
        () => deleteEquipment(item.equipmentId)
    );


    return row;

}


/* =========================================================
   VIEW
========================================================= */

async function viewEquipment(equipmentId) {

    try {

        const response = await fetch(
            `${EQUIPMENT_API}/read/${equipmentId}`
        );

        if (!response.ok) {

            throw new Error(
                `Read failed: ${response.status}`
            );

        }

        const item = await response.json();


        viewEquipmentId.textContent =
            item.equipmentId || "-";

        viewName.textContent =
            item.name || "-";

        viewSerialNumber.textContent =
            item.serialNumber || "-";

        viewFacility.textContent =
            findFacilityName(item.facilityId);

        viewStatus.textContent =
            readable(item.equipmentStatus);


        viewEquipmentModal.classList.add("active");

    } catch (error) {

        console.error(
            "Error reading equipment:",
            error
        );

        showNotification(
            "Could not load the equipment.",
            "error"
        );

    }

}


/* =========================================================
   CLOSE VIEW MODAL
========================================================= */

function closeViewModal() {

    viewEquipmentModal.classList.remove("active");

}


/* =========================================================
   EDIT
========================================================= */

function editEquipment(equipmentId) {

    const item = equipmentItems.find(

        (equipment) => equipment.equipmentId === equipmentId

    );


    if (!item) {

        return;

    }


    editingEquipmentId = equipmentId;


    equipmentIdInput.value = item.equipmentId;
    equipmentIdInput.disabled = true;

    nameInput.value = item.name || "";
    serialNumberInput.value = item.serialNumber || "";
    facilitySelect.value = item.facilityId || "";
    statusSelect.value = item.equipmentStatus || "AVAILABLE";


    formTitle.textContent = "Edit Equipment";

    submitEquipmentBtn.innerHTML = `
        <i class="fa-solid fa-floppy-disk"></i>
        Update Equipment
    `;


    equipmentFormSection.classList.remove("hidden");

    equipmentFormSection.scrollIntoView({

        behavior: "smooth",
        block: "start"

    });

}


/* =========================================================
   DELETE
========================================================= */

async function deleteEquipment(equipmentId) {

    const confirmed = window.confirm(
        `Delete equipment ${equipmentId}?`
    );


    if (!confirmed) {

        return;

    }


    try {

        const response = await fetch(
            `${EQUIPMENT_API}/delete/${equipmentId}`,
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
            "Equipment deleted."
        );

        await loadEquipment();

    } catch (error) {

        console.error(
            "Error deleting equipment:",
            error
        );

        showNotification(
            "Could not delete the equipment.",
            "error"
        );

    }

}


/* =========================================================
   STATISTICS
========================================================= */

function updateStatistics() {

    const available = equipmentItems.filter(
        (item) => item.equipmentStatus === "AVAILABLE"
    ).length;

    const inUse = equipmentItems.filter(
        (item) => item.equipmentStatus === "IN_USE"
    ).length;

    const underMaintenance = equipmentItems.filter(
        (item) => item.equipmentStatus === "UNDER_MAINTENANCE"
    ).length;


    if (totalEquipment) {

        totalEquipment.textContent = equipmentItems.length;

    }

    if (availableEquipment) {

        availableEquipment.textContent = available;

    }

    if (inUseEquipment) {

        inUseEquipment.textContent = inUse;

    }

    if (maintenanceEquipment) {

        maintenanceEquipment.textContent = underMaintenance;

    }

}


/* =========================================================
   HELPERS
========================================================= */

function findFacilityName(facilityId) {

    const facility = facilities.find(

        (item) => item.facilityId === facilityId

    );


    return facility
        ? facility.name
        : (facilityId || "-");

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

    if (status === "AVAILABLE") {

        return "available";

    }

    if (status === "IN_USE") {

        return "in-use";

    }

    if (status === "UNDER_MAINTENANCE") {

        return "under-maintenance";

    }

    return "";

}


function updateEquipmentCount(count) {

    if (!equipmentCount) {

        return;

    }


    equipmentCount.textContent =
        `Showing ${count} item${count === 1 ? "" : "s"}`;

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
            ".equipment-notification"
        );


    if (existing) {

        existing.remove();

    }


    const notification =
        document.createElement("div");


    notification.className =
        `equipment-notification ${type}`;


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
