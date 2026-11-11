
/* =========================================================
   STAFF FACILITIES DIRECTORY
   Campus Facility Booking System
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    // Match these routes to FacilityController.java.
    const FACILITY_API = `${window.location.origin}/facility`;

    const grid = document.getElementById("staffFacilitiesGrid");
    const searchInput = document.getElementById("facilityListSearch");
    const countLabel = document.getElementById("facilityListCount");
    const message = document.getElementById("facilitiesListMessage");
    const refreshButton = document.getElementById("refreshFacilitiesBtn");

    if (!grid) return;

    let facilities = [];

    /* =====================================================
       HELPERS
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
        return facility.facilityName ?? facility.name ?? "";
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
       LOAD FACILITIES
    ===================================================== */

    async function loadFacilities() {

        showMessage("Loading facilities...");

        try {

            const response = await fetch(`${FACILITY_API}/all`);

            if (!response.ok) {
                throw new Error(
                    `Unable to load facilities (${response.status})`
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

            console.error("Facility loading error:", error);

            grid.innerHTML = "";

            if (countLabel) {
                countLabel.textContent = "Unable to load facilities";
            }

            showMessage(error.message);
        }
    }

    /* =====================================================
       RENDER FACILITY CARDS
    ===================================================== */

    function renderFacilities() {

        const query = (searchInput?.value || "")
            .trim()
            .toLowerCase();

        const filtered = facilities.filter(facility => {

            const searchable = [
                getFacilityId(facility),
                getFacilityName(facility),
                getFacilityType(facility),
                facility.location,
                getDepartmentId(facility)
            ].join(" ").toLowerCase();

            return searchable.includes(query);
        });

        if (countLabel) {
            countLabel.textContent =
                `${filtered.length} of ${facilities.length} facilities`;
        }

        if (filtered.length === 0) {

            grid.innerHTML = `
                <div class="staff-facility-card">
                    <h3>No facilities found</h3>
                    <p>No facilities match your search.</p>
                </div>
            `;

            return;
        }

        grid.innerHTML = filtered.map(facility => {

            const id = escapeHtml(getFacilityId(facility));
            const name = escapeHtml(getFacilityName(facility));
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

        }).join("");
    }

    /* =====================================================
       EDIT FACILITY
    ===================================================== */

    function editFacility(id) {

        const facility = facilities.find(
            item => String(getFacilityId(item)) === String(id)
        );

        if (!facility) {
            showMessage("Facility not found.");
            return;
        }

        const values = {
            facilityId: getFacilityId(facility),
            facilityName: getFacilityName(facility),
            capacity: facility.capacity ?? "",
            location: facility.location ?? "",
            departmentId: getDepartmentId(facility),
            facilityType: getFacilityType(facility)
        };

        Object.entries(values).forEach(([fieldId, value]) => {

            const input = document.getElementById(fieldId);

            if (input) {
                input.value = value;
            }
        });

        document.getElementById("facilityForm")
            ?.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });

        showMessage(
            `Editing facility ${id}. Make your changes and click Update Facility.`
        );
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

            const response = await fetch(
                `${FACILITY_API}/delete/${encodeURIComponent(id)}`,
                {
                    method: "DELETE"
                }
            );

            if (!response.ok) {
                throw new Error(
                    `Delete failed (${response.status})`
                );
            }

            await loadFacilities();

            showMessage(
                `Facility ${id} deleted successfully.`
            );

        } catch (error) {

            console.error("Facility deletion error:", error);

            showMessage(
                `Unable to delete facility ${id}: ${error.message}`
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

    // Can be called after a successful Create or Update.
    window.refreshStaffFacilities = loadFacilities;

    loadFacilities();

});
