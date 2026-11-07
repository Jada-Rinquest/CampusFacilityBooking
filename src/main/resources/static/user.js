
/* =========================================================
   CAMPUS FACILITY BOOKING
   USER MANAGEMENT JAVASCRIPT
========================================================= */

const API_BASE_URL = "http://localhost:8080";
const USER_API = `${API_BASE_URL}/user`;
const BOOKING_API = `${API_BASE_URL}/booking`;

/* =========================================================
   EXISTING FORM ELEMENTS
========================================================= */

const userForm = document.getElementById("userForm");
const userIdInput = document.getElementById("userId");
const firstNameInput = document.getElementById("firstName");
const lastNameInput = document.getElementById("lastName");
const emailInput = document.getElementById("email");
const dateOfBirthInput = document.getElementById("dateOfBirth");
const departmentIdInput = document.getElementById("departmentId");

const createButton = document.getElementById("createButton");
const readButton = document.getElementById("readButton");
const updateButton = document.getElementById("updateButton");
const deleteButton = document.getElementById("deleteButton");
const clearButton = document.getElementById("clearButton");

const userMessage = document.getElementById("userMessage");
const userResult = document.getElementById("userResult");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");

/* =========================================================
   NEW TABLE ELEMENTS
========================================================= */

const totalUsersElement = document.getElementById("totalUsers");
const usersWithBookingsElement =
    document.getElementById("usersWithBookings");
const totalBookingsElement =
    document.getElementById("totalUserBookings");

const userTableSearch =
    document.getElementById("userTableSearch");
const bookingFilter =
    document.getElementById("bookingFilter");
const refreshUsersBtn =
    document.getElementById("refreshUsersBtn");

const registeredUsersBody =
    document.getElementById("registeredUsersBody");
const usersTableMessage =
    document.getElementById("usersTableMessage");

const userBookingsCard =
    document.getElementById("userBookingsCard");
const selectedUserBookingTitle =
    document.getElementById("selectedUserBookingTitle");
const selectedUserBookingSubtitle =
    document.getElementById("selectedUserBookingSubtitle");
const selectedUserBookingsBody =
    document.getElementById("selectedUserBookingsBody");
const closeUserBookingsBtn =
    document.getElementById("closeUserBookingsBtn");

/* =========================================================
   STORED PAGE DATA
========================================================= */

let registeredUsers = [];
let allBookings = [];
let bookingsLoaded = false;
let selectedUserId = null;

/* =========================================================
   PAGE INITIALIZATION
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    if (userForm) {
        userForm.addEventListener("submit", createUser);
    }

    readButton?.addEventListener("click", readUser);
    updateButton?.addEventListener("click", updateUser);
    deleteButton?.addEventListener("click", deleteUser);
    clearButton?.addEventListener("click", clearForm);

    mobileMenuBtn?.addEventListener("click", () => {
        sidebar?.classList.toggle("open");
    });

    userTableSearch?.addEventListener("input", renderUsersTable);
    bookingFilter?.addEventListener("change", renderUsersTable);

    refreshUsersBtn?.addEventListener("click", loadUsersAndBookings);

    closeUserBookingsBtn?.addEventListener("click", () => {
        userBookingsCard.hidden = true;
        selectedUserId = null;
    });

    registeredUsersBody?.addEventListener("click", event => {
        const button = event.target.closest("[data-view-user]");

        if (!button) return;

        const id = button.dataset.viewUser;
        viewUserBookings(id);
    });

    showEmptyResult();
    loadUsersAndBookings();
});

/* =========================================================
   COMMON HELPERS
========================================================= */

function buildPayload() {
    return {
        userId: userIdInput.value.trim(),
        firstName: firstNameInput.value.trim(),
        lastName: lastNameInput.value.trim(),
        email: emailInput.value.trim(),
        dateOfBirth: dateOfBirthInput.value,
        departmentId: departmentIdInput.value.trim()
    };
}

function fillForm(user) {
    userIdInput.value = user.userId || "";
    firstNameInput.value = user.firstName || "";
    lastNameInput.value = user.lastName || "";
    emailInput.value = user.email || "";
    dateOfBirthInput.value = user.dateOfBirth || "";
    departmentIdInput.value = user.departmentId || "";
}

