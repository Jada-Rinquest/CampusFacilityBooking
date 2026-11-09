
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   BOOKING MANAGEMENT
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const BOOKING_API = `${API_BASE_URL}/booking`;
const USER_API = `${API_BASE_URL}/user`;
const FACILITY_API = `${API_BASE_URL}/facility`;
const TIME_SLOT_API = `${API_BASE_URL}/timeslot`;

let bookings = [];
let users = [];
let facilities = [];
let timeSlots = [];

let editingBookingId = null;
let currentPageNumber = 1;
const bookingsPerPage = 8;

const $ = (id) => document.getElementById(id);

/* =========================================================
   CURRENT USER AND ROLE
========================================================= */

function getCurrentUser() {
    return window.currentUser || null;
}

function isStudent() {
    const user = getCurrentUser();

    if (!user) return false;

    return user.isStudent === true ||
        String(user.role || user.userRole || "")
            .toUpperCase() === "STUDENT";
}

function currentUserId() {
    return String(getCurrentUser()?.userId || "");
}

function canManageBookings() {
    return !isStudent();
}

/* =========================================================
   INITIALISATION
========================================================= */

document.addEventListener("DOMContentLoaded", async () => {
    setMinimumDate();
    setupEventListeners();
    updatePageForRole();

    await loadInitialData();
    await handleFacilityPreselection();
});

async function loadInitialData() {
    await Promise.all([
        loadUsers(),
        loadFacilities(),
        loadTimeSlotsData()
    ]);

    await loadBookings();
}

/* =========================================================
   ROLE-BASED PAGE DISPLAY
========================================================= */

function updatePageForRole() {
    if (!isStudent()) return;

    const title = document.querySelector(".topbar h1");
    const subtitle = document.querySelector(".topbar p");
    const heading = document.querySelector(".page-header h2");
    const description = document.querySelector(".page-header p");
    const listHeading = document.querySelector(".booking-list-header h3");
    const listDescription = document.querySelector(".booking-list-header p");

    if (title) title.textContent = "My Bookings";

    if (subtitle) {
        subtitle.textContent =
            "Book campus facilities and view your reservations.";
    }

    if (heading) heading.textContent = "My Facility Bookings";

    if (description) {
        description.textContent =
            "Create a booking and track your booking status.";
    }

    if (listHeading) listHeading.textContent = "My Bookings";

    if (listDescription) {
        listDescription.textContent =
            "View the bookings you have made.";
    }

    const userLabel = document.querySelector(
        'label[for="userId"]'
    );

    if (userLabel) {
        userLabel.textContent = "Booking For";
    }
}

/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {
    $("newBookingBtn")?.addEventListener(
        "click", openBookingForm
    );

    $("closeFormBtn")?.addEventListener(
        "click", closeBookingForm
    );

    $("cancelBookingBtn")?.addEventListener(
        "click", closeBookingForm
    );

    $("bookingForm")?.addEventListener(
        "submit", handleBookingSubmit
    );

    $("facilityId")?.addEventListener(
        "change", loadAvailableTimeSlots
    );

    $("bookingDate")?.addEventListener(
        "change", loadAvailableTimeSlots
    );

    $("purpose")?.addEventListener(
        "input", updatePurposeCounter
    );

    ["bookingSearch", "statusFilter", "facilityFilter"]
        .forEach((id) => {
            $(id)?.addEventListener("input", () => {
                currentPageNumber = 1;
                renderBookings();
            });
        });

    $("refreshBtn")?.addEventListener("click", async () => {
        await loadBookings();
        showNotification("Bookings refreshed.", "success");
    });

    $("previousPage")?.addEventListener("click", () => {
        if (currentPageNumber > 1) {
            currentPageNumber--;
            renderBookings();
        }
    });

    $("nextPage")?.addEventListener("click", () => {
        const pages = Math.ceil(
            getFilteredBookings().length / bookingsPerPage
        );

        if (currentPageNumber < pages) {
            currentPageNumber++;
            renderBookings();
        }
    });

    $("closeViewModalBtn")?.addEventListener(
        "click", closeViewModal
    );

    $("viewBookingModal")?.addEventListener("click", (event) => {
        if (event.target === $("viewBookingModal")) {
            closeViewModal();
        }
    });

    $("mobileMenuBtn")?.addEventListener("click", () => {
        document.querySelector(".sidebar")
            ?.classList.toggle("open");
    });
}

/* =========================================================
   API HELPER
========================================================= */

