
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   FACILITY MANAGEMENT JAVASCRIPT
========================================================= */

(() => {
    "use strict";

    const FACILITY_API = `${window.location.origin}/facility`;

    document.addEventListener("DOMContentLoaded", () => {

        /* =====================================================
           ELEMENTS
        ===================================================== */

        const facilityForm =
            document.getElementById("facilityForm");

        const facilityIdInput =
            document.getElementById("facilityId");

        const facilityNameInput =
            document.getElementById("facilityName");

        const facilityTypeInput =
            document.getElementById("facilityType");

        const capacityInput =
            document.getElementById("capacity");

        const locationInput =
            document.getElementById("location");

        const departmentIdInput =
            document.getElementById("departmentId");

        const createFacilityButton =
            document.getElementById("createFacilityButton");

        const readFacilityButton =
            document.getElementById("readFacilityButton");

        const updateFacilityButton =
            document.getElementById("updateFacilityButton");

        const deleteFacilityButton =
            document.getElementById("deleteFacilityButton");

        const facilityMessage =
            document.getElementById("facilityMessage");

        const facilityResult =
            document.getElementById("facilityResult");

        const mobileMenuBtn =
            document.getElementById("mobileMenuBtn");

        const sidebar =
            document.querySelector(".sidebar");

        if (!facilityForm) return;

        /* =====================================================
           HELPERS
        ===================================================== */

        function escapeHtml(value) {
            return String(value ?? "")
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll('"', "&quot;")
                .replaceAll("'", "&#39;");
        }

        function formatType(value) {
            return String(value || "Not specified")
                .replaceAll("_", " ");
        }

        function showMessage(text, type = "success") {
            if (!facilityMessage) return;

            facilityMessage.textContent = text;

            facilityMessage.className = `message ${type}`;
        }

        function showEmptyResult() {
            if (!facilityResult) return;

            facilityResult.innerHTML = `
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

        function showResult(facility) {
            if (!facilityResult || !facility) return;

            const id = escapeHtml(
                facility.facilityId ?? facility.id ?? "-"
            );

            const name = escapeHtml(
                facility.name ?? facility.facilityName ?? "-"
            );

            const type = escapeHtml(
                formatType(facility.facilityType)
            );

            const capacity = escapeHtml(
                facility.capacity ?? "-"
            );

            const location = escapeHtml(
                facility.location ?? "-"
            );

            const department = escapeHtml(
                facility.departmentId ??
                facility.department?.departmentId ??
                "-"
            );

            facilityResult.innerHTML = `
                <div class="facility-info-display">

                    <div class="facility-info-heading">
                        <div class="facility-info-icon">
                            <i class="fa-solid fa-building"></i>
                        </div>

                        <div>
                            <h3>${name}</h3>
                            <span>Facility ID: ${id}</span>
                        </div>
                    </div>

                    <div class="facility-info-list">

                        <div class="facility-info-row">
                            <i class="fa-solid fa-layer-group"></i>
                            <div>
                                <span>Facility Type</span>
                                <strong>${type}</strong>
                            </div>
                        </div>

                        <div class="facility-info-row">
                            <i class="fa-solid fa-users"></i>
                            <div>
                                <span>Capacity</span>
                                <strong>${capacity} Students</strong>
                            </div>
                        </div>

                        <div class="facility-info-row">
                            <i class="fa-solid fa-location-dot"></i>
                            <div>
                                <span>Location</span>
                                <strong>${location}</strong>
                            </div>
                        </div>

                        <div class="facility-info-row">
                            <i class="fa-solid fa-sitemap"></i>
                            <div>
                                <span>Department</span>
                                <strong>${department}</strong>
                            </div>
                        </div>

                    </div>
                </div>
            `;
        }

        function selectFacilityType(type) {
            const value = String(type ?? "");

            if (!value) {
                facilityTypeInput.value = "";
                return;
            }

            // Preserve the exact value returned by the backend.
            // The Java enum must still be verified.
            const exists = Array.from(
                facilityTypeInput.options
            ).some(option => option.value === value);

            if (!exists) {
                const option = document.createElement("option");
                option.value = value;
                option.textContent = formatType(value);
                facilityTypeInput.appendChild(option);
            }

            facilityTypeInput.value = value;
        }

        function fillForm(facility) {
            if (!facility) return;

            facilityIdInput.value =
                facility.facilityId ?? facility.id ?? "";

            facilityNameInput.value =
                facility.name ?? facility.facilityName ?? "";

            capacityInput.value =
                facility.capacity ?? "";

            locationInput.value =
                facility.location ?? "";

            departmentIdInput.value =
                facility.departmentId ??
                facility.department?.departmentId ??
                "";

            selectFacilityType(facility.facilityType);
        }

        function buildPayload() {
            return {
                facilityId: facilityIdInput.value.trim(),
                name: facilityNameInput.value.trim(),
                facilityType: facilityTypeInput.value,
                capacity: Number(capacityInput.value),
                location: locationInput.value.trim(),
                departmentId: departmentIdInput.value.trim()
            };
        }

        function validatePayload(payload) {
            if (
                !payload.facilityId ||
                !payload.name ||
                !payload.facilityType ||
                !payload.location ||
                !payload.departmentId
            ) {
                showMessage(
                    "Please complete all facility fields.",
                    "error"
                );
                return false;
            }

            if (
                !Number.isInteger(payload.capacity) ||
                payload.capacity < 1
            ) {
                showMessage(
                    "Capacity must be a positive whole number.",
                    "error"
                );
                return false;
            }

            return true;
        }

        async function request(url, options = {}) {
            const response = await fetch(url, options);

            const responseText = await response.text();

            let data = null;

            if (responseText) {
                try {
                    data = JSON.parse(responseText);
                } catch {
                    data = responseText;
                }
            }

            if (!response.ok) {
                const detail =
                    typeof data === "string"
                        ? data
                        : data?.message ||
                        data?.error ||
                        response.statusText;

                throw new Error(
                    `HTTP ${response.status}: ${detail}`
                );
            }

            return data;
        }

        function refreshFacilityCards() {
            if (
                typeof window.refreshStaffFacilities === "function"
            ) {
                window.refreshStaffFacilities();
            }
        }

        function setButtonsDisabled(disabled) {
            [
                createFacilityButton,
                readFacilityButton,
                updateFacilityButton,
                deleteFacilityButton
            ].forEach(button => {
                if (button) button.disabled = disabled;
            });
        }

        /* =====================================================
           CREATE FACILITY
        ===================================================== */

        async function createFacility() {
            const payload = buildPayload();

            if (!validatePayload(payload)) return;

            setButtonsDisabled(true);

            try {
                const facility = await request(
                    `${FACILITY_API}/create`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(payload)
                    }
                );

                showMessage(
                    "Facility created successfully.",
                    "success"
                );

                if (facility && typeof facility === "object") {
                    fillForm(facility);
                    showResult(facility);
                } else {
                    showResult(payload);
                }

                refreshFacilityCards();

            } catch (error) {
                console.error("Create facility error:", error);

                showMessage(
                    `Could not create facility. ${error.message}`,
                    "error"
                );
            } finally {
                setButtonsDisabled(false);
            }
        }

        /* =====================================================
           SEARCH FACILITY
        ===================================================== */

        async function readFacility() {
            const id = facilityIdInput.value.trim();

            if (!id) {
                showMessage(
                    "Enter a Facility ID to search.",
                    "error"
                );
                return;
            }

            setButtonsDisabled(true);

            try {
                const facility = await request(
                    `${FACILITY_API}/read/${encodeURIComponent(id)}`
                );

                if (!facility || typeof facility !== "object") {
                    throw new Error(
                        "The server returned no facility details."
                    );
                }

                fillForm(facility);
                showResult(facility);

                showMessage(
                    "Facility loaded successfully.",
                    "success"
                );

            } catch (error) {
                console.error("Read facility error:", error);

                showMessage(
                    `Could not load facility. ${error.message}`,
                    "error"
                );

                showEmptyResult();

            } finally {
                setButtonsDisabled(false);
            }
        }

        /* =====================================================
           UPDATE FACILITY
        ===================================================== */

        async function updateFacility() {
            const payload = buildPayload();

            if (!validatePayload(payload)) return;

            const confirmed = window.confirm(
                `Update facility ${payload.facilityId}?`
            );

            if (!confirmed) return;

            setButtonsDisabled(true);

            try {
                const facility = await request(
                    `${FACILITY_API}/update`,
                    {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(payload)
                    }
                );

                showMessage(
                    "Facility updated successfully.",
                    "success"
                );

                if (facility && typeof facility === "object") {
                    fillForm(facility);
                    showResult(facility);
                } else {
                    showResult(payload);
                }

                refreshFacilityCards();

            } catch (error) {
                console.error("Update facility error:", error);

                showMessage(
                    `Could not update facility. ${error.message}`,
                    "error"
                );

            } finally {
                setButtonsDisabled(false);
            }
        }

        /* =====================================================
           DELETE FACILITY
        ===================================================== */

        async function deleteFacility() {
            const id = facilityIdInput.value.trim();

            if (!id) {
                showMessage(
                    "Enter a Facility ID to delete.",
                    "error"
                );
                return;
            }

            if (!window.confirm(`Delete facility ${id}?`)) {
                return;
            }

            setButtonsDisabled(true);

            try {
                await request(
                    `${FACILITY_API}/delete/${encodeURIComponent(id)}`,
                    {
                        method: "DELETE"
                    }
                );

                facilityForm.reset();
                showEmptyResult();

                showMessage(
                    `Facility ${id} deleted successfully.`,
                    "success"
                );

                refreshFacilityCards();

            } catch (error) {
                console.error("Delete facility error:", error);

                showMessage(
                    `Could not delete facility. ${error.message}`,
                    "error"
                );

            } finally {
                setButtonsDisabled(false);
            }
        }

        /* =====================================================
           EVENT LISTENERS
        ===================================================== */

        createFacilityButton?.addEventListener(
            "click",
            createFacility
        );

        readFacilityButton?.addEventListener(
            "click",
            readFacility
        );

        updateFacilityButton?.addEventListener(
            "click",
            updateFacility
        );

        deleteFacilityButton?.addEventListener(
            "click",
            deleteFacility
        );

        facilityForm.addEventListener("submit", event => {
            event.preventDefault();
        });

        facilityForm.addEventListener("reset", () => {
            showEmptyResult();
            showMessage("", "success");
        });

        mobileMenuBtn?.addEventListener("click", () => {
            sidebar?.classList.toggle("open");
        });

        // Allow the facility cards to fill the existing form.
        window.fillStaffFacilityForm = function (facility) {
            fillForm(facility);
            showResult(facility);

            facilityForm.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });

            showMessage(
                "Facility selected. Edit the details and click Update Facility.",
                "success"
            );
        };

    });
})();