function showMessage(message, type = "") {
    if (!userMessage) return;

    userMessage.textContent = message;
    userMessage.className = message
        ? `message show ${type}`
        : "message";
}

function showEmptyResult() {
    if (!userResult) return;

    userResult.replaceChildren();

    const container = document.createElement("div");
    container.className = "empty-result";

    const icon = document.createElement("i");
    icon.className = "fa-solid fa-user";

    const title = document.createElement("h4");
    title.textContent = "No User Selected";

    const description = document.createElement("p");
    description.textContent =
        "Search for a user using their User ID to display their information here.";

    container.append(icon, title, description);
    userResult.appendChild(container);
}

function addDetail(container, label, value) {
    const item = document.createElement("div");
    item.className = "user-detail";

    const labelElement = document.createElement("span");
    labelElement.textContent = label;

    const valueElement = document.createElement("strong");
    valueElement.textContent = value || "—";

    item.append(labelElement, valueElement);
    container.appendChild(item);
}

function showResult(user) {
    if (!userResult) return;

    userResult.replaceChildren();

    const details = document.createElement("div");
    details.className = "user-details";

    addDetail(details, "User ID", user.userId);
    addDetail(details, "First Name", user.firstName);
    addDetail(details, "Last Name", user.lastName);
    addDetail(details, "Email Address", user.email);
    addDetail(details, "Date of Birth", user.dateOfBirth);
    addDetail(details, "Department ID", user.departmentId);

    userResult.appendChild(details);
}