async function fetchList(url) {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(
            `Request failed: ${response.status} - ${url}`
        );
    }

    const data = await response.json();

    if (!Array.isArray(data)) {
        throw new Error(`Expected a list from ${url}`);
    }

    return data;
}

/* =========================================================
   LOAD USERS
========================================================= */

async function loadUsers() {
    if (isStudent()) {
        const user = getCurrentUser();

        users = user?.userId ? [{
            userId: user.userId,
            firstName: user.firstName || "",
            lastName: user.lastName || ""
        }] : [];

        populateUserDropdown();

        if ($("userId")) {
            $("userId").disabled = true;
        }

        return;
    }

    try {
        users = await fetchList(`${USER_API}/all`);
        populateUserDropdown();
    } catch (error) {
        console.error("Unable to load users:", error);
        showNotification("Unable to load users.", "error");
    }
}

function populateUserDropdown() {
    const select = $("userId");
    if (!select) return;

    select.innerHTML =
        '<option value="">Select a user</option>';

    users.forEach((user) => {
        const option = document.createElement("option");

        option.value = user.userId;
        option.textContent =
            `${user.firstName || ""} ${user.lastName || ""} (${user.userId})`;

        select.appendChild(option);
    });

    if (isStudent()) {
        select.value = currentUserId();
    }
}

/* =========================================================
   LOAD FACILITIES
========================================================= */

async function loadFacilities() {
    try {
        facilities = await fetchList(
            `${FACILITY_API}/all`
        );

        populateFacilityDropdowns();
    } catch (error) {
        console.error("Unable to load facilities:", error);
        showNotification("Unable to load facilities.", "error");
    }
}

function populateFacilityDropdowns() {
    const select = $("facilityId");
    const filter = $("facilityFilter");

    if (select) {
        select.innerHTML =
            '<option value="">Select a facility</option>';
    }

    if (filter) {
        filter.innerHTML =
            '<option value="all">All Facilities</option>';
    }

    facilities.forEach((facility) => {
        const name = facility.name ||
            facility.facilityName ||
            facility.facilityId;

        if (select) {
            const option = document.createElement("option");

            option.value = facility.facilityId;
            option.textContent =
                `${name} (${facility.location || "Campus"})`;

            select.appendChild(option);
        }

        if (filter) {
            const option = document.createElement("option");

            option.value = facility.facilityId;
            option.textContent = name;

            filter.appendChild(option);
        }
    });
}

/* =========================================================
   LOAD TIME SLOTS
========================================================= */

async function loadTimeSlotsData() {
    try {
        timeSlots = await fetchList(
            `${TIME_SLOT_API}/all`
        );
    } catch (error) {
        console.error("Unable to load time slots:", error);
        timeSlots = [];
    }
}

function normaliseDate(value) {
    if (!value) return "";

    return String(value).substring(0, 10);
}

async function loadAvailableTimeSlots() {
    const select = $("timeSlotId");
    if (!select) return;

    const date = $("bookingDate")?.value;
    const facilityId = $("facilityId")?.value;

    select.innerHTML =
        '<option value="">Select a time slot</option>';

    if (!date || !facilityId) {
        select.innerHTML =
            '<option value="">Select a facility and date first</option>';
        return;
    }

    if (!timeSlots.length) {
        await loadTimeSlotsData();
    }

    const available = timeSlots.filter((slot) => {
        return normaliseDate(slot.date) === date;
    });

    if (!available.length) {
        select.innerHTML =
            '<option value="">No time slots for this date</option>';
        return;
    }

    available.forEach((slot) => {
        const option = document.createElement("option");

        option.value = slot.timeSlotId;
        option.textContent =
            `${formatTime(slot.startTime)} - ${formatTime(slot.endTime)}`;

        select.appendChild(option);
    });
}

/* =========================================================
   LOAD BOOKINGS
========================================================= */

async function loadBookings() {
    try {
        const allBookings = await fetchList(
            `${BOOKING_API}/all`
        );

        if (isStudent()) {
            const id = currentUserId();

            bookings = allBookings.filter((booking) => {
                return String(booking.userId) === id;
            });
        } else {
            bookings = allBookings;
        }

        currentPageNumber = 1;
        updateStatistics();
        renderBookings();
    } catch (error) {
        console.error("Unable to load bookings:", error);

        bookings = [];
        updateStatistics();
        renderBookings();

        showNotification("Unable to load bookings.", "error");
    }
}

/* =========================================================
   OPEN / CLOSE BOOKING FORM
========================================================= */

