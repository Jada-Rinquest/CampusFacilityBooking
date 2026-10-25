document.addEventListener("DOMContentLoaded", function () {

    /* =========================================================
       API CONFIGURATION
    ========================================================= */

    const API_BASE_URL = "http://localhost:8080";

    const NOTIFICATION_API = `${API_BASE_URL}/notification`;
    const USER_API = `${API_BASE_URL}/user`;


    /* =========================================================
       SIGNED-IN USER (set by auth.js)
       Students only see their own notifications and cannot
       send or delete any. Staff see and manage all of them.
    ========================================================= */

    const currentUser = window.currentUser || null;

    const isStudent = !currentUser || currentUser.isStudent;


    /* =========================================================
       NOTIFICATION TYPES (NotificationType enum)
    ========================================================= */

    const TYPES = {

        BOOKING_CONFIRMATION: {
            name: "Booking",
            cssClass: "booking",
            icon: "fa-calendar-check"
        },

        MAINTENANCE_UPDATE: {
            name: "Maintenance",
            cssClass: "maintenance",
            icon: "fa-screwdriver-wrench"
        },

        INVOICE_REMINDER: {
            name: "Invoice Reminder",
            cssClass: "reminder",
            icon: "fa-file-invoice-dollar"
        }

    };


    /* =========================================================
       ELEMENTS
    ========================================================= */

    const openNotificationModal =
        document.getElementById("openNotificationModal");

    const notificationModal =
        document.getElementById("notificationModal");

    const closeNotificationModal =
        document.getElementById("closeNotificationModal");

    const cancelNotification =
        document.getElementById("cancelNotification");

    const notificationForm =
        document.getElementById("notificationForm");

    const viewNotificationModal =
        document.getElementById("viewNotificationModal");

    const closeViewModal =
        document.getElementById("closeViewModal");

    const notificationSearch =
        document.getElementById("notificationSearch");

    const notificationTypeFilter =
        document.getElementById("notificationTypeFilter");

    const notificationMessage =
        document.getElementById("notificationMessage");

    const characterCount =
        document.getElementById("characterCount");

    const notificationTableBody =
        document.getElementById("notificationTableBody");

    const recipientSelect =
        document.getElementById("recipient");

    const totalNotifications =
        document.getElementById("totalNotifications");

    const sentToday =
        document.getElementById("sentToday");

    const bookingNotifications =
        document.getElementById("bookingNotifications");

    const maintenanceNotifications =
        document.getElementById("maintenanceNotifications");


    /* =========================================================
       STATE
    ========================================================= */

    let notifications = [];

    // userId -> "First Last"
    let userNames = {};


    /* =========================================================
       PAGE SET-UP FOR STUDENTS
    ========================================================= */

    if (isStudent) {

        if (openNotificationModal) {
            openNotificationModal.remove();
        }

        setText("pageTitle", "My Notifications");

        setText(
            "pageDescription",
            "Updates about your bookings, maintenance requests and invoices."
        );

        setText(
            "panelDescription",
            "Notifications sent to you through the campus booking system."
        );

    }


    /* =========================================================
       LOAD DATA
    ========================================================= */

    loadPage();


    async function loadPage() {

        await loadUsers();

        await loadNotifications();

    }


    async function loadUsers() {

        // Students never load the list of other users
        if (isStudent) {

            if (currentUser) {
                userNames[currentUser.userId] = currentUser.fullName;
            }

            return;
        }

        try {

            const response = await fetch(`${USER_API}/all`);

            if (!response.ok) {
                throw new Error(`User request failed: ${response.status}`);
            }

            const users = await response.json();

            users.forEach(function (user) {
                userNames[user.userId] =
                    `${user.firstName} ${user.lastName}`.trim();
            });

            populateRecipients(users);

        } catch (error) {

            console.error("Error loading users:", error);

        }

    }


    async function loadNotifications() {

        try {

            const response = await fetch(`${NOTIFICATION_API}/all`);

            if (!response.ok) {
                throw new Error(`Notification request failed: ${response.status}`);
            }

            notifications = await response.json();

            if (isStudent) {

                notifications = notifications.filter(function (notification) {
                    return currentUser && notification.userId === currentUser.userId;
                });

            }

        } catch (error) {

            console.error("Error loading notifications:", error);

            notifications = [];

        }

        renderNotifications();

    }


    function populateRecipients(users) {

        if (!recipientSelect) {
            return;
        }

        recipientSelect.innerHTML =
            '<option value="">Select recipient</option>';

        users.forEach(function (user) {

            const option = document.createElement("option");

            option.value = user.userId;

            option.textContent =
                `${user.firstName} ${user.lastName} (${user.email})`;

            recipientSelect.appendChild(option);

        });

        const allOption = document.createElement("option");

        allOption.value = "all";

        allOption.textContent = "All Users";

        recipientSelect.appendChild(allOption);

    }


    /* =========================================================
       RENDER TABLE + STATISTICS
    ========================================================= */

    function renderNotifications() {

        if (!notificationTableBody) {
            return;
        }

        notificationTableBody.innerHTML = "";

        // Newest first
        const sorted = notifications.slice().sort(function (a, b) {
            return String(b.sentDate).localeCompare(String(a.sentDate));
        });

        if (sorted.length === 0) {

            notificationTableBody.innerHTML = `
                <tr>
                    <td colspan="5" style="text-align: center; padding: 30px;">
                        ${isStudent ? "You have no notifications yet." : "No notifications found."}
                    </td>
                </tr>
            `;

        }

        sorted.forEach(function (notification) {

            notificationTableBody.appendChild(
                createNotificationRow(notification)
            );

        });

        updateStatistics();

        filterNotifications();

    }


    function createNotificationRow(notification) {

        const type =
            TYPES[notification.notificationType] ||
            { name: "General", cssClass: "system", icon: "fa-bell" };

        const recipientName =
            userNames[notification.userId] || notification.userId;

        const title = `${type.name} Notification`;

        const date = formatDate(notification.sentDate);

        const row = document.createElement("tr");

        row.innerHTML = `

            <td>

                <div class="notification-message">

                    <div class="notification-row-icon ${type.cssClass}-icon">

                        <i class="fa-solid ${type.icon}"></i>

                    </div>

                    <div>

                        <strong>
                            ${escapeHTML(title)}
                        </strong>

                        <span>
                            ${escapeHTML(notification.message)}
                        </span>

                    </div>

                </div>

            </td>


            <td>

                <div class="recipient">

                    <div class="recipient-avatar">
                        ${escapeHTML(getInitials(recipientName))}
                    </div>

                    <span>
                        ${escapeHTML(recipientName)}
                    </span>

                </div>

            </td>


            <td>

                <span class="type-badge ${type.cssClass}">
                    ${escapeHTML(type.name)}
                </span>

            </td>


            <td>

                <span class="date">
                    ${escapeHTML(date)}
                </span>

            </td>


            <td>

                <div class="action-buttons">

                    <button
                        class="action-btn view-btn"
                        title="View notification"
                        data-title="${escapeHTML(title)}"
                        data-recipient="${escapeHTML(recipientName)}"
                        data-type="${escapeHTML(type.name)}"
                        data-date="${escapeHTML(date)}"
                        data-message="${escapeHTML(notification.message)}">

                        <i class="fa-solid fa-eye"></i>

                    </button>

                    ${isStudent ? "" : `
                    <button
                        class="action-btn delete-btn"
                        title="Delete notification"
                        data-id="${escapeHTML(notification.notificationId)}">

                        <i class="fa-solid fa-trash"></i>

                    </button>`}

                </div>

            </td>

        `;

        attachRowButtons(row);

        return row;

    }


    function updateStatistics() {

        const today = todayIso();

        setCount(totalNotifications, notifications.length);

        setCount(
            sentToday,
            notifications.filter(function (n) {
                return n.sentDate === today;
            }).length
        );

        setCount(
            bookingNotifications,
            notifications.filter(function (n) {
                return n.notificationType === "BOOKING_CONFIRMATION";
            }).length
        );

        setCount(
            maintenanceNotifications,
            notifications.filter(function (n) {
                return n.notificationType === "MAINTENANCE_UPDATE";
            }).length
        );

    }


    /* =========================================================
       OPEN SEND NOTIFICATION MODAL
    ========================================================= */

    if (openNotificationModal && !isStudent) {

        openNotificationModal.addEventListener("click", function () {

            notificationModal.classList.add("active");

            document.body.classList.add("modal-open");

        });

    }


    /* =========================================================
       CLOSE SEND NOTIFICATION MODAL
    ========================================================= */

    function closeNotification() {

        notificationModal.classList.remove("active");

        document.body.classList.remove("modal-open");

        if (notificationForm) {
            notificationForm.reset();
        }

        if (characterCount) {
            characterCount.textContent = "0";
        }
    }


    if (closeNotificationModal) {

        closeNotificationModal.addEventListener(
            "click",
            closeNotification
        );

    }


    if (cancelNotification) {

        cancelNotification.addEventListener(
            "click",
            closeNotification
        );

    }


    /* =========================================================
       CLOSE MODAL WHEN CLICKING OUTSIDE
    ========================================================= */

    if (notificationModal) {

        notificationModal.addEventListener("click", function (event) {

            if (event.target === notificationModal) {

                closeNotification();

            }

        });

    }


    /* =========================================================
       CHARACTER COUNT
    ========================================================= */

    if (notificationMessage && characterCount) {

        notificationMessage.addEventListener("input", function () {

            characterCount.textContent =
                notificationMessage.value.length;

        });

    }


    /* =========================================================
       SEND NOTIFICATION (staff only)
    ========================================================= */

    if (notificationForm) {

        notificationForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            if (isStudent) {
                return;
            }

            const recipient =
                recipientSelect.value;

            const notificationType =
                document.getElementById("notificationType").value;

            const message =
                notificationMessage.value.trim();


            /* Validate form */

            if (!recipient ||
                !notificationType ||
                !message) {

                alert("Please complete all required fields.");

                return;
            }


            /* "All Users" sends one notification to every user */

            const recipients = recipient === "all"
                ? Object.keys(userNames)
                : [recipient];

            const stamp = Date.now();

            try {

                for (let i = 0; i < recipients.length; i++) {

                    const response = await fetch(`${NOTIFICATION_API}/create`, {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            notificationId: `NT${stamp}${i}`,
                            userId: recipients[i],
                            message: message,
                            sentDate: todayIso(),
                            notificationType: notificationType
                        })
                    });

                    if (!response.ok) {
                        throw new Error(`Server error: ${response.status}`);
                    }

                }

                closeNotification();

                await loadNotifications();

                alert("Notification sent successfully!");

            } catch (error) {

                console.error("Error sending notification:", error);

                alert("The notification could not be sent. Please try again.");

            }

        });

    }


    /* =========================================================
       VIEW + DELETE BUTTONS
    ========================================================= */

    function attachRowButtons(row) {

        const viewButton =
            row.querySelector(".view-btn");

        const deleteButton =
            row.querySelector(".delete-btn");


        /* VIEW */

        if (viewButton) {

            viewButton.addEventListener("click", function () {

                document.getElementById(
                    "viewNotificationTitle"
                ).textContent = this.dataset.title;


                document.getElementById(
                    "viewRecipient"
                ).textContent = this.dataset.recipient;


                document.getElementById(
                    "viewType"
                ).textContent = this.dataset.type;


                document.getElementById(
                    "viewDate"
                ).textContent = this.dataset.date;


                document.getElementById(
                    "viewMessage"
                ).textContent = this.dataset.message;


                viewNotificationModal.classList.add("active");

                document.body.classList.add("modal-open");

            });

        }


        /* DELETE (staff only - students get no delete button) */

        if (deleteButton) {

            deleteButton.addEventListener(
                "click",
                async function () {

                    const confirmed =
                        confirm(
                            "Are you sure you want to delete this notification?"
                        );

                    if (!confirmed) {
                        return;
                    }

                    try {

                        const response = await fetch(
                            `${NOTIFICATION_API}/delete/${encodeURIComponent(this.dataset.id)}`,
                            { method: "DELETE" }
                        );

                        if (!response.ok) {
                            throw new Error(`Delete failed: ${response.status}`);
                        }

                        await loadNotifications();

                    } catch (error) {

                        console.error("Error deleting notification:", error);

                        alert("The notification could not be deleted.");

                    }

                }
            );

        }

    }


    /* =========================================================
       CLOSE VIEW MODAL
    ========================================================= */

    if (closeViewModal) {

        closeViewModal.addEventListener(
            "click",
            function () {

                viewNotificationModal.classList.remove("active");

                document.body.classList.remove("modal-open");

            }
        );

    }


    /* =========================================================
       CLOSE VIEW MODAL WHEN CLICKING OUTSIDE
    ========================================================= */

    if (viewNotificationModal) {

        viewNotificationModal.addEventListener(
            "click",
            function (event) {

                if (event.target === viewNotificationModal) {

                    viewNotificationModal.classList.remove("active");

                    document.body.classList.remove("modal-open");

                }

            }
        );

    }


    /* =========================================================
       SEARCH NOTIFICATIONS
    ========================================================= */

    if (notificationSearch) {

        notificationSearch.addEventListener(
            "input",
            filterNotifications
        );

    }


    /* =========================================================
       FILTER BY TYPE
    ========================================================= */

    if (notificationTypeFilter) {

        notificationTypeFilter.addEventListener(
            "change",
            filterNotifications
        );

    }


    function filterNotifications() {

        if (!notificationTableBody) {
            return;
        }

        const searchTerm = notificationSearch
            ? notificationSearch.value.toLowerCase().trim()
            : "";

        const selectedType = notificationTypeFilter
            ? notificationTypeFilter.value.toLowerCase()
            : "";


        const rows =
            notificationTableBody.querySelectorAll("tr");


        rows.forEach(function (row) {

            const badge =
                row.querySelector(".type-badge");

            // The "no notifications" row has no badge
            if (!badge) {
                return;
            }

            const rowText =
                row.textContent.toLowerCase();


            const matchesSearch =
                rowText.includes(searchTerm);


            const matchesType =
                !selectedType ||
                badge.classList.contains(selectedType);


            if (matchesSearch && matchesType) {

                row.style.display = "";

            }

            else {

                row.style.display = "none";

            }

        });

    }


    /* =========================================================
       HELPERS
    ========================================================= */

    function setText(id, value) {

        const element = document.getElementById(id);

        if (element) {
            element.textContent = value;
        }

    }


    function setCount(element, value) {

        if (element) {
            element.textContent = value;
        }

    }


    // Local date as YYYY-MM-DD (what the backend's LocalDate expects)
    function todayIso() {

        const now = new Date();

        const month = String(now.getMonth() + 1).padStart(2, "0");

        const day = String(now.getDate()).padStart(2, "0");

        return `${now.getFullYear()}-${month}-${day}`;

    }


    function formatDate(isoDate) {

        if (!isoDate) {
            return "";
        }

        const date = new Date(`${isoDate}T00:00:00`);

        return date.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });

    }


    /* =========================================================
       GET INITIALS
    ========================================================= */

    function getInitials(name) {

        if (!name) {
            return "U";
        }


        const words =
            String(name).trim().split(/\s+/);


        if (words.length === 1) {

            return words[0]
                .substring(0, 2)
                .toUpperCase();

        }


        return (
            words[0].charAt(0) +
            words[words.length - 1].charAt(0)
        ).toUpperCase();

    }


    /* =========================================================
       SECURITY HELPER
       Prevent HTML being inserted directly from user input.
    ========================================================= */

    function escapeHTML(value) {

        if (value === null || value === undefined) {
            return "";
        }


        return String(value)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");

    }


    /* =========================================================
       ESC KEY CLOSES MODALS
    ========================================================= */

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape") {

                if (
                    notificationModal &&
                    notificationModal.classList.contains("active")
                ) {

                    closeNotification();

                }


                if (
                    viewNotificationModal &&
                    viewNotificationModal.classList.contains("active")
                ) {

                    viewNotificationModal.classList.remove("active");

                    document.body.classList.remove("modal-open");

                }

            }

        }
    );


    /* =========================================================
       MOBILE SIDEBAR
    ========================================================= */

    const mobileMenuBtn =
        document.getElementById("mobileMenuBtn");

    const sidebar =
        document.querySelector(".sidebar");


    if (mobileMenuBtn && sidebar) {

        mobileMenuBtn.addEventListener(
            "click",
            function () {

                sidebar.classList.toggle("open");

            }
        );

    }


});
