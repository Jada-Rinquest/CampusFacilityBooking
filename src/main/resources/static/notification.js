document.addEventListener("DOMContentLoaded", function () {

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

    const totalNotifications =
        document.getElementById("totalNotifications");

    const sentToday =
        document.getElementById("sentToday");

    const bookingNotifications =
        document.getElementById("bookingNotifications");

    const systemNotifications =
        document.getElementById("systemNotifications");


    /* =========================================================
       OPEN SEND NOTIFICATION MODAL
    ========================================================= */

    if (openNotificationModal) {

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
       SEND NOTIFICATION
    ========================================================= */

    if (notificationForm) {

        notificationForm.addEventListener("submit", function (event) {

            event.preventDefault();

            const recipient =
                document.getElementById("recipient").value;

            const notificationType =
                document.getElementById("notificationType").value;

            const message =
                document.getElementById("notificationMessage").value.trim();


            /* Validate form */

            if (!recipient ||
                !notificationType ||
                !message) {

                alert("Please complete all required fields.");

                return;
            }


            /* Generate a temporary notification ID */

            const notificationId =
                "NT" + Date.now();


            /* Current date */

            const currentDate =
                new Date().toLocaleDateString("en-GB", {
                    day: "2-digit",
                    month: "short",
                    year: "numeric"
                });


            /* Get recipient name */

            const recipientSelect =
                document.getElementById("recipient");

            const recipientName =
                recipientSelect.options[
                    recipientSelect.selectedIndex
                ].text;


            /* Get notification type name */

            const typeSelect =
                document.getElementById("notificationType");

            const typeName =
                typeSelect.options[
                    typeSelect.selectedIndex
                ].text;


            /* Add notification to table */

            addNotificationToTable(
                notificationId,
                recipientName,
                recipient,
                typeName,
                notificationType,
                currentDate,
                message
            );


            /* Update statistics */

            updateStatistics(notificationType);


            /* Close modal */

            closeNotification();


            /* Success message */

            alert("Notification sent successfully!");

        });

    }


    /* =========================================================
       ADD NOTIFICATION TO TABLE
    ========================================================= */

    function addNotificationToTable(
        notificationId,
        recipientName,
        recipientId,
        typeName,
        typeValue,
        date,
        message
    ) {

        if (!notificationTableBody) {
            return;
        }


        /* Determine icon */

        let icon = "fa-bell";

        if (typeValue === "booking") {
            icon = "fa-calendar-check";
        }

        else if (typeValue === "maintenance") {
            icon = "fa-screwdriver-wrench";
        }

        else if (typeValue === "system") {
            icon = "fa-circle-info";
        }

        else if (typeValue === "reminder") {
            icon = "fa-clock";
        }

        else if (typeValue === "announcement") {
            icon = "fa-bullhorn";
        }


        /* Create row */

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>

                <div class="notification-message">

                    <div class="notification-row-icon ${typeValue}-icon">

                        <i class="fa-solid ${icon}"></i>

                    </div>

                    <div>

                        <strong>
                            New ${typeName} Notification
                        </strong>

                        <span>
                            ${escapeHTML(message)}
                        </span>

                    </div>

                </div>

            </td>


            <td>

                <div class="recipient">

                    <div class="recipient-avatar">
                        ${getInitials(recipientName)}
                    </div>

                    <span>
                        ${escapeHTML(recipientName)}
                    </span>

                </div>

            </td>


            <td>

                <span class="type-badge ${typeValue}">
                    ${escapeHTML(typeName)}
                </span>

            </td>


            <td>

                <span class="date">
                    ${date}
                </span>

            </td>


            <td>

                <div class="action-buttons">

                    <button
                        class="action-btn view-btn"
                        title="View notification"
                        data-title="New ${escapeHTML(typeName)} Notification"
                        data-recipient="${escapeHTML(recipientName)}"
                        data-type="${escapeHTML(typeName)}"
                        data-date="${date}"
                        data-message="${escapeHTML(message)}">

                        <i class="fa-solid fa-eye"></i>

                    </button>


                    <button
                        class="action-btn delete-btn"
                        title="Delete notification">

                        <i class="fa-solid fa-trash"></i>

                    </button>

                </div>

            </td>

        `;


        /*
         * Insert the newest notification
         * at the top of the table.
         */

        notificationTableBody.prepend(row);


        /*
         * Attach buttons to the new row.
         */

        attachRowButtons(row);

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

                const title =
                    this.dataset.title;

                const recipient =
                    this.dataset.recipient;

                const type =
                    this.dataset.type;

                const date =
                    this.dataset.date;

                const message =
                    this.dataset.message;


                document.getElementById(
                    "viewNotificationTitle"
                ).textContent = title;


                document.getElementById(
                    "viewRecipient"
                ).textContent = recipient;


                document.getElementById(
                    "viewType"
                ).textContent = type;


                document.getElementById(
                    "viewDate"
                ).textContent = date;


                document.getElementById(
                    "viewMessage"
                ).textContent = message;


                viewNotificationModal.classList.add("active");

                document.body.classList.add("modal-open");

            });

        }


        /* DELETE */

        if (deleteButton) {

            deleteButton.addEventListener(
                "click",
                function () {

                    const confirmed =
                        confirm(
                            "Are you sure you want to delete this notification?"
                        );


                    if (confirmed) {

                        row.remove();

                        updateTotalCount(-1);

                    }

                }
            );

        }

    }


    /* =========================================================
       ATTACH BUTTONS TO EXISTING NOTIFICATIONS
    ========================================================= */

    if (notificationTableBody) {

        const rows =
            notificationTableBody.querySelectorAll("tr");

        rows.forEach(function (row) {

            attachRowButtons(row);

        });

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

        const searchTerm =
            notificationSearch.value
                .toLowerCase()
                .trim();

        const selectedType =
            notificationTypeFilter.value
                .toLowerCase();


        const rows =
            notificationTableBody.querySelectorAll("tr");


        rows.forEach(function (row) {

            const rowText =
                row.textContent.toLowerCase();


            const matchesSearch =
                rowText.includes(searchTerm);


            const badge =
                row.querySelector(".type-badge");


            let matchesType = true;


            if (selectedType && badge) {

                matchesType =
                    badge.classList.contains(selectedType);

            }


            if (matchesSearch && matchesType) {

                row.style.display = "";

            }

            else {

                row.style.display = "none";

            }

        });

    }


    /* =========================================================
       UPDATE STATISTICS
    ========================================================= */

    function updateStatistics(type) {

        updateTotalCount(1);


        if (sentToday) {

            sentToday.textContent =
                parseInt(sentToday.textContent) + 1;

        }


        if (type === "booking" && bookingNotifications) {

            bookingNotifications.textContent =
                parseInt(
                    bookingNotifications.textContent
                ) + 1;

        }


        if (type === "system" && systemNotifications) {

            systemNotifications.textContent =
                parseInt(
                    systemNotifications.textContent
                ) + 1;

        }

    }


    function updateTotalCount(amount) {

        if (totalNotifications) {

            totalNotifications.textContent =
                parseInt(
                    totalNotifications.textContent
                ) + amount;

        }

    }


    /* =========================================================
       GET INITIALS
    ========================================================= */

    function getInitials(name) {

        if (!name) {
            return "U";
        }


        if (name === "All Users") {
            return "AL";
        }


        const words =
            name.trim().split(" ");


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

        if (!value) {
            return "";
        }


        return value
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