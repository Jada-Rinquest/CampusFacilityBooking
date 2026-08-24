/* =========================================================
   BOOKING MANAGEMENT
========================================================= */

const API_BASE_URL = "http://localhost:8080/bookings";

let editingBookingId = null;


/* =========================================================
   DOM ELEMENTS
========================================================= */

const bookingForm = document.getElementById("bookingForm");

const bookingIdInput = document.getElementById("bookingId");
const facilityIdInput = document.getElementById("facilityId");
const timeSlotIdInput = document.getElementById("timeSlotId");
const userIdInput = document.getElementById("userId");
const purposeInput = document.getElementById("purpose");
const bookingStatusIdInput = document.getElementById("bookingStatusId");

const submitButton = document.getElementById("submitButton");
const clearButton = document.getElementById("clearButton");
const cancelEditButton = document.getElementById("cancelEditButton");

const searchBookingId = document.getElementById("searchBookingId");
const searchButton = document.getElementById("searchButton");

const bookingDetails = document.getElementById("bookingDetails");

const detailBookingId = document.getElementById("detailBookingId");
const detailFacilityId = document.getElementById("detailFacilityId");
const detailTimeSlotId = document.getElementById("detailTimeSlotId");
const detailUserId = document.getElementById("detailUserId");
const detailPurpose = document.getElementById("detailPurpose");
const detailBookingStatusId = document.getElementById("detailBookingStatusId");

const bookingTableBody = document.getElementById("bookingTableBody");
const emptyMessage = document.getElementById("emptyMessage");

const refreshButton = document.getElementById("refreshButton");

const statusMessage = document.getElementById("statusMessage");
const statusText = document.getElementById("statusText");

const formTitle = document.getElementById("formTitle");


/* =========================================================
   PAGE LOAD
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadBookings();

});


/* =========================================================
   CREATE / UPDATE BOOKING
========================================================= */

bookingForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const booking = {

        bookingId: bookingIdInput.value.trim(),

        facilityId: facilityIdInput.value.trim(),

        timeSlotId: timeSlotIdInput.value.trim(),

        userId: userIdInput.value.trim(),

        purpose: purposeInput.value.trim(),

        bookingStatusId: bookingStatusIdInput.value.trim()

    };


    if (!validateBooking(booking)) {
        return;
    }


    if (editingBookingId) {

        await updateBooking(booking);

    } else {

        await createBooking(booking);

    }

});


/* =========================================================
   VALIDATE BOOKING
========================================================= */

function validateBooking(booking) {

    if (!booking.bookingId) {
        showStatus("Booking ID is required.", "error");
        return false;
    }

    if (!booking.facilityId) {
        showStatus("Facility ID is required.", "error");
        return false;
    }

    if (!booking.timeSlotId) {
        showStatus("Time Slot ID is required.", "error");
        return false;
    }

    if (!booking.userId) {
        showStatus("User ID is required.", "error");
        return false;
    }

    if (!booking.purpose) {
        showStatus("Booking purpose is required.", "error");
        return false;
    }

    if (!booking.bookingStatusId) {
        showStatus("Booking Status ID is required.", "error");
        return false;
    }

    return true;
}


/* =========================================================
   CREATE BOOKING
========================================================= */

async function createBooking(booking) {

    try {

        setLoading(true);

        const response = await fetch(API_BASE_URL, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(booking)

        });


        if (!response.ok) {

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        showStatus("Booking created successfully.", "success");

        clearForm();

        await loadBookings();

    } catch (error) {

        console.error("Error creating booking:", error);

        showStatus(
            "Unable to create booking: " + error.message,
            "error"
        );

    } finally {

        setLoading(false);

    }

}


/* =========================================================
   GET ALL BOOKINGS
========================================================= */

