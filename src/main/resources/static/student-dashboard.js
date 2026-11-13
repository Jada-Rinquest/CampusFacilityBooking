
/* =========================================================
   CAMPUS FACILITY BOOKING
   STUDENT DASHBOARD JAVASCRIPT
========================================================= */

(function () {

    "use strict";

    const API_URL = (
        window.location.hostname === "localhost" ||
        window.location.hostname === "127.0.0.1"
    )
        ? (window.location.port === "8080"
            ? ""
            : "http://localhost:8080")
        : window.location.origin;

    const SIGN_IN_PAGE = "index.html";
    const STAFF_DASHBOARD = "staff-dashboard.html";

    const STORED_KEYS = [
        "loggedInUser",
        "loginId",
        "registrarId",
        "username"
    ];

    const MONTHS = [
        "JAN", "FEB", "MAR", "APR",
        "MAY", "JUN", "JUL", "AUG",
        "SEP", "OCT", "NOV", "DEC"
    ];

    const UPCOMING_LIMIT = 3;
    const NOTIFICATION_LIMIT = 3;

    /* =====================================================
       INITIALIZATION
    ===================================================== */

    document.addEventListener("DOMContentLoaded", () => {

        const user = requireStudent();

        if (!user) {
            return;
        }

        showUser(user);
        loadDashboard(user);

        const menuButton =
            document.getElementById("mobileMenuBtn");

        const sidebar =
            document.querySelector(".sidebar");

        if (menuButton && sidebar) {

            menuButton.addEventListener("click", () => {
                sidebar.classList.toggle("open");
            });

        }

        const logoutLink =
            document.getElementById("logoutLink");

        if (logoutLink) {
            logoutLink.addEventListener("click", signOut);
        }

    });

    /* =====================================================
       LOGGED-IN USER
    ===================================================== */

    function getSignedInUser() {

        if (window.currentUser) {
            return window.currentUser;
        }

        try {

            return JSON.parse(
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser") ||
                "null"
            );

        } catch (error) {

            console.error("Unable to read user:", error);
            return null;

        }

    }

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

        STORED_KEYS.forEach((key) => {

            sessionStorage.removeItem(key);
            localStorage.removeItem(key);

        });

    }

    /* =====================================================
       STUDENT PROFILE
    ===================================================== */

    function showUser(user) {

        const name =
            `${user.firstName || ""} ${user.lastName || ""}`.trim()
            || user.username
            || "Student";

        setText("welcomeName", name);
        setText("topbarName", name);
        setText("topbarInitials", initialsOf(name));

    }

    function initialsOf(name) {

        const parts = String(name).trim().split(/\s+/);

        if (parts.length === 1) {
            return parts[0].substring(0, 2).toUpperCase();
        }

        return (
            parts[0][0] +
            parts[parts.length - 1][0]
        ).toUpperCase();

    }

    /* =====================================================
       DASHBOARD DATA
    ===================================================== */

    async function loadDashboard(user) {

        const [
            bookings,
            facilities,
            notifications,
            timeSlots
        ] = await Promise.all([

            fetchList("booking"),
            fetchList("facility"),
            fetchList("notification"),
            fetchList("timeslot")

        ]);

        const myBookings = bookings.filter(
            (booking) => sameId(
                booking.userId,
                user.userId
            )
        );

        const myNotifications = notifications.filter(
            (notification) => sameId(
                notification.userId,
                user.userId
            )
        );

        const slotsById =
            indexBy(timeSlots, "timeSlotId");

        const facilitiesById =
            indexBy(facilities, "facilityId");

        const upcoming = myBookings

            .map((booking) => ({

                booking: booking,

                slot:
                    slotsById[booking.timeSlotId],

                facility:
                    facilitiesById[booking.facilityId]

            }))

            .filter((item) =>
                isUpcoming(item.booking, item.slot)
            )

            .sort((a, b) =>
                dateOf(a.slot) - dateOf(b.slot)
            );

        setText("totalBookings", myBookings.length);
        setText("upcomingBookings", upcoming.length);
        setText("availableFacilities", facilities.length);

        renderBookings(
            upcoming.slice(0, UPCOMING_LIMIT)
        );

        renderNotifications(myNotifications);

        // The shared script controls the bell.
        if (window.updateNotificationBadge) {
            window.updateNotificationBadge();
        }

    }

    async function fetchList(resource) {

        try {

            const response = await fetch(
                `${API_URL}/${resource}/all`,
                {
                    cache: "no-store"
                }
            );

            if (!response.ok) {

                throw new Error(
                    `Request failed: ${response.status}`
                );

            }

            const data = await response.json();

            return Array.isArray(data) ? data : [];

        } catch (error) {

            console.error(
                `Could not load ${resource}:`,
                error
            );

            return [];

        }

    }

    /* =====================================================
       UPCOMING BOOKINGS
    ===================================================== */

    function isUpcoming(booking, slot) {

        if (!slot || !slot.date) {
            return false;
        }

        const status = String(
            booking.bookingStatus || ""
        ).toUpperCase();

        if (
            status === "CANCELLED" ||
            status === "REJECTED"
        ) {
            return false;
        }

        const today = new Date();

        today.setHours(0, 0, 0, 0);

        return dateOf(slot) >= today;

    }

    function dateOf(slot) {

        const none = new Date(8640000000000000);

        if (!slot || !slot.date) {
            return none;
        }

        let parts;

        if (Array.isArray(slot.date)) {

            parts = slot.date.map(Number);

        } else {

            parts = String(slot.date)
                .split("T")[0]
                .split("-")
                .map(Number);

        }

        const [year, month, day] = parts;

        if (!year || !month || !day) {
            return none;
        }

        return new Date(year, month - 1, day);

    }

    function timeText(value) {

        if (
            value === undefined ||
            value === null ||
            value === ""
        ) {
            return "";
        }

        if (Array.isArray(value)) {

            return value.slice(0, 2)
                .map((number) =>
                    String(number).padStart(2, "0")
                )
                .join(":");

        }

        return String(value).slice(0, 5);

    }

    function renderBookings(items) {

        const list =
            document.getElementById("bookingList");

        const empty =
            document.getElementById("emptyBookings");

        if (!list) {
            return;
        }

        if (items.length === 0) {

            list.innerHTML = "";

            if (empty) {
                empty.style.display = "block";
            }

            return;

        }

        if (empty) {
            empty.style.display = "none";
        }

        list.innerHTML = items.map((item) => {

            const { booking, slot, facility } = item;

            const date = dateOf(slot);

            const month =
                MONTHS[date.getMonth()] || "";

            const day = String(
                date.getDate()
            ).padStart(2, "0");

            const facilityName =
                (
                    facility &&
                    (facility.name || facility.facilityName)
                ) ||
                (
                    booking.facility &&
                    booking.facility.name
                ) ||
                "Facility booking";

            const place =
                facility &&
                (facility.location || facility.building);

            const start =
                slot ? timeText(slot.startTime) : "";

            const end =
                slot ? timeText(slot.endTime) : "";

            const time =
                start && end
                    ? `${start} - ${end}`
                    : start;

            const status = String(
                booking.bookingStatus || "PENDING"
            ).toUpperCase();

            return `
                <div class="booking-item">

                    <div class="booking-date">
                        <span>${esc(month)}</span>
                        <strong>${esc(day)}</strong>
                    </div>

                    <div class="booking-info">

                        <strong>
                            ${esc(facilityName)}
                        </strong>

                        ${
                time
                    ? `<span>
                                    <i class="fa-regular fa-clock"></i>
                                    ${esc(time)}
                                   </span>`
                    : ""
            }

                        ${
                place
                    ? `<span>
                                    <i class="fa-solid fa-location-dot"></i>
                                    ${esc(place)}
                                   </span>`
                    : ""
            }

                    </div>

                    <span class="status-badge ${statusClass(status)}">
                        ${esc(titleCase(status))}
                    </span>

                </div>
            `;

        }).join("");

    }

    function statusClass(status) {

        if (
            status === "APPROVED" ||
            status === "CONFIRMED"
        ) {
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

        const list =
            document.getElementById("notificationList");

        const sorted = mine.slice().sort(
            (a, b) =>
                notificationDate(b) -
                notificationDate(a)
        );

        const unreadCount = mine.filter(
            (notification) =>
                readFlagOf(notification) === false
        ).length;

        // Dashboard statistics only.
        setText(
            "unreadNotifications",
            unreadCount
        );

        /*
           IMPORTANT:

           Do not update topbarNotificationCount
           directly here.

           notification-badge.js controls the
           unread bell count on every page.
        */

        if (!list) {
            return;
        }

        if (sorted.length === 0) {

            list.innerHTML = `
                <p class="empty-note">
                    You have no notifications.
                </p>
            `;

            return;

        }

        list.innerHTML = sorted
            .slice(0, NOTIFICATION_LIMIT)
            .map((notification) => {

                const title =
                    notification.title ||
                    notification.subject ||
                    notification.type ||
                    "Notification";

                const message =
                    notification.message ||
                    notification.description ||
                    notification.content ||
                    "";

                const when =
                    notificationDate(notification);

                const unread =
                    readFlagOf(notification) === false;

                return `
                    <div class="notification-item">

                        <div class="notification-icon system">
                            <i class="fa-solid fa-circle-info"></i>
                        </div>

                        <div class="notification-content">

                            <strong>
                                ${esc(title)}
                            </strong>

                            ${
                    message
                        ? `<p>${esc(message)}</p>`
                        : ""
                }

                            ${
                    when > 0
                        ? `<span>
                                        ${esc(
                            new Date(when).toLocaleString(
                                "en-ZA",
                                {
                                    dateStyle: "medium",
                                    timeStyle: "short"
                                }
                            )
                        )}
                                       </span>`
                        : ""
                }

                        </div>

                        ${
                    unread
                        ? `<span class="unread-dot"></span>`
                        : ""
                }

                    </div>
                `;

            })
            .join("");

    }

    function notificationDate(notification) {

        const raw =
            notification.createdAt ||
            notification.dateTime ||
            notification.timestamp ||
            notification.date ||
            notification.sentDate;

        const time =
            raw ? new Date(raw).getTime() : 0;

        return Number.isNaN(time) ? 0 : time;

    }

    function readFlagOf(notification) {

        const value =
            notification.read ??
            notification.isRead ??
            notification.seen;

        if (value === true || value === 1) {
            return true;
        }

        if (value === false || value === 0) {
            return false;
        }

        if (String(value).toLowerCase() === "true") {
            return true;
        }

        if (String(value).toLowerCase() === "false") {
            return false;
        }

        return undefined;

    }

    /* =====================================================
       HELPER FUNCTIONS
    ===================================================== */

    function sameId(a, b) {

        return (
            a !== undefined &&
            a !== null &&
            String(a) === String(b)
        );

    }

    function indexBy(list, key) {

        const map = {};

        list.forEach((item) => {
            map[item[key]] = item;
        });

        return map;

    }

    function setText(elementId, value) {

        const element =
            document.getElementById(elementId);

        if (element) {
            element.textContent = value;
        }

    }

    function titleCase(text) {

        return (
            text.charAt(0) +
            text.slice(1).toLowerCase()
        );

    }

    function esc(value) {

        return String(value).replace(
            /[&<>"']/g,
            (character) => ({

                "&": "&amp;",
                "<": "&lt;",
                ">": "&gt;",
                '"': "&quot;",
                "'": "&#39;"

            }[character])
        );

    }

})();