async function readError(response) {
    let message = `Request failed (${response.status})`;

    try {
        const body = await response.json();

        if (body.detail || body.message) {
            message = body.detail || body.message;
        }
    } catch (error) {
        // Keep the HTTP status message.
    }

    return new Error(message);
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, options);

    if (!response.ok) {
        throw await readError(response);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function jsonOptions(method, payload) {
    return {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    };
}

function validUserPayload(payload) {
    if (
        !payload.userId ||
        !payload.firstName ||
        !payload.lastName ||
        !payload.email ||
        !payload.dateOfBirth ||
        !payload.departmentId
    ) {
        showMessage("Please complete all user fields.", "error");
        return false;
    }

    const email = payload.email.toLowerCase();

    if (
        !email.endsWith("@cput.ac.za") &&
        !email.endsWith("@mycput.ac.za")
    ) {
        showMessage(
            "Please use a valid CPUT student or staff email address.",
            "error"
        );
        return false;
    }

    return true;
}

/* =========================================================
   CREATE USER
========================================================= */

async function createUser(event) {
    event?.preventDefault();

    const payload = buildPayload();

    if (!validUserPayload(payload)) return;

    try {
        createButton.disabled = true;

        const user = await requestJson(
            `${USER_API}/create`,
            jsonOptions("POST", payload)
        );

        showMessage("User created successfully.", "success");
        showResult(user);

        await loadUsersAndBookings();

    } catch (error) {
        console.error("Error creating user:", error);
        showMessage(error.message || "Could not create user.", "error");

    } finally {
        createButton.disabled = false;
    }
}

/* =========================================================
   READ USER
========================================================= */

async function readUser() {
    const id = userIdInput.value.trim();

    if (!id) {
        showMessage("Enter a User ID to search.", "error");
        return;
    }

    try {
        readButton.disabled = true;

        const user = await requestJson(
            `${USER_API}/read/${encodeURIComponent(id)}`
        );

        fillForm(user);
        showResult(user);
        showMessage("User loaded successfully.", "success");

    } catch (error) {
        console.error("Error reading user:", error);

        showMessage(
            "Could not load user: " + error.message,
            "error"
        );

    } finally {
        readButton.disabled = false;
    }
}

/* =========================================================
   UPDATE USER
========================================================= */

async function updateUser() {
    const payload = buildPayload();

    if (!validUserPayload(payload)) return;

    try {
        updateButton.disabled = true;

        const user = await requestJson(
            `${USER_API}/update`,
            jsonOptions("PUT", payload)
        );

        showResult(user);
        showMessage("User updated successfully.", "success");

        await loadUsersAndBookings();

    } catch (error) {
        console.error("Error updating user:", error);

        showMessage(
            "Could not update user: " + error.message,
            "error"
        );

    } finally {
        updateButton.disabled = false;
    }
}

/* =========================================================
   DELETE USER
========================================================= */

async function deleteUser() {
    const id = userIdInput.value.trim();

    if (!id) {
        showMessage("Enter a User ID to delete.", "error");
        return;
    }

    if (!confirm(`Are you sure you want to delete user ${id}?`)) {
        return;
    }

    try {
        deleteButton.disabled = true;

        const deleted = await requestJson(
            `${USER_API}/delete/${encodeURIComponent(id)}`,
            { method: "DELETE" }
        );

        if (deleted === false) {
            throw new Error("The backend did not delete this user.");
        }

        clearForm();
        showMessage("User deleted successfully.", "success");

        await loadUsersAndBookings();

    } catch (error) {
        console.error("Error deleting user:", error);

        showMessage(
            "Could not delete user: " + error.message,
            "error"
        );

    } finally {
        deleteButton.disabled = false;
    }
}

/* =========================================================
   CLEAR FORM
========================================================= */

function clearForm() {
    userForm?.reset();
    showEmptyResult();
    showMessage("", "");
}

/* =========================================================
   LOAD REGISTERED USERS AND BOOKINGS
========================================================= */

async function loadUsersAndBookings() {
    if (!registeredUsersBody) return;

    usersTableMessage.textContent = "Loading registered users...";
    refreshUsersBtn.disabled = true;

    const [usersResult, bookingsResult] = await Promise.allSettled([
        requestJson(`${USER_API}/all`),
        requestJson(`${BOOKING_API}/all`)
    ]);

    if (usersResult.status === "fulfilled" &&
        Array.isArray(usersResult.value)) {
        registeredUsers = usersResult.value;
    } else {
        registeredUsers = [];

        console.error(
            "Could not load users:",
            usersResult.status === "rejected"
                ? usersResult.reason
                : "Invalid response"
        );

        usersTableMessage.textContent =
            "Could not load users. Check that Spring Boot is running and the /user/all endpoint is accessible.";

        registeredUsersBody.replaceChildren();

        totalUsersElement.textContent = "—";
        usersWithBookingsElement.textContent = "—";
        totalBookingsElement.textContent = "—";

        refreshUsersBtn.disabled = false;
        return;
    }

    if (bookingsResult.status === "fulfilled" &&
        Array.isArray(bookingsResult.value)) {
        allBookings = bookingsResult.value;
        bookingsLoaded = true;
    } else {
        allBookings = [];
        bookingsLoaded = false;

        console.error(
            "Could not load bookings:",
            bookingsResult.status === "rejected"
                ? bookingsResult.reason
                : "Invalid response"
        );
    }

    updateStatistics();
    renderUsersTable();

    if (selectedUserId && !userBookingsCard.hidden) {
        viewUserBookings(selectedUserId, false);
    }

    refreshUsersBtn.disabled = false;
}

/* =========================================================
   MATCH BOOKINGS TO USERS
========================================================= */

function getUserBookings(userId) {
    if (!bookingsLoaded) return [];

    return allBookings.filter(booking =>
        String(booking.userId || "").trim() ===
        String(userId || "").trim()
    );
}

/* =========================================================
   UPDATE STATISTICS
========================================================= */

function updateStatistics() {
    totalUsersElement.textContent = registeredUsers.length;

    if (!bookingsLoaded) {
        usersWithBookingsElement.textContent = "—";
        totalBookingsElement.textContent = "—";
        return;
    }

    const usersWithBookings = registeredUsers.filter(
        user => getUserBookings(user.userId).length > 0
    ).length;

    usersWithBookingsElement.textContent = usersWithBookings;
    totalBookingsElement.textContent = allBookings.length;
}

/* =========================================================
   TABLE HELPER
========================================================= */

function createCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = value == null || value === ""
        ? "—"
        : String(value);

    row.appendChild(cell);
    return cell;
}

/* =========================================================
   DISPLAY USERS TABLE
========================================================= */

