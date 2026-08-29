/* =========================================================
   CAMPUS FACILITY BOOKING
   BOOKING MANAGEMENT JAVASCRIPT
========================================================= */


/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const BOOKING_API = `${API_BASE_URL}/booking`;
const USER_API = `${API_BASE_URL}/user`;
const FACILITY_API = `${API_BASE_URL}/facility`;
const TIME_SLOT_API = `${API_BASE_URL}/time-slot`;


/* =========================================================
   DOM ELEMENTS
========================================================= */

// New booking
const newBookingBtn = document.getElementById("newBookingBtn");
const bookingFormSection = document.getElementById("bookingFormSection");
const closeFormBtn = document.getElementById("closeFormBtn");
const cancelBookingBtn = document.getElementById("cancelBookingBtn");

// Form
const bookingForm = document.getElementById("bookingForm");
const userSelect = document.getElementById("userId");
const facilitySelect = document.getElementById("facilityId");
const bookingDate = document.getElementById("bookingDate");
const timeSlotSelect = document.getElementById("timeSlotId");
const purposeInput = document.getElementById("purpose");
const purposeCount = document.getElementById("purposeCount");
const submitBookingBtn = document.getElementById("submitBookingBtn");

// Table
const bookingTableBody = document.getElementById("bookingTableBody");
const emptyState = document.getElementById("emptyState");

// Filters
const bookingSearch = document.getElementById("bookingSearch");
const statusFilter = document.getElementById("statusFilter");
const facilityFilter = document.getElementById("facilityFilter");

// Refresh
const refreshBtn = document.getElementById("refreshBtn");

// Statistics
const totalBookings = document.getElementById("totalBookings");
const approvedBookings = document.getElementById("approvedBookings");
const pendingBookings = document.getElementById("pendingBookings");
const cancelledBookings = document.getElementById("cancelledBookings");

// Table count
const bookingCount = document.getElementById("bookingCount");

// Pagination
const previousPage = document.getElementById("previousPage");
const nextPage = document.getElementById("nextPage");
const currentPage = document.getElementById("currentPage");

// View modal
const viewBookingModal = document.getElementById("viewBookingModal");
const closeViewModalBtn = document.getElementById("closeViewModalBtn");

const viewBookingId = document.getElementById("viewBookingId");
const viewUser = document.getElementById("viewUser");
const viewFacility = document.getElementById("viewFacility");
const viewDate = document.getElementById("viewDate");
const viewTime = document.getElementById("viewTime");
const viewStatus = document.getElementById("viewStatus");
const viewPurpose = document.getElementById("viewPurpose");

// Mobile menu
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


/* =========================================================
   APPLICATION STATE
========================================================= */

let bookings = [];
let users = [];
let facilities = [];
let timeSlots = [];

let editingBookingId = null;

let currentPageNumber = 1;

const bookingsPerPage = 8;


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
            loadBookings(),
            loadUsers(),
            loadFacilities()
        ]);

    } catch (error) {

        console.error("Error loading page data:", error);

        showNotification(
            "Unable to load booking data. Make sure the Spring Boot backend is running.",
            "error"
        );

    }

}


/* =========================================================
   EVENT LISTENERS
========================================================= */

