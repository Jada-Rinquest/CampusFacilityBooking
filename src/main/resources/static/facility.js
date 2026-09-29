/* =========================================================
   CAMPUS FACILITY BOOKING
   FACILITY MANAGEMENT JAVASCRIPT
========================================================= */

const API_BASE_URL = "http://localhost:8080";
const FACILITY_API = `${API_BASE_URL}/facility`;

const facilityForm = document.getElementById("facilityForm");
const facilityIdInput = document.getElementById("facilityId");
const facilityNameInput = document.getElementById("facilityName");
const facilityTypeInput = document.getElementById("facilityType");
const capacityInput = document.getElementById("capacity");
const locationInput = document.getElementById("location");
const departmentIdInput = document.getElementById("departmentId");

const createFacilityButton = document.getElementById("createFacilityButton");
const readFacilityButton = document.getElementById("readFacilityButton");
const updateFacilityButton = document.getElementById("updateFacilityButton");
const deleteFacilityButton = document.getElementById("deleteFacilityButton");

const facilityMessage = document.getElementById("facilityMessage");
const facilityResult = document.getElementById("facilityResult");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


document.addEventListener("DOMContentLoaded", () => {

    if (createFacilityButton) {
        createFacilityButton.addEventListener("click", createFacility);
    }

    if (readFacilityButton) {
        readFacilityButton.addEventListener("click", readFacility);
    }

    if (updateFacilityButton) {
        updateFacilityButton.addEventListener("click", updateFacility);
    }

    if (deleteFacilityButton) {
        deleteFacilityButton.addEventListener("click", deleteFacility);
    }

    if (facilityForm) {
        facilityForm.addEventListener("submit", (event) => event.preventDefault());
    }

    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener("click", () => sidebar.classList.toggle("open"));
    }

});


function buildPayload() {

    return {
        facilityId: facilityIdInput.value.trim(),
        name: facilityNameInput.value.trim(),
        facilityType: facilityTypeInput.value,
        capacity: parseInt(capacityInput.value, 10),
        location: locationInput.value.trim(),
        departmentId: departmentIdInput.value.trim()
    };

}


async function createFacility() {

    const payload = buildPayload();

    if (!payload.facilityId || !payload.name) {
        showMessage("Facility ID and name are required.", "error");
        return;
    }

    try {

        const response = await fetch(`${FACILITY_API}/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Create failed: ${response.status}`);
        }

        const facility = await response.json();

        showMessage("Facility created successfully.", "success");
        showResult(facility);

    } catch (error) {
        console.error("Error creating facility:", error);
        showMessage("Could not create the facility.", "error");
    }

}


async function readFacility() {

    const id = facilityIdInput.value.trim();

    if (!id) {
        showMessage("Enter a facility ID to read.", "error");
        return;
    }

    try {

        const response = await fetch(`${FACILITY_API}/read/${id}`);

        if (!response.ok) {
            throw new Error(`Read failed: ${response.status}`);
        }

        const facility = await response.json();

        facilityNameInput.value = facility.name || "";
        facilityTypeInput.value = facility.facilityType || "";
        capacityInput.value = facility.capacity || "";
        locationInput.value = facility.location || "";
        departmentIdInput.value = facility.departmentId || "";

        showMessage("Facility loaded.", "success");
        showResult(facility);

    } catch (error) {
        console.error("Error reading facility:", error);
        showMessage("Facility not found.", "error");
    }

}


async function updateFacility() {

    const payload = buildPayload();

    if (!payload.facilityId) {
        showMessage("Facility ID is required to update.", "error");
        return;
    }

    try {

        const response = await fetch(`${FACILITY_API}/update`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Update failed: ${response.status}`);
        }

        const facility = await response.json();

        showMessage("Facility updated successfully.", "success");
        showResult(facility);

    } catch (error) {
        console.error("Error updating facility:", error);
        showMessage("Could not update the facility.", "error");
    }

}


async function deleteFacility() {

    const id = facilityIdInput.value.trim();

    if (!id) {
        showMessage("Enter a facility ID to delete.", "error");
        return;
    }

    if (!confirm(`Delete facility ${id}?`)) {
        return;
    }

    try {

        const response = await fetch(`${FACILITY_API}/delete/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            throw new Error(`Delete failed: ${response.status}`);
        }

        showMessage("Facility deleted.", "success");

        if (facilityForm) {
            facilityForm.reset();
        }

        if (facilityResult) {
            facilityResult.textContent = "";
        }

    } catch (error) {
        console.error("Error deleting facility:", error);
        showMessage("Could not delete the facility.", "error");
    }

}


function showMessage(text, type) {

    if (!facilityMessage) {
        return;
    }

    facilityMessage.textContent = text;
    facilityMessage.className = type;

}


function showResult(facility) {

    if (!facilityResult) {
        return;
    }

    facilityResult.textContent = JSON.stringify(facility, null, 2);

}