async function loadBookings() {

    try {

        const response = await fetch(API_BASE_URL);


        if (!response.ok) {

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const bookings = await response.json();

        displayBookings(bookings);

    } catch (error) {

        console.error("Error loading bookings:", error);

        bookingTableBody.innerHTML = "";

        emptyMessage.classList.remove("hidden");

        emptyMessage.querySelector("p").textContent =
            "Unable to load bookings.";

        emptyMessage.querySelector("span").textContent =
            error.message;

    }

}


/* =========================================================
   DISPLAY ALL BOOKINGS
========================================================= */

function displayBookings(bookings) {

    bookingTableBody.innerHTML = "";


    if (!bookings || bookings.length === 0) {

        emptyMessage.classList.remove("hidden");

        emptyMessage.querySelector("p").textContent =
            "No bookings found.";

        emptyMessage.querySelector("span").textContent =
            "Create a booking using the form above.";

        return;
    }


    emptyMessage.classList.add("hidden");


    bookings.forEach(booking => {

        const row = document.createElement("tr");


        row.innerHTML = `

            <td>${escapeHtml(booking.bookingId)}</td>

            <td>${escapeHtml(booking.facilityId)}</td>

            <td>${escapeHtml(booking.timeSlotId)}</td>

            <td>${escapeHtml(booking.userId)}</td>

            <td>${escapeHtml(booking.purpose)}</td>

            <td>${escapeHtml(booking.bookingStatusId)}</td>

            <td>

                <button
                    type="button"
                    class="edit-button"
                    onclick="editBooking('${escapeAttribute(booking.bookingId)}')">
                    Edit
                </button>

                <button
                    type="button"
                    class="delete-button"
                    onclick="deleteBooking('${escapeAttribute(booking.bookingId)}')">
                    Delete
                </button>

            </td>

        `;


        bookingTableBody.appendChild(row);

    });

}


/* =========================================================
   FIND BOOKING
========================================================= */

searchButton.addEventListener("click", async () => {

    const id = searchBookingId.value.trim();


    if (!id) {

        showStatus(
            "Please enter a Booking ID.",
            "error"
        );

        return;
    }


    await findBooking(id);

});


/* =========================================================
   FIND BOOKING BY ID
========================================================= */

async function findBooking(id) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/${encodeURIComponent(id)}`
        );


        if (!response.ok) {

            if (response.status === 404) {

                throw new Error(
                    "Booking with ID " + id + " was not found."
                );

            }

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const booking = await response.json();

        displayBookingDetails(booking);

        showStatus(
            "Booking found successfully.",
            "success"
        );

    } catch (error) {

        console.error("Error finding booking:", error);

        bookingDetails.classList.add("hidden");

        showStatus(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   DISPLAY BOOKING DETAILS
========================================================= */

function displayBookingDetails(booking) {

    detailBookingId.textContent =
        booking.bookingId ?? "-";

    detailFacilityId.textContent =
        booking.facilityId ?? "-";

    detailTimeSlotId.textContent =
        booking.timeSlotId ?? "-";

    detailUserId.textContent =
        booking.userId ?? "-";

    detailPurpose.textContent =
        booking.purpose ?? "-";

    detailBookingStatusId.textContent =
        booking.bookingStatusId ?? "-";


    bookingDetails.classList.remove("hidden");

}


/* =========================================================
   EDIT BOOKING
========================================================= */

async function editBooking(id) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/${encodeURIComponent(id)}`
        );


        if (!response.ok) {

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const booking = await response.json();


        bookingIdInput.value =
            booking.bookingId ?? "";

        facilityIdInput.value =
            booking.facilityId ?? "";

        timeSlotIdInput.value =
            booking.timeSlotId ?? "";

        userIdInput.value =
            booking.userId ?? "";

        purposeInput.value =
            booking.purpose ?? "";

        bookingStatusIdInput.value =
            booking.bookingStatusId ?? "";


        editingBookingId = booking.bookingId;


        formTitle.textContent = "Update Booking";

        submitButton.textContent = "Update Booking";

        cancelEditButton.classList.remove("hidden");


        bookingForm.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });


        showStatus(
            "Editing booking " + booking.bookingId,
            "warning"
        );


    } catch (error) {

        console.error("Error loading booking:", error);

        showStatus(
            "Unable to load booking: " + error.message,
            "error"
        );

    }

}


/* =========================================================
   UPDATE BOOKING
========================================================= */

async function updateBooking(booking) {

    try {

        setLoading(true);


        const response = await fetch(
            `${API_BASE_URL}/${encodeURIComponent(editingBookingId)}`,
            {

                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(booking)

            }
        );


        if (!response.ok) {

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        showStatus(
            "Booking updated successfully.",
            "success"
        );


        clearForm();

        await loadBookings();


    } catch (error) {

        console.error("Error updating booking:", error);

        showStatus(
            "Unable to update booking: " + error.message,
            "error"
        );

    } finally {

        setLoading(false);

    }

}


/* =========================================================
   DELETE BOOKING
========================================================= */

async function deleteBooking(id) {

    const confirmed = confirm(
        `Are you sure you want to delete booking "${id}"?`
    );


    if (!confirmed) {
        return;
    }


    try {

        const response = await fetch(
            `${API_BASE_URL}/${encodeURIComponent(id)}`,
            {
                method: "DELETE"
            }
        );


        if (!response.ok) {

            const errorMessage = await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        showStatus(
            "Booking deleted successfully.",
            "success"
        );


        if (editingBookingId === id) {
            clearForm();
        }


        bookingDetails.classList.add("hidden");


        await loadBookings();


    } catch (error) {

        console.error("Error deleting booking:", error);

        showStatus(
            "Unable to delete booking: " + error.message,
            "error"
        );

    }

}


/* =========================================================
   CLEAR FORM
========================================================= */

clearButton.addEventListener("click", () => {

    clearForm();

});


function clearForm() {

    bookingForm.reset();

    editingBookingId = null;


    formTitle.textContent = "Create a Booking";

    submitButton.textContent = "Create Booking";

    cancelEditButton.classList.add("hidden");

}


/* =========================================================
   CANCEL EDIT
========================================================= */

cancelEditButton.addEventListener("click", () => {

    clearForm();

    showStatus(
        "Edit cancelled.",
        "warning"
    );

});


/* =========================================================
   REFRESH BOOKINGS
========================================================= */

refreshButton.addEventListener("click", async () => {

    await loadBookings();

    showStatus(
        "Bookings refreshed.",
        "success"
    );

});


/* =========================================================
   STATUS MESSAGE
========================================================= */

function showStatus(message, type) {

    statusText.textContent = message;

    statusMessage.className =
        `status-message ${type}`;

    statusMessage.classList.remove("hidden");


    setTimeout(() => {

        statusMessage.classList.add("hidden");

    }, 4000);

}


/* =========================================================
   LOADING STATE
========================================================= */

function setLoading(isLoading) {

    if (isLoading) {

        submitButton.disabled = true;

        submitButton.textContent =
            editingBookingId
                ? "Updating..."
                : "Creating...";

    } else {

        submitButton.disabled = false;

        submitButton.textContent =
            editingBookingId
                ? "Update Booking"
                : "Create Booking";

    }

}


/* =========================================================
   ERROR HANDLING
========================================================= */

async function getErrorMessage(response) {

    try {

        const data = await response.json();


        if (data.message) {
            return data.message;
        }

        if (data.error) {
            return data.error;
        }


        return `Request failed with status ${response.status}.`;

    } catch {

        return `Request failed with status ${response.status}.`;

    }

}


/* =========================================================
   SECURITY HELPERS
========================================================= */

function escapeHtml(value) {

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


function escapeAttribute(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/\\/g, "\\\\")
        .replace(/'/g, "\\'");

}