function openBookingForm() {
    editingBookingId = null;

    $("bookingForm")?.reset();

    if (isStudent() && $("userId")) {
        $("userId").value = currentUserId();
    }

    if ($("formTitle")) {
        $("formTitle").textContent = "Create a Booking";
    }

    if ($("submitBookingBtn")) {
        $("submitBookingBtn").innerHTML =
            '<i class="fa-solid fa-calendar-check"></i> Create Booking';
    }

    updatePurposeCounter();

    $("bookingFormSection")?.classList.remove("hidden");

    $("bookingFormSection")?.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

function closeBookingForm() {
    $("bookingFormSection")?.classList.add("hidden");
    $("bookingForm")?.reset();

    editingBookingId = null;

    if (isStudent() && $("userId")) {
        $("userId").value = currentUserId();
    }

    updatePurposeCounter();
}

/* =========================================================
   PRESELECT FACILITY FROM FACILITIES PAGE
========================================================= */

async function handleFacilityPreselection() {
    const params = new URLSearchParams(
        window.location.search
    );

    const facilityId = params.get("facilityId");

    if (!facilityId) return;

    const select = $("facilityId");

    if (!select) return;

    const exists = facilities.some(
        (facility) =>
            String(facility.facilityId) === facilityId
    );

    if (!exists) return;

    openBookingForm();
    select.value = facilityId;

    await loadAvailableTimeSlots();
}

/* =========================================================
   CREATE / UPDATE BOOKING
========================================================= */

async function handleBookingSubmit(event) {
    event.preventDefault();

    const userId = isStudent()
        ? currentUserId()
        : $("userId")?.value;

    const facilityId = $("facilityId")?.value;
    const timeSlotId = $("timeSlotId")?.value;
    const purpose = $("purpose")?.value.trim();

    if (!userId || !facilityId || !timeSlotId || !purpose) {
        showNotification(
            "Please complete all booking fields.",
            "error"
        );
        return;
    }

    if (isStudent() && editingBookingId) {
        showNotification(
            "Students cannot edit existing bookings.",
            "error"
        );
        return;
    }

    const existing = bookings.find(
        (booking) => booking.bookingId === editingBookingId
    );

    const bookingData = {
        bookingId: editingBookingId || generateBookingId(),
        facilityId,
        timeSlotId,
        userId,
        purpose,
        bookingStatus: existing?.bookingStatus || "PENDING"
    };

    const updating = Boolean(editingBookingId);

    const url = updating
        ? `${BOOKING_API}/update`
        : `${BOOKING_API}/create`;

    const submitButton = $("submitBookingBtn");

    try {
        submitButton.disabled = true;
        submitButton.textContent = "Saving...";

        const response = await fetch(url, {
            method: updating ? "PUT" : "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bookingData)
        });

        if (!response.ok) {
            const details = await response.text();

            throw new Error(
                details || `Server error ${response.status}`
            );
        }

        showNotification(
            updating
                ? "Booking updated successfully."
                : "Booking created successfully.",
            "success"
        );

        closeBookingForm();
        await loadBookings();
    } catch (error) {
        console.error("Unable to save booking:", error);

        showNotification(
            "Unable to save booking. Check the backend.",
            "error"
        );
    } finally {
        submitButton.disabled = false;
        submitButton.innerHTML = updating
            ? '<i class="fa-solid fa-floppy-disk"></i> Update Booking'
            : '<i class="fa-solid fa-calendar-check"></i> Create Booking';
    }
}

function generateBookingId() {
    const random = Math.floor(
        10000 + Math.random() * 90000
    );

    return `B${random}`;
}

/* =========================================================
   FILTER AND DISPLAY BOOKINGS
========================================================= */

function getFilteredBookings() {
    const search = (
        $("bookingSearch")?.value || ""
    ).trim().toLowerCase();

    const status = $("statusFilter")?.value || "all";
    const facility = $("facilityFilter")?.value || "all";

    return bookings.filter((booking) => {
        const facilityName =
            findFacility(booking.facilityId)?.name || "";

        const searchText = [
            booking.bookingId,
            booking.userId,
            booking.facilityId,
            facilityName,
            booking.purpose
        ].join(" ").toLowerCase();

        const matchesSearch =
            !search || searchText.includes(search);

        const matchesStatus =
            status === "all" ||
            getStatusName(booking.bookingStatus) === status;

        const matchesFacility =
            facility === "all" ||
            String(booking.facilityId) === facility;

        return matchesSearch &&
            matchesStatus &&
            matchesFacility;
    });
}

function renderBookings() {
    const tbody = $("bookingTableBody");
    const emptyState = $("emptyState");

    if (!tbody) return;

    const filtered = getFilteredBookings();
    const totalPages = Math.ceil(
        filtered.length / bookingsPerPage
    );

    currentPageNumber = Math.max(
        1,
        Math.min(currentPageNumber, totalPages || 1)
    );

    const start = (
        currentPageNumber - 1
    ) * bookingsPerPage;

    const pageBookings = filtered.slice(
        start,
        start + bookingsPerPage
    );

    tbody.innerHTML = "";

    pageBookings.forEach((booking) => {
        tbody.appendChild(createBookingRow(booking));
    });

    if (emptyState) {
        emptyState.style.display =
            filtered.length ? "none" : "block";
    }

    if ($("bookingCount")) {
        $("bookingCount").textContent =
            `Showing ${filtered.length} booking${filtered.length === 1 ? "" : "s"}`;
    }

    if ($("currentPage")) {
        $("currentPage").textContent = currentPageNumber;
    }

    if ($("previousPage")) {
        $("previousPage").disabled =
            currentPageNumber <= 1;
    }

    if ($("nextPage")) {
        $("nextPage").disabled =
            currentPageNumber >= totalPages;
    }
}

function createBookingRow(booking) {
    const row = document.createElement("tr");

    const user = findUser(booking.userId);
    const facility = findFacility(booking.facilityId);
    const slot = findTimeSlot(booking.timeSlotId);

    const status = getStatusName(
        booking.bookingStatus
    );

    const userName = user
        ? `${user.firstName || ""} ${user.lastName || ""}`.trim()
        : booking.userId;

    const facilityName = facility
        ? facility.name || facility.facilityName
        : booking.facilityId;

    const actions = document.createElement("div");
    actions.className = "table-actions";

    actions.appendChild(
        createActionButton(
            "fa-regular fa-eye",
            "View booking",
            () => viewBooking(booking.bookingId)
        )
    );

    // Only staff receive Edit and Delete controls.
    if (canManageBookings()) {
        actions.appendChild(
            createActionButton(
                "fa-solid fa-pen",
                "Edit booking",
                () => editBooking(booking.bookingId)
            )
        );

        actions.appendChild(
            createActionButton(
                "fa-solid fa-trash",
                "Delete booking",
                () => deleteBooking(booking.bookingId),
                "delete"
            )
        );
    }

    row.innerHTML = `
        <td>
            <span class="booking-id">
                ${escapeHTML(booking.bookingId)}
            </span>
        </td>
        <td>${escapeHTML(userName)}</td>
        <td>${escapeHTML(facilityName)}</td>
        <td>${escapeHTML(slot?.date || "-")}</td>
        <td>
            ${slot
        ? `${formatTime(slot.startTime)} - ${formatTime(slot.endTime)}`
        : escapeHTML(booking.timeSlotId)}
        </td>
        <td>
            <span class="status-badge ${status}">
                ${escapeHTML(status)}
            </span>
        </td>
        <td></td>
    `;

    row.lastElementChild.appendChild(actions);

    return row;
}

function createActionButton(icon, title, callback, extraClass = "") {
    const button = document.createElement("button");

    button.type = "button";
    button.className = `table-action ${extraClass}`.trim();
    button.title = title;
    button.setAttribute("aria-label", title);

    const iconElement = document.createElement("i");
    iconElement.className = icon;

    button.appendChild(iconElement);
    button.addEventListener("click", callback);

    return button;
}

/* =========================================================
   VIEW BOOKING
========================================================= */

function viewBooking(bookingId) {
    const booking = bookings.find(
        (item) => item.bookingId === bookingId
    );

    if (!booking) return;

    const user = findUser(booking.userId);
    const facility = findFacility(booking.facilityId);
    const slot = findTimeSlot(booking.timeSlotId);

    $("viewBookingId").textContent = booking.bookingId;

    $("viewUser").textContent = user
        ? `${user.firstName || ""} ${user.lastName || ""}`.trim()
        : booking.userId;

    $("viewFacility").textContent = facility
        ? facility.name || facility.facilityName
        : booking.facilityId;

    $("viewDate").textContent = slot?.date || "-";

    $("viewTime").textContent = slot
        ? `${formatTime(slot.startTime)} - ${formatTime(slot.endTime)}`
        : "-";

    $("viewStatus").textContent =
        getStatusName(booking.bookingStatus);

    $("viewPurpose").textContent =
        booking.purpose || "-";

    $("viewBookingModal")?.classList.add("active");
}

function closeViewModal() {
    $("viewBookingModal")?.classList.remove("active");
}

/* =========================================================
   EDIT BOOKING - STAFF ONLY
========================================================= */

async function editBooking(bookingId) {
    if (!canManageBookings()) {
        showNotification(
            "Students cannot edit bookings.",
            "error"
        );
        return;
    }

    const booking = bookings.find(
        (item) => item.bookingId === bookingId
    );

    if (!booking) return;

    editingBookingId = booking.bookingId;

    $("userId").value = booking.userId;
    $("facilityId").value = booking.facilityId;

    const slot = findTimeSlot(booking.timeSlotId);

    $("bookingDate").value = normaliseDate(
        slot?.date
    );

    $("purpose").value = booking.purpose || "";

    updatePurposeCounter();
    await loadAvailableTimeSlots();

    $("timeSlotId").value = booking.timeSlotId;

    $("formTitle").textContent = "Update Booking";

    $("submitBookingBtn").innerHTML =
        '<i class="fa-solid fa-floppy-disk"></i> Update Booking';

    $("bookingFormSection").classList.remove("hidden");

    $("bookingFormSection").scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

/* =========================================================
   DELETE BOOKING - STAFF ONLY
========================================================= */

async function deleteBooking(bookingId) {
    if (!canManageBookings()) {
        showNotification(
            "Students cannot delete bookings.",
            "error"
        );
        return;
    }

    const confirmed = confirm(
        `Are you sure you want to delete booking ${bookingId}?`
    );

    if (!confirmed) return;

    try {
        const response = await fetch(
            `${BOOKING_API}/delete/${encodeURIComponent(bookingId)}`,
            { method: "DELETE" }
        );

        if (!response.ok) {
            throw new Error(
                `Delete failed: ${response.status}`
            );
        }

        const result = await response.text();

        if (result.trim().toLowerCase() === "false") {
            throw new Error("Backend did not delete the booking.");
        }

        showNotification(
            "Booking deleted successfully.",
            "success"
        );

        await loadBookings();
    } catch (error) {
        console.error("Unable to delete booking:", error);

        showNotification(
            "Unable to delete booking.",
            "error"
        );
    }
}

/* =========================================================
   STATISTICS
========================================================= */

function updateStatistics() {
    const countStatus = (status) =>
        bookings.filter(
            (booking) =>
                getStatusName(booking.bookingStatus) === status
        ).length;

    if ($("totalBookings")) {
        $("totalBookings").textContent = bookings.length;
    }

    if ($("approvedBookings")) {
        $("approvedBookings").textContent =
            countStatus("approved");
    }

    if ($("pendingBookings")) {
        $("pendingBookings").textContent =
            countStatus("pending");
    }

    if ($("cancelledBookings")) {
        $("cancelledBookings").textContent =
            countStatus("cancelled");
    }
}

/* =========================================================
   HELPERS
========================================================= */

function findUser(userId) {
    return users.find(
        (user) =>
            String(user.userId) === String(userId)
    );
}

function findFacility(facilityId) {
    return facilities.find(
        (facility) =>
            String(facility.facilityId) === String(facilityId)
    );
}

function findTimeSlot(timeSlotId) {
    return timeSlots.find(
        (slot) =>
            String(slot.timeSlotId) === String(timeSlotId)
    );
}

function getStatusName(status) {
    return String(status || "PENDING").toLowerCase();
}

function formatTime(time) {
    if (!time) return "-";
    return String(time).substring(0, 5);
}

function updatePurposeCounter() {
    if ($("purposeCount") && $("purpose")) {
        $("purposeCount").textContent =
            `${$("purpose").value.length} / 500`;
    }
}

function setMinimumDate() {
    const input = $("bookingDate");
    if (!input) return;

    const today = new Date();

    const year = today.getFullYear();
    const month = String(
        today.getMonth() + 1
    ).padStart(2, "0");

    const day = String(
        today.getDate()
    ).padStart(2, "0");

    input.min = `${year}-${month}-${day}`;
}

function escapeHTML(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

/* =========================================================
   SUCCESS / ERROR MESSAGE
========================================================= */

function showNotification(message, type = "success") {
    document.querySelector(".booking-notification")?.remove();

    const notification = document.createElement("div");

    notification.className =
        `booking-notification ${type}`;

    notification.style.cssText = `
        position: fixed;
        top: 25px;
        right: 25px;
        z-index: 9999;
        padding: 14px 20px;
        border-radius: 10px;
        background: white;
        color: ${type === "success" ? "#16a34a" : "#dc2626"};
        box-shadow: 0 10px 30px rgba(0,0,0,0.12);
        border: 1px solid #e5e7eb;
        font-size: 14px;
        font-weight: 600;
    `;

    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 3500);
}