function renderUsersTable() {
    registeredUsersBody.replaceChildren();

    const search = (userTableSearch?.value || "")
        .trim()
        .toLowerCase();

    const filter = bookingFilter?.value || "all";

    const filteredUsers = registeredUsers.filter(user => {
        const searchableText = [
            user.userId,
            user.firstName,
            user.lastName,
            user.email
        ].join(" ").toLowerCase();

        const matchesSearch = searchableText.includes(search);

        const bookingCount = getUserBookings(user.userId).length;

        let matchesFilter = true;

        if (bookingsLoaded && filter === "booked") {
            matchesFilter = bookingCount > 0;
        }

        if (bookingsLoaded && filter === "not-booked") {
            matchesFilter = bookingCount === 0;
        }

        return matchesSearch && matchesFilter;
    });

    if (!bookingsLoaded) {
        usersTableMessage.textContent =
            "Users loaded, but booking records could not be retrieved. Booking counts are unavailable.";
    } else {
        usersTableMessage.textContent =
            `${filteredUsers.length} of ${registeredUsers.length} registered users displayed.`;
    }

    if (filteredUsers.length === 0) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");

        cell.colSpan = 6;
        cell.className = "empty-table-cell";
        cell.textContent = registeredUsers.length === 0
            ? "No registered users found."
            : "No users match your search or filter.";

        row.appendChild(cell);
        registeredUsersBody.appendChild(row);
        return;
    }

    filteredUsers.forEach(user => {
        const row = document.createElement("tr");

        createCell(row, user.userId);

        const fullName =
            `${user.firstName || ""} ${user.lastName || ""}`.trim();

        createCell(row, fullName);
        createCell(row, user.email);
        createCell(row, user.departmentId);

        const countCell = document.createElement("td");

        if (bookingsLoaded) {
            const count = getUserBookings(user.userId).length;

            const badge = document.createElement("span");
            badge.className = count > 0
                ? "booking-count has-bookings"
                : "booking-count no-bookings";

            badge.textContent = count === 1
                ? "1 booking"
                : `${count} bookings`;

            countCell.appendChild(badge);
        } else {
            countCell.textContent = "Unavailable";
        }

        row.appendChild(countCell);

        const actionCell = document.createElement("td");

        const viewButton = document.createElement("button");
        viewButton.type = "button";
        viewButton.className = "table-view-btn";
        viewButton.dataset.viewUser = user.userId;
        viewButton.textContent = "View Bookings";
        viewButton.disabled = !bookingsLoaded;

        actionCell.appendChild(viewButton);
        row.appendChild(actionCell);

        registeredUsersBody.appendChild(row);
    });
}

/* =========================================================
   VIEW A USER'S BOOKINGS
========================================================= */

function viewUserBookings(userId, scrollToCard = true) {
    if (!bookingsLoaded) {
        showMessage(
            "Booking records are currently unavailable.",
            "error"
        );
        return;
    }

    const user = registeredUsers.find(
        item => String(item.userId) === String(userId)
    );

    if (!user) return;

    selectedUserId = userId;

    const fullName =
        `${user.firstName || ""} ${user.lastName || ""}`.trim();

    selectedUserBookingTitle.textContent =
        `${fullName || userId} - Booking History`;

    selectedUserBookingSubtitle.textContent =
        `User ID: ${userId}`;

    selectedUserBookingsBody.replaceChildren();

    const bookings = getUserBookings(userId);

    if (bookings.length === 0) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");

        cell.colSpan = 5;
        cell.className = "empty-table-cell";
        cell.textContent =
            "This user has not made any bookings yet.";

        row.appendChild(cell);
        selectedUserBookingsBody.appendChild(row);
    } else {
        bookings.forEach(booking => {
            const row = document.createElement("tr");

            createCell(row, booking.bookingId);
            createCell(row, booking.facilityId);
            createCell(row, booking.timeSlotId);
            createCell(row, booking.purpose);

            const statusCell = document.createElement("td");
            const status = booking.bookingStatus || "PENDING";

            const badge = document.createElement("span");
            badge.className =
                `booking-status ${String(status).toLowerCase()}`;
            badge.textContent = status;

            statusCell.appendChild(badge);
            row.appendChild(statusCell);

            selectedUserBookingsBody.appendChild(row);
        });
    }

    userBookingsCard.hidden = false;

    if (scrollToCard) {
        userBookingsCard.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    }
}