function setupEventListeners() {

    /* New booking */

    if (newBookingBtn) {

        newBookingBtn.addEventListener("click", openBookingForm);

    }


    /* Close form */

    if (closeFormBtn) {

        closeFormBtn.addEventListener(
            "click",
            closeBookingForm
        );

    }


    /* Cancel form */

    if (cancelBookingBtn) {

        cancelBookingBtn.addEventListener(
            "click",
            closeBookingForm
        );

    }


    /* Booking form */

    if (bookingForm) {

        bookingForm.addEventListener(
            "submit",
            handleBookingSubmit
        );

    }


    /* Facility */

    if (facilitySelect) {

        facilitySelect.addEventListener(
            "change",
            handleFacilityChange
        );

    }


    /* Date */

    if (bookingDate) {

        bookingDate.addEventListener(
            "change",
            handleDateChange
        );

    }


    /* Purpose character counter */

    if (purposeInput) {

        purposeInput.addEventListener(
            "input",
            updatePurposeCounter
        );

    }


    /* Search */

    if (bookingSearch) {

        bookingSearch.addEventListener(
            "input",
            function () {

                currentPageNumber = 1;

                renderBookings();

            }
        );

    }


    /* Status filter */

    if (statusFilter) {

        statusFilter.addEventListener(
            "change",
            function () {

                currentPageNumber = 1;

                renderBookings();

            }
        );

    }


    /* Facility filter */

    if (facilityFilter) {

        facilityFilter.addEventListener(
            "change",
            function () {

                currentPageNumber = 1;

                renderBookings();

            }
        );

    }


    /* Refresh */

    if (refreshBtn) {

        refreshBtn.addEventListener(
            "click",
            async function () {

                await loadBookings();

                showNotification(
                    "Bookings refreshed successfully.",
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

                    renderBookings();

                }

            }
        );

    }


    if (nextPage) {

        nextPage.addEventListener(
            "click",
            function () {

                currentPageNumber++;

                renderBookings();

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

    if (viewBookingModal) {

        viewBookingModal.addEventListener(
            "click",
            function (event) {

                if (event.target === viewBookingModal) {

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
   OPEN BOOKING FORM
========================================================= */

function openBookingForm() {

    editingBookingId = null;

    bookingForm.reset();

    if (purposeCount) {

        purposeCount.textContent = "0 / 500";

    }

    document.getElementById("formTitle").textContent =
        "Create a Booking";

    submitBookingBtn.innerHTML =
        '<i class="fa-solid fa-calendar-check"></i> Create Booking';

    bookingFormSection.classList.remove("hidden");

    bookingFormSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });

}


/* =========================================================
   CLOSE BOOKING FORM
========================================================= */

function closeBookingForm() {

    bookingFormSection.classList.add("hidden");

    bookingForm.reset();

    editingBookingId = null;

    if (purposeCount) {

        purposeCount.textContent = "0 / 500";

    }

}


/* =========================================================
   LOAD BOOKINGS
========================================================= */

async function loadBookings() {

    try {

        /*
         * Change this URL if your controller uses
         * a different endpoint for findAll().
         */

        const response = await fetch(
            `${BOOKING_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Booking request failed: ${response.status}`
            );

        }

        bookings = await response.json();

        renderBookings();

        updateStatistics();

    } catch (error) {

        console.error(
            "Error loading bookings:",
            error
        );

        bookings = [];

        renderBookings();

    }

}


/* =========================================================
   LOAD USERS
========================================================= */

async function loadUsers() {

    try {

        /*
         * Change /all if your UserController
         * uses another findAll endpoint.
         */

        const response = await fetch(
            `${USER_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `User request failed: ${response.status}`
            );

        }

        users = await response.json();

        populateUserDropdown();

    } catch (error) {

        console.error(
            "Error loading users:",
            error
        );

    }

}


/* =========================================================
   LOAD FACILITIES
========================================================= */

async function loadFacilities() {

    try {

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

    }

}


/* =========================================================
   POPULATE USER DROPDOWN
========================================================= */

function populateUserDropdown() {

    userSelect.innerHTML =
        '<option value="">Select a user</option>';

    users.forEach(function (user) {

        const option = document.createElement("option");

        option.value = user.userId;

        option.textContent =
            `${user.firstName} ${user.lastName} (${user.userId})`;

        userSelect.appendChild(option);

    });

}


/* =========================================================
   POPULATE FACILITY DROPDOWNS
========================================================= */

function populateFacilityDropdowns() {

    facilitySelect.innerHTML =
        '<option value="">Select a facility</option>';

    facilityFilter.innerHTML =
        '<option value="all">All Facilities</option>';


    facilities.forEach(function (facility) {

        /* Form dropdown */

        const option = document.createElement("option");

        option.value = facility.facilityId;

        option.textContent =
            `${facility.name} (${facility.location})`;

        facilitySelect.appendChild(option);


        /* Filter dropdown */

        const filterOption =
            document.createElement("option");

        filterOption.value =
            facility.facilityId;

        filterOption.textContent =
            facility.name;

        facilityFilter.appendChild(filterOption);

    });

}


/* =========================================================
   FACILITY CHANGE
========================================================= */

async function handleFacilityChange() {

    const selectedFacility =
        facilitySelect.value;

    if (!selectedFacility) {

        timeSlotSelect.innerHTML =
            '<option value="">Select a time slot</option>';

        return;

    }

    await loadTimeSlots();

}


/* =========================================================
   DATE CHANGE
========================================================= */

async function handleDateChange() {

    if (!bookingDate.value) {

        timeSlotSelect.innerHTML =
            '<option value="">Select a time slot</option>';

        return;

    }

    await loadTimeSlots();

}


/* =========================================================
   LOAD TIME SLOTS
========================================================= */

async function loadTimeSlots() {

    const selectedDate =
        bookingDate.value;

    if (!selectedDate) {

        timeSlotSelect.innerHTML =
            '<option value="">Select a date first</option>';

        return;

    }


    try {

        /*
         * This assumes your TimeSlotController
         * has an endpoint such as:
         *
         * GET /time-slot/all
         *
         * The filtering is done here in JavaScript.
         */

        const response = await fetch(
            `${TIME_SLOT_API}/all`
        );

        if (!response.ok) {

            throw new Error(
                `Time slot request failed: ${response.status}`
            );

        }

        timeSlots = await response.json();


        const matchingSlots =
            timeSlots.filter(function (slot) {

                return slot.date === selectedDate;

            });


        populateTimeSlotDropdown(
            matchingSlots
        );


    } catch (error) {

        console.error(
            "Error loading time slots:",
            error
        );

        timeSlotSelect.innerHTML =
            '<option value="">Unable to load time slots</option>';

    }

}


/* =========================================================
   POPULATE TIME SLOT DROPDOWN
========================================================= */

function populateTimeSlotDropdown(slots) {

    timeSlotSelect.innerHTML =
        '<option value="">Select a time slot</option>';


    if (slots.length === 0) {

        timeSlotSelect.innerHTML =
            '<option value="">No time slots available</option>';

        return;

    }


    slots.forEach(function (slot) {

        const option =
            document.createElement("option");

        option.value =
            slot.timeSlotId;

        option.textContent =
            `${formatTime(slot.startTime)} - ${formatTime(slot.endTime)}`;

        timeSlotSelect.appendChild(option);

    });

}


/* =========================================================
   CREATE / UPDATE BOOKING
========================================================= */

async function handleBookingSubmit(event) {

    event.preventDefault();


    const userId =
        userSelect.value;

    const facilityId =
        facilitySelect.value;

    const timeSlotId =
        timeSlotSelect.value;

    const purpose =
        purposeInput.value.trim();


    /* Validation */

    if (!userId) {

        showNotification(
            "Please select a user.",
            "error"
        );

        return;

    }


    if (!facilityId) {

        showNotification(
            "Please select a facility.",
            "error"
        );

        return;

    }


    if (!bookingDate.value) {

        showNotification(
            "Please select a booking date.",
            "error"
        );

        return;

    }


    if (!timeSlotId) {

        showNotification(
            "Please select a time slot.",
            "error"
        );

        return;

    }


    if (!purpose) {

        showNotification(
            "Please enter the purpose of the booking.",
            "error"
        );

        return;

    }


    /*
     * When creating a booking, generate
     * a simple unique booking ID.
     */

    const bookingId =
        editingBookingId ||
        generateBookingId();


    /*
     * Default status.
     *
     * Change this value if your database
     * uses a different booking status ID.
     */

    const bookingStatusId =
        "BS001";


    const bookingData = {

        bookingId: bookingId,

        facilityId: facilityId,

        timeSlotId: timeSlotId,

        userId: userId,

        purpose: purpose,

        bookingStatusId: bookingStatusId

    };


    try {

        submitBookingBtn.disabled = true;

        submitBookingBtn.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';


        let response;


        /* UPDATE */

        if (editingBookingId) {

            response = await fetch(
                `${BOOKING_API}/update`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(bookingData)
                }
            );

        }

        /* CREATE */

        else {

            response = await fetch(
                `${BOOKING_API}/create`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(bookingData)
                }
            );

        }


        if (!response.ok) {

            const errorText =
                await response.text();

            throw new Error(
                errorText ||
                `Server error: ${response.status}`
            );

        }


        const savedBooking =
            await response.json();


        console.log(
            "Saved booking:",
            savedBooking
        );


        showNotification(
            editingBookingId
                ? "Booking updated successfully."
                : "Booking created successfully.",
            "success"
        );


        closeBookingForm();

        await loadBookings();


    } catch (error) {

        console.error(
            "Error saving booking:",
            error
        );

        showNotification(
            "Unable to save the booking.",
            "error"
        );


    } finally {

        submitBookingBtn.disabled = false;

        submitBookingBtn.innerHTML =
            editingBookingId
                ? '<i class="fa-solid fa-floppy-disk"></i> Update Booking'
                : '<i class="fa-solid fa-calendar-check"></i> Create Booking';

    }

}


/* =========================================================
   GENERATE BOOKING ID
========================================================= */

function generateBookingId() {

    const randomNumber =
        Math.floor(
            10000 +
            Math.random() * 90000
        );

    return `B${randomNumber}`;

}


/* =========================================================
   RENDER BOOKINGS
========================================================= */

function renderBookings() {

    const filteredBookings =
        getFilteredBookings();


    if (filteredBookings.length === 0) {

        bookingTableBody.innerHTML = "";

        emptyState.style.display = "block";

        updateBookingCount(0);

        updatePagination(
            0
        );

        return;

    }


    emptyState.style.display = "none";


    const totalPages =
        Math.ceil(
            filteredBookings.length /
            bookingsPerPage
        );


    if (
        currentPageNumber >
        totalPages
    ) {

        currentPageNumber =
            totalPages;

    }


    const startIndex =
        (currentPageNumber - 1) *
        bookingsPerPage;


    const endIndex =
        startIndex +
        bookingsPerPage;


    const pageBookings =
        filteredBookings.slice(
            startIndex,
            endIndex
        );


    bookingTableBody.innerHTML = "";


    pageBookings.forEach(
        function (booking) {

            const row =
                createBookingRow(
                    booking
                );

            bookingTableBody.appendChild(
                row
            );

        }
    );


    updateBookingCount(
        filteredBookings.length
    );

    updatePagination(
        filteredBookings.length
    );

}


/* =========================================================
   FILTER BOOKINGS
========================================================= */

function getFilteredBookings() {

    const search =
        bookingSearch.value
            .trim()
            .toLowerCase();


    const selectedStatus =
        statusFilter.value;


    const selectedFacility =
        facilityFilter.value;


    return bookings.filter(
        function (booking) {

            const searchMatch =

                !search ||

                String(
                    booking.bookingId
                )
                    .toLowerCase()
                    .includes(search)

                ||

                String(
                    booking.userId
                )
                    .toLowerCase()
                    .includes(search)

                ||

                String(
                    booking.facilityId
                )
                    .toLowerCase()
                    .includes(search)

                ||

                String(
                    booking.purpose
                )
                    .toLowerCase()
                    .includes(search);


            const statusMatch =

                selectedStatus === "all" ||

                getStatusName(
                    booking.bookingStatusId
                ).toLowerCase()
                === selectedStatus;


            const facilityMatch =

                selectedFacility === "all" ||

                booking.facilityId ===
                selectedFacility;


            return (
                searchMatch &&
                statusMatch &&
                facilityMatch
            );

        }
    );

}


/* =========================================================
   CREATE TABLE ROW
========================================================= */

function createBookingRow(booking) {

    const row =
        document.createElement("tr");


    const user =
        findUser(
            booking.userId
        );


    const facility =
        findFacility(
            booking.facilityId
        );


    const slot =
        findTimeSlot(
            booking.timeSlotId
        );


    const status =
        getStatusName(
            booking.bookingStatusId
        );


    row.innerHTML = `

        <td>

            <span class="booking-id">
                ${escapeHTML(booking.bookingId)}
            </span>

        </td>


        <td>

            ${
                user
                    ? `${escapeHTML(user.firstName)}
                       ${escapeHTML(user.lastName)}`
                    : escapeHTML(booking.userId)
            }

        </td>


        <td>

            ${
                facility
                    ? escapeHTML(facility.name)
                    : escapeHTML(booking.facilityId)
            }

        </td>


        <td>

            ${
                slot
                    ? escapeHTML(slot.date)
                    : "-"
            }

        </td>


        <td>

            ${
                slot
                    ? `${formatTime(slot.startTime)}
                       -
                       ${formatTime(slot.endTime)}`
                    : escapeHTML(booking.timeSlotId)
            }

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
                    title="View booking"
                    onclick="viewBooking('${booking.bookingId}')">

                    <i class="fa-regular fa-eye"></i>

                </button>


                <button
                    type="button"
                    class="table-action"
                    title="Edit booking"
                    onclick="editBooking('${booking.bookingId}')">

                    <i class="fa-solid fa-pen"></i>

                </button>


                <button
                    type="button"
                    class="table-action delete"
                    title="Delete booking"
                    onclick="deleteBooking('${booking.bookingId}')">

                    <i class="fa-solid fa-trash"></i>

                </button>

            </div>

        </td>

    `;


    return row;

}


/* =========================================================
   VIEW BOOKING
========================================================= */

function viewBooking(bookingId) {

    const booking =
        bookings.find(
            function (item) {

                return item.bookingId === bookingId;

            }
        );


    if (!booking) {

        return;

    }


    const user =
        findUser(
            booking.userId
        );


    const facility =
        findFacility(
            booking.facilityId
        );


    const slot =
        findTimeSlot(
            booking.timeSlotId
        );


    const status =
        getStatusName(
            booking.bookingStatusId
        );


    viewBookingId.textContent =
        booking.bookingId;


    viewUser.textContent =
        user
            ? `${user.firstName} ${user.lastName}`
            : booking.userId;


    viewFacility.textContent =
        facility
            ? facility.name
            : booking.facilityId;


    viewDate.textContent =
        slot
            ? slot.date
            : "-";


    viewTime.textContent =
        slot
            ? `${formatTime(slot.startTime)}
               - ${formatTime(slot.endTime)}`
            : "-";


    viewStatus.textContent =
        status;


    viewPurpose.textContent =
        booking.purpose || "-";


    viewBookingModal.classList.add(
        "active"
    );

}


/* =========================================================
   CLOSE VIEW MODAL
========================================================= */

function closeViewModal() {

    viewBookingModal.classList.remove(
        "active"
    );

}


/* =========================================================
   EDIT BOOKING
========================================================= */

function editBooking(bookingId) {

    const booking =
        bookings.find(
            function (item) {

                return item.bookingId === bookingId;

            }
        );


    if (!booking) {

        return;

    }


    editingBookingId =
        booking.bookingId;


    userSelect.value =
        booking.userId;


    facilitySelect.value =
        booking.facilityId;


    const slot =
        findTimeSlot(
            booking.timeSlotId
        );


    if (slot) {

        bookingDate.value =
            slot.date;

    }


    purposeInput.value =
        booking.purpose || "";


    updatePurposeCounter();


    document.getElementById(
        "formTitle"
    ).textContent =
        "Update Booking";


    submitBookingBtn.innerHTML =
        '<i class="fa-solid fa-floppy-disk"></i> Update Booking';


    bookingFormSection.classList.remove(
        "hidden"
    );


    bookingFormSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });


    /*
     * Load the available time slots
     * after setting the date.
     */

    loadTimeSlots().then(
        function () {

            timeSlotSelect.value =
                booking.timeSlotId;

        }
    );

}


/* =========================================================
   DELETE BOOKING
========================================================= */

async function deleteBooking(bookingId) {

    const confirmed =
        confirm(
            `Are you sure you want to delete booking ${bookingId}?`
        );


    if (!confirmed) {

        return;

    }


    try {

        const response =
            await fetch(
                `${BOOKING_API}/delete/${bookingId}`,
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
            "Booking deleted successfully.",
            "success"
        );


        await loadBookings();


    } catch (error) {

        console.error(
            "Error deleting booking:",
            error
        );


        showNotification(
            "Unable to delete the booking.",
            "error"
        );

    }

}


/* =========================================================
   UPDATE STATISTICS
========================================================= */

function updateStatistics() {

    totalBookings.textContent =
        bookings.length;


    approvedBookings.textContent =
        bookings.filter(
            function (booking) {

                return getStatusName(
                    booking.bookingStatusId
                ) === "approved";

            }
        ).length;


    pendingBookings.textContent =
        bookings.filter(
            function (booking) {

                return getStatusName(
                    booking.bookingStatusId
                ) === "pending";

            }
        ).length;


    cancelledBookings.textContent =
        bookings.filter(
            function (booking) {

                return getStatusName(
                    booking.bookingStatusId
                ) === "cancelled";

            }
        ).length;

}


/* =========================================================
   STATUS NAME
========================================================= */

function getStatusName(statusId) {

    /*
     * Update these IDs to match the values
     * in your BookingStatus table.
     */

    const statuses = {

        "BS001": "pending",

        "BS002": "approved",

        "BS003": "cancelled",

        "BS004": "completed"

    };


    return statuses[statusId]
        || "pending";

}


/* =========================================================
   FIND USER
========================================================= */

function findUser(userId) {

    return users.find(
        function (user) {

            return user.userId === userId;

        }
    );

}


/* =========================================================
   FIND FACILITY
========================================================= */

function findFacility(facilityId) {

    return facilities.find(
        function (facility) {

            return facility.facilityId === facilityId;

        }
    );

}


/* =========================================================
   FIND TIME SLOT
========================================================= */

function findTimeSlot(timeSlotId) {

    return timeSlots.find(
        function (slot) {

            return slot.timeSlotId === timeSlotId;

        }
    );

}


/* =========================================================
   PURPOSE CHARACTER COUNTER
========================================================= */

function updatePurposeCounter() {

    if (!purposeInput || !purposeCount) {

        return;

    }


    const length =
        purposeInput.value.length;


    purposeCount.textContent =
        `${length} / 500`;

}


/* =========================================================
   MINIMUM DATE
========================================================= */

function setMinimumDate() {

    if (!bookingDate) {

        return;

    }


    const today =
        new Date();


    const year =
        today.getFullYear();


    const month =
        String(
            today.getMonth() + 1
        ).padStart(2, "0");


    const day =
        String(
            today.getDate()
        ).padStart(2, "0");


    bookingDate.min =
        `${year}-${month}-${day}`;

}


/* =========================================================
   FORMAT TIME
========================================================= */

function formatTime(time) {

    if (!time) {

        return "-";

    }


    /*
     * Handles values such as:
     *
     * 09:00:00
     * 09:00
     */

    return time.substring(
        0,
        5
    );

}


/* =========================================================
   UPDATE BOOKING COUNT
========================================================= */

function updateBookingCount(count) {

    if (!bookingCount) {

        return;

    }


    bookingCount.textContent =
        `Showing ${count} booking${count === 1 ? "" : "s"}`;

}


/* =========================================================
   PAGINATION
========================================================= */

function updatePagination(totalItems) {

    const totalPages =
        Math.ceil(
            totalItems /
            bookingsPerPage
        );


    currentPage.textContent =
        currentPageNumber;


    previousPage.disabled =
        currentPageNumber <= 1;


    nextPage.disabled =
        currentPageNumber >= totalPages
        || totalPages === 0;

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
            ".booking-notification"
        );


    if (existing) {

        existing.remove();

    }


    const notification =
        document.createElement("div");


    notification.className =
        `booking-notification ${type}`;


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
        top: 25px;
        right: 25px;
        z-index: 9999;

        display: flex;
        align-items: center;
        gap: 10px;

        padding: 14px 18px;

        border-radius: 10px;

        background: white;

        box-shadow:
            0 10px 30px rgba(0,0,0,0.12);

        border: 1px solid #e5e7eb;

        color: ${
            type === "success"
                ? "#16a34a"
                : "#dc2626"
        };

        font-size: 13px;
        font-weight: 600;

        animation:
            notificationIn 0.25s ease;

    `;


    document.body.appendChild(
        notification
    );


    setTimeout(
        function () {

            notification.remove();

        },
        3500
    );

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