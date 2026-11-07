
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   STUDENT FACILITIES JAVASCRIPT
========================================================= */

/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";
const FACILITY_API = `${API_BASE_URL}/facility`;

/* =========================================================
   PAGE ELEMENTS
========================================================= */

const facilitiesGrid = document.getElementById("facilitiesGrid");
const facilitySearch = document.getElementById("facilitySearch");
const facilityTypeFilter = document.getElementById("facilityTypeFilter");
const refreshFacilitiesButton =
    document.getElementById("refreshFacilities");

const facilityCount = document.getElementById("facilityCount");
const facilitiesEmpty = document.getElementById("facilitiesEmpty");
const facilityMessage = document.getElementById("facilityMessage");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");

/* =========================================================
   FACILITY DATA
========================================================= */

let allFacilities = [];

/* =========================================================
   INITIALIZE PAGE
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadFacilities();

    facilitySearch?.addEventListener(
        "input",
        displayFacilities
    );

    facilityTypeFilter?.addEventListener(
        "change",
        displayFacilities
    );

    refreshFacilitiesButton?.addEventListener(
        "click",
        loadFacilities
    );

    mobileMenuBtn?.addEventListener("click", () => {
        sidebar?.classList.toggle("open");
    });

});

/* =========================================================
   LOAD FACILITIES FROM MYSQL
========================================================= */

async function loadFacilities() {

    try {

        showFacilityMessage(
            "Loading campus facilities...",
            "info"
        );

        if (refreshFacilitiesButton) {
            refreshFacilitiesButton.disabled = true;
        }

        const response = await fetch(
            `${FACILITY_API}/all`
        );

        if (!response.ok) {
            throw new Error(
                `Unable to load facilities. HTTP ${response.status}`
            );
        }

        const facilities = await response.json();

        if (!Array.isArray(facilities)) {
            throw new Error(
                "Invalid facility data received."
            );
        }

        allFacilities = facilities;

        // Populate the filter with the types
        // actually returned by the database.
        updateFacilityTypeFilter();

        displayFacilities();

        showFacilityMessage("", "");

    } catch (error) {

        console.error(
            "Error loading facilities:",
            error
        );

        allFacilities = [];

        if (facilitiesGrid) {
            facilitiesGrid.replaceChildren();
        }

        if (facilityCount) {
            facilityCount.textContent =
                "Facilities unavailable";
        }

        if (facilitiesEmpty) {
            facilitiesEmpty.hidden = true;
        }

        showFacilityMessage(
            "Could not load facilities. Make sure Spring Boot and MySQL are running.",
            "error"
        );

    } finally {

        if (refreshFacilitiesButton) {
            refreshFacilitiesButton.disabled = false;
        }

    }
}

/* =========================================================
   GET FACILITY NAME

   Supports both:
   - name
   - facilityName
========================================================= */

function getFacilityName(facility) {

    return facility.name ||
        facility.facilityName ||
        "Unnamed Facility";
}

/* =========================================================
   GET FACILITY TYPE
========================================================= */

function getFacilityType(facility) {

    return String(
        facility.facilityType ||
        facility.type ||
        "OTHER"
    );
}

/* =========================================================
   UPDATE FACILITY TYPE FILTER
========================================================= */

function updateFacilityTypeFilter() {

    if (!facilityTypeFilter) {
        return;
    }

    const previousValue = facilityTypeFilter.value;

    const types = [
        ...new Set(
            allFacilities.map(getFacilityType)
        )
    ].sort();

    facilityTypeFilter.replaceChildren();

    const allOption = document.createElement("option");
    allOption.value = "ALL";
    allOption.textContent = "All Facility Types";

    facilityTypeFilter.appendChild(allOption);

    types.forEach(type => {

        const option = document.createElement("option");

        option.value = type;
        option.textContent = formatFacilityType(type);

        facilityTypeFilter.appendChild(option);

    });

    if (types.includes(previousValue)) {
        facilityTypeFilter.value = previousValue;
    } else {
        facilityTypeFilter.value = "ALL";
    }
}

/* =========================================================
   DISPLAY FACILITIES
========================================================= */

function displayFacilities() {

    if (!facilitiesGrid) {
        return;
    }

    facilitiesGrid.replaceChildren();

    const searchText = String(
        facilitySearch?.value || ""
    ).trim().toLowerCase();

    const selectedType =
        facilityTypeFilter?.value || "ALL";

    const filteredFacilities = allFacilities.filter(
        facility => {

            const name = String(
                getFacilityName(facility)
            ).toLowerCase();

            const location = String(
                facility.location || ""
            ).toLowerCase();

            const type = getFacilityType(facility)
                .toLowerCase();

            const facilityId = String(
                facility.facilityId || ""
            ).toLowerCase();

            const matchesSearch =
                name.includes(searchText) ||
                location.includes(searchText) ||
                type.replaceAll("_", " ")
                    .includes(searchText) ||
                facilityId.includes(searchText);

            const matchesType =
                selectedType === "ALL" ||
                getFacilityType(facility) === selectedType;

            return matchesSearch && matchesType;
        }
    );

    if (facilityCount) {

        facilityCount.textContent =
            `Showing ${filteredFacilities.length} of ${allFacilities.length} facilities`;

    }

    if (facilitiesEmpty) {
        facilitiesEmpty.hidden =
            filteredFacilities.length !== 0;
    }

    filteredFacilities.forEach(facility => {

        const card = createFacilityCard(facility);

        facilitiesGrid.appendChild(card);

    });
}

