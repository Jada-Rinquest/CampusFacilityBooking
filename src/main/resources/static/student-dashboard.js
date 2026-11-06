/* =========================================================
   CAMPUS FACILITY BOOKING
   STUDENT DASHBOARD JAVASCRIPT
   (wrapped in a function so its names can't clash with auth.js)
========================================================= */

(function () {

    "use strict";

    const API_URL = (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1")
        ? (window.location.port === "8080" ? "" : "http://localhost:8080")
        : window.location.origin;

    const SIGN_IN_PAGE = "index.html";
    const STAFF_DASHBOARD = "staff-dashboard.html";

    // Everything script.js stores at sign-in
    const STORED_KEYS = ["loggedInUser", "loginId", "registrarId", "username"];

    const MONTHS = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];

    const UPCOMING_LIMIT = 3;
    const NOTIFICATION_LIMIT = 3;


    /* =====================================================
       START
    ===================================================== */

    document.addEventListener("DOMContentLoaded", () => {

        const user = requireStudent();

        if (!user) {
            return;
        }

        showUser(user);

        loadDashboard(user);

        const menuButton = document.getElementById("mobileMenuBtn");
        const sidebar = document.querySelector(".sidebar");

        if (menuButton && sidebar) {
            menuButton.addEventListener("click", () => sidebar.classList.toggle("open"));
        }

        const logoutLink = document.getElementById("logoutLink");

        if (logoutLink) {
            logoutLink.addEventListener("click", signOut);
        }

    });


    /* =====================================================
       WHO IS SIGNED IN
    ===================================================== */

    function getSignedInUser() {

        try {
            return JSON.parse(
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser")
            );
        } catch (error) {
            return null;
        }

    }


    // Only a signed-in student may stay on this page
    function requireStudent() {

        const user = getSignedInUser();

        if (!user) {
            window.location.replace(SIGN_IN_PAGE);
            return null;
        }

        if (user.accountType === "STAFF") {
            window.location.replace(STAFF_DASHBOARD);
            return null;
        }

        return user;

    }


    function signOut() {

        // Clear the saved session, otherwise "Logout" leaves the user signed in
        STORED_KEYS.forEach((key) => {
            sessionStorage.removeItem(key);
            localStorage.removeItem(key);
        });

    }


    function showUser(user) {

        const name = `${user.firstName || ""} ${user.lastName || ""}`.trim() || user.username || "Student";

        setText("welcomeName", name);
        setText("topbarName", name);
        setText("topbarInitials", initialsOf(name));

    }


    function initialsOf(name) {

        const parts = String(name).trim().split(/\s+/);

        if (parts.length === 1) {
            return parts[0].substring(0, 2).toUpperCase();
        }

        return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();

    }


    /* =====================================================
       DASHBOARD DATA
    ===================================================== */

    async function loadDashboard(user) {

        const [bookings, facilities, notifications, timeSlots] = await Promise.all([
            fetchList("booking"),
            fetchList("facility"),
            fetchList("notification"),
            fetchList("timeslot")
        ]);

        // Only this student's own records
        const myBookings = bookings.filter((b) => sameId(b.userId, user.userId));
        const myNotifications = notifications.filter((n) => sameId(n.userId, user.userId));

        const slotsById = indexBy(timeSlots, "timeSlotId");
        const facilitiesById = indexBy(facilities, "facilityId");

        const upcoming = myBookings
            .map((b) => ({
                booking: b,
                slot: slotsById[b.timeSlotId],
                facility: facilitiesById[b.facilityId]
            }))
            .filter((item) => isUpcoming(item.booking, item.slot))
            .sort((a, b) => dateOf(a.slot) - dateOf(b.slot));

        setText("totalBookings", myBookings.length);
        setText("upcomingBookings", upcoming.length);
        setText("availableFacilities", facilities.length);

        renderBookings(upcoming.slice(0, UPCOMING_LIMIT));
        renderNotifications(myNotifications);

    }


    async function fetchList(resource) {

        try {

            const response = await fetch(`${API_URL}/${resource}/all`);

            if (!response.ok) {
                throw new Error(`Request failed: ${response.status}`);
            }

            const data = await response.json();

            return Array.isArray(data) ? data : [];

        } catch (error) {

            console.error(`Could not load ${resource}:`, error);

            return [];

        }

    }


    /* =====================================================
       BOOKINGS
    ===================================================== */

    function isUpcoming(booking, slot) {

        if (!slot || !slot.date) {
            return false;
        }

        const status = String(booking.bookingStatus || "").toUpperCase();

        if (status === "CANCELLED" || status === "REJECTED") {
            return false;
        }

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        return dateOf(slot) >= today;

    }


    // Read a date as a local date so the day never shifts.
    // Handles "2026-09-03", "2026-09-03T10:00:00" and [2026, 9, 3].
    function dateOf(slot) {

        const none = new Date(8640000000000000);

        if (!slot || !slot.date) {
            return none;
        }

        let parts;

        if (Array.isArray(slot.date)) {
            parts = slot.date.map(Number);
        } else {
            parts = String(slot.date).split("T")[0].split("-").map(Number);
        }

        const [year, month, day] = parts;

        if (!year || !month || !day) {
            return none;
        }

        return new Date(year, month - 1, day);

    }


    // Handles "10:00:00" and [10, 0]
    function timeText(value) {

        if (value === undefined || value === null || value === "") {
            return "";
        }

        if (Array.isArray(value)) {
            return value.slice(0, 2).map((n) => String(n).padStart(2, "0")).join(":");
        }

        return String(value).slice(0, 5);

    }


    function renderBookings(items) {

        const list = document.getElementById("bookingList");
        const empty = document.getElementById("emptyBookings");

        if (!list) {
            return;
        }

        if (items.length === 0) {
            list.innerHTML = "";
            if (empty) empty.style.display = "block";
            return;
        }

        if (empty) empty.style.display = "none";

        list.innerHTML = items.map(({ booking, slot, facility }) => {

            const date = dateOf(slot);
            const month = MONTHS[date.getMonth()] || "";
            const day = String(date.getDate()).padStart(2, "0");

            const facilityName = (facility && (facility.name || facility.facilityName))
                || (booking.facility && booking.facility.name)
                || "Facility booking";

            const place = facility && (facility.location || facility.building);

            const start = slot ? timeText(slot.startTime) : "";
            const end = slot ? timeText(slot.endTime) : "";
            const time = start && end ? `${start} - ${end}` : start;

            const status = String(booking.bookingStatus || "PENDING").toUpperCase();

            return `
                <div class="booking-item">

                    <div class="booking-date">
                        <span>${esc(month)}</span>
                        <strong>${esc(day)}</strong>
                    </div>

                    <div class="booking-info">
                        <strong>${esc(facilityName)}</strong>
                        ${time ? `<span><i class="fa-regular fa-clock"></i> ${esc(time)}</span>` : ""}
                        ${place ? `<span><i class="fa-solid fa-location-dot"></i> ${esc(place)}</span>` : ""}
                    </div>

                    <span class="status-badge ${statusClass(status)}">${esc(titleCase(status))}</span>

                </div>`;

        }).join("");

    }


    function statusClass(status) {

        if (status === "APPROVED" || status === "CONFIRMED") {
            return "confirmed";
        }

        if (status === "PENDING") {
            return "pending";
        }

        return status.toLowerCase();

    }


    /* =====================================================
       NOTIFICATIONS
    ===================================================== */

    function renderNotifications(mine) {

        const list = document.getElementById("notificationList");

        // Newest first, if the records carry a date
        const sorted = mine.slice().sort((a, b) => notificationDate(b) - notificationDate(a));

        // Count unread only if the records say which are read
        const hasReadFlag = mine.some((n) => readFlagOf(n) !== undefined);
        const unreadCount = hasReadFlag
            ? mine.filter((n) => readFlagOf(n) === false).length
            : mine.length;

        setText("unreadNotifications", unreadCount);

        const badge = document.getElementById("topbarNotificationCount");

        if (badge) {
            badge.textContent = unreadCount;
            badge.style.display = unreadCount > 0 ? "" : "none";
        }

        if (!list) {
            return;
        }

        if (sorted.length === 0) {
            list.innerHTML = `<p class="empty-note">You have no notifications.</p>`;
            return;
        }

        list.innerHTML = sorted.slice(0, NOTIFICATION_LIMIT).map((n) => {

            const title = n.title || n.subject || n.type || "Notification";
            const message = n.message || n.description || n.content || "";
            const when = notificationDate(n);
            const unread = readFlagOf(n) === false;

            return `
                <div class="notification-item">

                    <div class="notification-icon system">
                        <i class="fa-solid fa-circle-info"></i>
                    </div>

                    <div class="notification-content">
                        <strong>${esc(title)}</strong>
                        ${message ? `<p>${esc(message)}</p>` : ""}
                        ${when > 0 ? `<span>${esc(new Date(when).toLocaleString("en-ZA", { dateStyle: "medium", timeStyle: "short" }))}</span>` : ""}
                    </div>

                    ${unread ? `<span class="unread-dot"></span>` : ""}

                </div>`;

        }).join("");

    }


    function notificationDate(n) {

        const raw = n.createdAt || n.dateTime || n.timestamp || n.date;
        const time = raw ? new Date(raw).getTime() : 0;

        return Number.isNaN(time) ? 0 : time;

    }


    // true = read, false = unread, undefined = record has no such field
    function readFlagOf(n) {

        if (typeof n.read === "boolean") return n.read;
        if (typeof n.isRead === "boolean") return n.isRead;
        if (typeof n.seen === "boolean") return n.seen;

        return undefined;

    }


    /* =====================================================
       SMALL HELPERS
    ===================================================== */

    function sameId(a, b) {

        return a !== undefined && a !== null && String(a) === String(b);

    }


    function indexBy(list, key) {

        const map = {};

        list.forEach((item) => { map[item[key]] = item; });

        return map;

    }


    function setText(elementId, value) {

        const element = document.getElementById(elementId);

        if (element) {
            element.textContent = value;
        }

    }


    function titleCase(text) {

        return text.charAt(0) + text.slice(1).toLowerCase();

    }


    // Stop booking or notification text from being treated as HTML
    function esc(value) {

        return String(value).replace(/[&<>"']/g, (c) => ({
            "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
        }[c]));

    }

})();