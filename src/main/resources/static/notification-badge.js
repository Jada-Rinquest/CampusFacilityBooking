
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   SHARED NOTIFICATION BADGE
========================================================= */

(function () {

    "use strict";

    const API_URL = "http://localhost:8080/notification/all";

    /* =====================================================
       GET LOGGED-IN USER
    ===================================================== */

    function getLoggedInUser() {

        if (window.currentUser) {
            return window.currentUser;
        }

        try {
            const storedUser =
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser");

            return storedUser ? JSON.parse(storedUser) : null;

        } catch (error) {
            console.error("Error reading user:", error);
            return null;
        }
    }

    /* =====================================================
       CHECK IF NOTIFICATION IS UNREAD
    ===================================================== */

    function isUnread(notification) {

        const readValue = notification.read;

        return (
            readValue === false ||
            readValue === 0 ||
            String(readValue).toLowerCase() === "false"
        );
    }

    /* =====================================================
       HIDE NOTIFICATION BADGES
    ===================================================== */

    function hideBadges() {

        document.querySelectorAll(".notification-count")
            .forEach(function (badge) {

                badge.textContent = "";
                badge.style.display = "none";

            });
    }

    /* =====================================================
       UPDATE NOTIFICATION BADGES
    ===================================================== */

    async function updateNotificationBadge() {

        hideBadges();

        const user = getLoggedInUser();

        if (!user || !user.userId) {
            return;
        }

        try {

            const response = await fetch(API_URL, {
                method: "GET",
                cache: "no-store"
            });

            if (!response.ok) {
                throw new Error(
                    "Failed to load notifications: " +
                    response.status
                );
            }

            const notifications = await response.json();

            if (!Array.isArray(notifications)) {
                throw new Error("Invalid notification response");
            }

            const unreadCount = notifications.filter(
                function (notification) {

                    const belongsToUser =
                        String(notification.userId) ===
                        String(user.userId);

                    return belongsToUser && isUnread(notification);
                }
            ).length;

            document.querySelectorAll(".notification-count")
                .forEach(function (badge) {

                    if (unreadCount > 0) {

                        badge.textContent = String(unreadCount);
                        badge.style.display = "flex";

                    } else {

                        badge.textContent = "";
                        badge.style.display = "none";

                    }

                });

        } catch (error) {

            console.error(
                "Notification badge error:",
                error
            );

            hideBadges();
        }
    }

    /* =====================================================
       INITIALIZE
    ===================================================== */

    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            updateNotificationBadge
        );

    } else {

        updateNotificationBadge();

    }

    /* =====================================================
       MAKE FUNCTION AVAILABLE TO OTHER FILES
    ===================================================== */

    window.updateNotificationBadge =
        updateNotificationBadge;

})();