/* =========================================================
   CREATE FACILITY CARD
========================================================= */

function createFacilityCard(facility) {

    const card = document.createElement("div");
    card.className = "student-facility-card";

    const facilityType = getFacilityType(facility);

    // Corrected: uses facility.name
    const facilityName = getFacilityName(facility);

    const iconClass = getFacilityIcon(facilityType);

    /* FACILITY ICON */

    const iconBox = document.createElement("div");
    iconBox.className = "student-facility-icon";

    const icon = document.createElement("i");
    icon.className = `fa-solid ${iconClass}`;

    iconBox.appendChild(icon);

    /* FACILITY TYPE */

    const typeBadge = document.createElement("span");
    typeBadge.className = "student-facility-type";

    typeBadge.textContent =
        formatFacilityType(facilityType);

    /* FACILITY NAME */

    const title = document.createElement("h3");
    title.textContent = facilityName;

    /* FACILITY DETAILS */

    const details = document.createElement("div");
    details.className = "student-facility-details";

    details.appendChild(
        createDetailRow(
            "fa-location-dot",
            "Location",
            facility.location
        )
    );

    details.appendChild(
        createDetailRow(
            "fa-users",
            "Capacity",
            facility.capacity != null
                ? `${facility.capacity} people`
                : "Not specified"
        )
    );

    details.appendChild(
        createDetailRow(
            "fa-id-card",
            "Facility ID",
            facility.facilityId
        )
    );

    details.appendChild(
        createDetailRow(
            "fa-building",
            "Department",
            facility.departmentId
        )
    );

    /* BOOK FACILITY BUTTON */

    const bookButton = document.createElement("button");

    bookButton.type = "button";
    bookButton.className = "student-book-button";

    const bookIcon = document.createElement("i");
    bookIcon.className = "fa-solid fa-calendar-plus";

    const bookText = document.createElement("span");
    bookText.textContent = "Book Facility";

    bookButton.append(bookIcon, bookText);

    bookButton.addEventListener("click", () => {
        bookFacility(facility.facilityId);
    });

    card.append(
        iconBox,
        typeBadge,
        title,
        details,
        bookButton
    );

    return card;
}

/* =========================================================
   CREATE DETAIL ROW
========================================================= */

function createDetailRow(iconName, label, value) {

    const row = document.createElement("div");
    row.className = "student-facility-detail-row";

    const icon = document.createElement("i");
    icon.className = `fa-solid ${iconName}`;

    const text = document.createElement("span");

    const labelElement = document.createElement("strong");
    labelElement.textContent = `${label}: `;

    const valueElement = document.createElement("span");

    valueElement.textContent =
        value === null ||
        value === undefined ||
        value === ""
            ? "Not specified"
            : String(value);

    text.append(
        labelElement,
        valueElement
    );

    row.append(icon, text);

    return row;
}

/* =========================================================
   FORMAT FACILITY TYPE
========================================================= */

function formatFacilityType(type) {

    return String(type)
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, character =>
            character.toUpperCase()
        );
}

/* =========================================================
   FACILITY ICONS
========================================================= */

function getFacilityIcon(type) {

    switch (String(type).toUpperCase()) {

        case "LECTURE_HALL":
        case "HALL":
            return "fa-person-chalkboard";

        case "CLASSROOM":
        case "ROOM":
            return "fa-chalkboard";

        case "LABORATORY":
        case "COMPUTER_LAB":
        case "LAB":
            return "fa-flask";

        case "MEETING_ROOM":
            return "fa-people-group";

        case "SPORTS_FACILITY":
            return "fa-volleyball";

        case "AUDITORIUM":
            return "fa-landmark";

        default:
            return "fa-building";
    }
}

/* =========================================================
   BOOK FACILITY
========================================================= */

function bookFacility(facilityId) {

    if (!facilityId) {

        showFacilityMessage(
            "This facility does not have a valid Facility ID.",
            "error"
        );

        return;
    }

    // Pass the selected facility ID to the booking page.
    // booking.js will need to read this parameter.

    const bookingUrl =
        `booking.html?facilityId=${encodeURIComponent(facilityId)}`;

    window.location.href = bookingUrl;
}

/* =========================================================
   DISPLAY MESSAGES
========================================================= */

function showFacilityMessage(message, type) {

    if (!facilityMessage) {
        return;
    }

    facilityMessage.textContent = message;

    facilityMessage.className = message
        ? `student-facility-message ${type}`
        : "student-facility-message";
}
