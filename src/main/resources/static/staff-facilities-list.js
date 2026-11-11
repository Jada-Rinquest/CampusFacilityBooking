
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   STAFF FACILITIES DIRECTORY
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    "use strict";

    /* =====================================================
       API CONFIGURATION
    ===================================================== */

    const FACILITY_API = `${window.location.origin}/facility`;

    /* =====================================================
       HTML ELEMENTS
    ===================================================== */

    const grid =
        document.getElementById("staffFacilitiesGrid");

    const searchInput =
        document.getElementById("facilityListSearch");

    const countLabel =
        document.getElementById("facilityListCount");

    const message =
        document.getElementById("facilitiesListMessage");

    const refreshButton =
        document.getElementById("refreshFacilitiesBtn");

    if (!grid) return;

    let facilities = [];

    /* =====================================================
       HELPER FUNCTIONS
    ===================================================== */

    function showMessage(text) {
        if (message) {
            message.textContent = text;
        }
    }

    function escapeHtml(value) {
        return String(value ?? "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#39;");
    }

    function getFacilityId(facility) {
        return facility.facilityId ?? facility.id ?? "";
    }

    function getFacilityName(facility) {
        return facility.name ?? facility.facilityName ?? "";
    }

    function getFacilityType(facility) {
        return facility.facilityType ?? facility.type ?? "";
    }

    function getDepartmentId(facility) {
        return facility.departmentId ??
            facility.department?.departmentId ?? "";
    }

    function formatType(type) {
        return String(type || "Not specified")
            .replaceAll("_", " ");
    }

    /* =====================================================
       LOAD ALL FACILITIES
    ===================================================== */

    async function loadFacilities() {

        showMessage("Loading facilities...");

        try {

            const response = await fetch(
                `${FACILITY_API}/all`,
                {
                    cache: "no-store"
                }
            );

            if (!response.ok) {
                throw new Error(
                    `Unable to load facilities (HTTP ${response.status})`
                );
            }

            const data = await response.json();

            if (!Array.isArray(data)) {
                throw new Error(
                    "The server did not return a facility list."
                );
            }

            facilities = data;

            renderFacilities();

            showMessage("");

        } catch (error) {

            console.error(
                "Error loading staff facilities:",
                error
            );

            grid.innerHTML = "";

            if (countLabel) {
                countLabel.textContent =
                    "Unable to load facilities";
            }

            showMessage(error.message);
        }
    }

    /* =====================================================
       DISPLAY FACILITY CARDS
    ===================================================== */

    function renderFacilities() {

        const query = (searchInput?.value || "")
            .trim()
            .toLowerCase();

        const filteredFacilities = facilities.filter(
            facility => {

                const searchable = [
                    getFacilityId(facility),
                    getFacilityName(facility),
                    getFacilityType(facility),
                    facility.location,
                    getDepartmentId(facility)
                ]
                    .join(" ")
                    .toLowerCase();

                return searchable.includes(query);
            }
        );

        if (countLabel) {
            countLabel.textContent =
                `${filteredFacilities.length} of ${facilities.length} facilities`;
        }

        if (filteredFacilities.length === 0) {

            grid.innerHTML = `
                <div class="staff-facility-card">
                    <div class="staff-facility-icon">
                        <i class="fa-solid fa-building"></i>
                    </div>

                    <h3>No Facilities Found</h3>

                    <p>
                        No campus facilities match your search.
                    </p>
                </div>
            `;

            return;
        }

        grid.innerHTML = filteredFacilities.map(
            facility => {

                const id = escapeHtml(
                    getFacilityId(facility)
                );

                const name = escapeHtml(
                    getFacilityName(facility)
                );

                const location = escapeHtml(
                    facility.location || "Not specified"
                );

                const capacity = escapeHtml(
                    facility.capacity ?? "Not specified"
                );

                const type = escapeHtml(
                    formatType(getFacilityType(facility))
                );

                const department = escapeHtml(
                    getDepartmentId(facility) || "Not specified"
                );

                return `
                    <article class="staff-facility-card">

                        <div class="staff-facility-card-header">

                            <div class="staff-facility-icon">
                                <i class="fa-solid fa-building"></i>
                            </div>

                            <span class="staff-facility-id">
                                ${id}
                            </span>

                        </div>

                        <h3>${name}</h3>

                        <div class="staff-facility-details">

                            <div class="staff-facility-detail">
                                <i class="fa-solid fa-location-dot"></i>
                                <span>${location}</span>
                            </div>

                            <div class="staff-facility-detail">
                                <i class="fa-solid fa-users"></i>
                                <span>Capacity: ${capacity}</span>
                            </div>

                            <div class="staff-facility-detail">
                                <i class="fa-solid fa-layer-group"></i>
                                <span>${type}</span>
                            </div>

                            <div class="staff-facility-detail">
                                <i class="fa-solid fa-sitemap"></i>
                                <span>Department: ${department}</span>
                            </div>

                        </div>

                        <div class="staff-facility-actions">

                            <button
                                type="button"
                                class="staff-edit-btn"
                                data-action="edit"
                                data-id="${id}">

                                <i class="fa-solid fa-pen"></i>
                                Edit

                            </button>

                            <button
                                type="button"
                                class="staff-delete-btn"
                                data-action="delete"
                                data-id="${id}">

                                <i class="fa-solid fa-trash"></i>
                                Delete

                            </button>

                        </div>

                    </article>
                `;

            }
        ).join("");
    }

    /* =====================================================
       EDIT FACILITY
    ===================================================== */

    function editFacility(id) {

        const facility = facilities.find(
            item =>
                String(getFacilityId(item)) === String(id)
        );

        if (!facility) {
            showMessage("Facility not found.");
            return;
        }

        // Use the shared form function from facility.js.
        if (
            typeof window.fillStaffFacilityForm === "function"
        ) {

            window.fillStaffFacilityForm(facility);

            showMessage(
                `Editing facility ${id}. Make your changes and click Update Facility.`
            );

        } else {

            showMessage(
                "Unable to open the facility editor. Check that facility.js is loaded."
            );

        }
    }

    /* =====================================================
       DELETE FACILITY
    ===================================================== */

    async function deleteFacility(id) {

        const confirmed = window.confirm(
            `Are you sure you want to delete facility ${id}?`
        );

        if (!confirmed) return;

        try {

            showMessage(`Deleting facility ${id}...`);

            const response = await fetch(
                `${FACILITY_API}/delete/${encodeURIComponent(id)}`,
                {
                    method: "DELETE"
                }
            );

            if (!response.ok) {

                const errorText = await response.text();

                throw new Error(
                    `HTTP ${response.status}: ${errorText || response.statusText}`
                );
            }

            await loadFacilities();

            showMessage(
                `Facility ${id} deleted successfully.`
            );

            // Clear the form if the deleted facility is selected.
            const selectedId =
                document.getElementById("facilityId")?.value;

            if (String(selectedId) === String(id)) {

                document.getElementById("facilityForm")?.reset();

                const result =
                    document.getElementById("facilityResult");

                if (result) {
                    result.innerHTML = `
                        <div class="empty-result">
                            <i class="fa-solid fa-building"></i>
                            <h4>No Facility Selected</h4>
                            <p>
                                Search for a facility to view
                                its information.
                            </p>
                        </div>
                    `;
                }
            }

        } catch (error) {

            console.error(
                "Error deleting facility:",
                error
            );

            showMessage(
                `Could not delete facility ${id}. ${error.message}`
            );
        }
    }

    /* =====================================================
       EVENT LISTENERS
    ===================================================== */

    searchInput?.addEventListener(
        "input",
        renderFacilities
    );

    refreshButton?.addEventListener(
        "click",
        loadFacilities
    );

    grid.addEventListener("click", event => {

        const button = event.target.closest(
            "button[data-action]"
        );

        if (!button || !grid.contains(button)) return;

        const id = button.dataset.id;
        const action = button.dataset.action;

        if (action === "edit") {
            editFacility(id);
        }

        if (action === "delete") {
            deleteFacility(id);
        }
    });

    /* =====================================================
       SHARED REFRESH FUNCTION
    ===================================================== */

    window.refreshStaffFacilities = loadFacilities;

    /* =====================================================
       INITIAL LOAD
    ===================================================== */

    loadFacilities();

});
