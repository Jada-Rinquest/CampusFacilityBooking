
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   SHARED NOTIFICATION BADGE

   STAFF:
   Shows all unread notifications in the system.

   STUDENT:
   Shows only unread notifications belonging
   to the logged-in student.
========================================================= */

(function () {
    "use strict";

    const API_URL = "http://localhost:8080/notification/all";

    let requestNumber = 0;

    /* =====================================================
       GET LOGGED-IN USER
    ===================================================== */

    function getLoggedInUser() {
        if (window.currentUser?.userId) {
            return window.currentUser;
        }

        try {
            const storedUser =
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser");

            return storedUser ? JSON.parse(storedUser) : null;

        } catch (error) {
            console.error("Unable to read logged-in user:", error);
            return null;
        }
    }

    /* =====================================================
       CHECK USER ROLE
    ===================================================== */

    function isStaff(user) {
        if (!user) return false;

        const role = String(
            user.accountType ||
            user.role ||
            user.userRole ||
            ""
        ).toUpperCase();

        if (role === "STAFF" || role === "ADMIN") {
            return true;
        }

        if (role === "STUDENT") {
            return false;
        }

        return user.isStudent === false;
    }

    /* =====================================================
       CHECK NOTIFICATION OWNERSHIP
    ===================================================== */

    function belongsToUser(notification, userId) {
        const recipientId =
            notification.userId ??
            notification.recipientId ??
            notification.user?.userId ??
            notification.recipient?.userId;

        return recipientId != null &&
            String(recipientId).trim() ===
            String(userId).trim();
    }

    /* =====================================================
       CHECK UNREAD STATUS
    ===================================================== */

    function isUnread(notification) {
        const readValue =
            notification.read ??
            notification.isRead ??
            notification.is_read ??
            notification.readStatus;

        if (readValue == null) {
            return false;
        }

        if (typeof readValue === "boolean") {
            return !readValue;
        }

        if (typeof readValue === "number") {
            return readValue === 0;
        }

        return [
            "false",
            "0",
            "unread",
            "not_read"
        ].includes(
            String(readValue).trim().toLowerCase()
        );
    }

    /* =====================================================
       DISPLAY BADGE COUNT
    ===================================================== */

    function displayBadgeCount(count) {
        document.querySelectorAll(".notification-count")
            .forEach(function (badge) {

                if (count > 0) {
                    badge.textContent =
                        count > 99 ? "99+" : String(count);

                    badge.hidden = false;
                    badge.style.display = "flex";

                } else {
                    badge.textContent = "";
                    badge.hidden = true;
                    badge.style.display = "none";
                }
            });
    }

    /* =====================================================
       LOAD NOTIFICATIONS
    ===================================================== */

    async function updateNotificationBadge() {
        const currentRequest = ++requestNumber;

        displayBadgeCount(0);

        const user = getLoggedInUser();

        if (!user?.userId) {
            return;
        }

        const userId = String(user.userId);
        const staffAccount = isStaff(user);

        try {
            const response = await fetch(API_URL, {
                method: "GET",
                cache: "no-store"
            });

            if (!response.ok) {
                throw new Error(
                    "Unable to load notifications: " +
                    response.status
                );
            }

            const notifications = await response.json();

            if (!Array.isArray(notifications)) {
                throw new Error(
                    "Notification API did not return a list."
                );
            }

            if (currentRequest !== requestNumber) {
                return;
            }

            const latestUser = getLoggedInUser();

            if (
                !latestUser?.userId ||
                String(latestUser.userId) !== userId
            ) {
                displayBadgeCount(0);
                return;
            }

            const unreadCount = notifications.filter(
                function (notification) {

                    if (!isUnread(notification)) {
                        return false;
                    }

                    // Staff count every unread notification.
                    if (staffAccount) {
                        return true;
                    }

                    // Students count only their own.
                    return belongsToUser(
                        notification,
                        userId
                    );
                }
            ).length;

            displayBadgeCount(unreadCount);

        } catch (error) {
            console.error(
                "Notification badge error:",
                error
            );

            if (currentRequest === requestNumber) {
                displayBadgeCount(0);
            }
        }
    }

    /* =====================================================
       INITIALISE
    ===================================================== */

    function initialise() {
        updateNotificationBadge();
    }

    if (document.readyState === "loading") {
        document.addEventListener(
            "DOMContentLoaded",
            initialise
        );
    } else {
        initialise();
    }

    /* =====================================================
       REFRESH WHEN RETURNING TO PAGE
    ===================================================== */

    window.addEventListener(
        "pageshow",
        updateNotificationBadge
    );

    document.addEventListener(
        "visibilitychange",
        function () {
            if (!document.hidden) {
                updateNotificationBadge();
            }
        }
    );

    /* =====================================================
       ALLOW OTHER SCRIPTS TO REFRESH BADGE
    ===================================================== */

    window.updateNotificationBadge =
        updateNotificationBadge;

})();
