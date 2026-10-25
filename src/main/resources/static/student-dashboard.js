/* =========================================================
   CAMPUS FACILITY BOOKING
   STUDENT DASHBOARD JAVASCRIPT
========================================================= */

const API_BASE_URL = "http://localhost:8080";

const BOOKING_API = `${API_BASE_URL}/booking`;
const FACILITY_API = `${API_BASE_URL}/facility`;
const NOTIFICATION_API = `${API_BASE_URL}/notification`;
const TIMESLOT_API = `${API_BASE_URL}/timeslot`;

const welcomeName = document.getElementById("welcomeName");
const topbarName = document.getElementById("topbarName");
const topbarInitials = document.getElementById("topbarInitials");

const totalBookings = document.getElementById("totalBookings");
const upcomingBookings = document.getElementById("upcomingBookings");
const availableFacilities = document.getElementById("availableFacilities");
const unreadNotifications = document.getElementById("unreadNotifications");

const emptyBookings = document.getElementById("emptyBookings");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


document.addEventListener("DOMContentLoaded", () => {

    loadSignedInUser();

    loadDashboard();

    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener("click", () => sidebar.classList.toggle("open"));
    }

});


/* =========================================================
   SIGNED IN USER
========================================================= */

function loadSignedInUser() {

    let user = null;

    // script.js stores the user in localStorage when "Remember me" is ticked
    try {
        user = JSON.parse(
            sessionStorage.getItem("loggedInUser") ||
            localStorage.getItem("loggedInUser")
        );
    } catch (error) {
        user = null;
    }

    const name = user && user.firstName
        ? `${user.firstName} ${user.lastName || ""}`.trim()
        : "Student";

    if (welcomeName) {
        welcomeName.textContent = name;
    }

    if (topbarName) {
        topbarName.textContent = name;
    }

    if (topbarInitials) {
        topbarInitials.textContent = initialsOf(name);
    }

    return user;

}


function initialsOf(name) {

    const parts = String(name).trim().split(/\s+/);

    if (parts.length === 1) {
        return parts[0].substring(0, 2).toUpperCase();
    }

    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();

}


/* =========================================================
   DASHBOARD DATA
========================================================= */

async function loadDashboard() {

    const user = loadSignedInUser();
    const userId = user ? user.userId : null;

    const bookings = await safeFetch(`${BOOKING_API}/all`);
    const facilities = await safeFetch(`${FACILITY_API}/all`);
    const notifications = await safeFetch(`${NOTIFICATION_API}/all`);
    const timeSlots = await safeFetch(`${TIMESLOT_API}/all`);

    const myBookings = userId
        ? bookings.filter((b) => b.userId === userId)
        : bookings;

    // A booking's date lives on its time slot
    const slotDates = {};
    timeSlots.forEach((t) => { slotDates[t.timeSlotId] = t.date; });

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const upcoming = myBookings.filter((b) => {

        const date = slotDates[b.timeSlotId];

        if (!date || b.bookingStatus === "CANCELLED" || b.bookingStatus === "REJECTED") {
            return false;
        }

        const d = new Date(date);
        d.setHours(0, 0, 0, 0);

        return d >= today;

    });

    const myNotifications = userId
        ? notifications.filter((n) => n.userId === userId)
        : notifications;

    setText(totalBookings, myBookings.length);
    setText(upcomingBookings, upcoming.length);
    setText(availableFacilities, facilities.length);
    setText(unreadNotifications, myNotifications.length);

    if (emptyBookings) {
        emptyBookings.style.display = myBookings.length === 0 ? "block" : "none";
    }

}


async function safeFetch(url) {

    try {

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Request failed: ${response.status}`);
        }

        const data = await response.json();

        return Array.isArray(data) ? data : [];

    } catch (error) {

        console.error("Dashboard request failed:", url, error);

        return [];

    }

}


function setText(element, value) {

    if (element) {
        element.textContent = value;
    }

}
