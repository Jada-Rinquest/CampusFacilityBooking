
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   AUTH.JS - UPDATED VERSION
   Shared Login, Session and Role Permissions
========================================================= */

(function () {
    "use strict";

    /* =====================================================
       PAGE PERMISSIONS
    ===================================================== */

    const STAFF_ONLY_PAGES = [
        "user.html",
        "invoice.html",
        "equipment.html",
        "maintenance.html",
        "staff-dashboard.html",
        "facility.html"
    ];

    const STUDENT_ONLY_PAGES = [
        "student-dashboard.html",
        "student-facility.html"
    ];

    /* =====================================================
       READ LOGGED-IN USER
    ===================================================== */

    function readUser() {
        try {
            const storedUser =
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser");

            if (!storedUser) {
                return null;
            }

            return JSON.parse(storedUser);

        } catch (error) {
            console.error(
                "Unable to read logged-in user:",
                error
            );
            return null;
        }
    }

    /* =====================================================
       IDENTIFY ACCOUNT TYPE
    ===================================================== */

    function accountTypeOf(user) {
        if (user.accountType) {
            return String(user.accountType)
                .trim()
                .toUpperCase();
        }

        const email = String(user.email || "")
            .trim()
            .toLowerCase();

        if (email.endsWith("@cput.ac.za")) {
            return "STAFF";
        }

        return "STUDENT";
    }

    /* =====================================================
       SESSION HELPERS
    ===================================================== */

    function clearStoredUser() {
        [
            "loggedInUser",
            "loginId",
            "registrarId",
            "username"
        ].forEach(function (key) {
            sessionStorage.removeItem(key);
            localStorage.removeItem(key);
        });
    }

    function sessionExpired() {
        console.warn(
            "Your login session has expired."
        );

        clearStoredUser();

        window.location.replace("index.html");
    }

    /* =====================================================
       CHECK LOGIN
    ===================================================== */

    const user = readUser();

    if (!user || !user.userId) {
        console.warn(
            "No valid user information found."
        );

        window.location.replace("index.html");
        return;
    }

    /* =====================================================
       SERVER SESSION CHECK
    ===================================================== */

    const originalFetch = window.fetch.bind(window);

    // Do not log out because an unrelated API returns 401.
    // Authentication is checked separately through /auth/me.

    originalFetch("/auth/me", {
        method: "GET",
        credentials: "same-origin",
        cache: "no-store"
    })
        .then(function (response) {

            if (response.status === 401) {
                console.error(
                    "Server rejected the login session."
                );

                sessionExpired();
                return;
            }

            if (!response.ok) {
                console.error(
                    "Session verification failed:",
                    response.status
                );
                return;
            }

            console.log(
                "Login session verified successfully."
            );
        })
        .catch(function (error) {
            console.error(
                "Unable to verify server session:",
                error
            );
        });

    /* =====================================================
       DETERMINE USER ROLE
    ===================================================== */

    const accountType = accountTypeOf(user);
    const isStudent = accountType === "STUDENT";

    const page =
        window.location.pathname.split("/").pop() ||
        "index.html";

    /* =====================================================
       STUDENT PAGE RESTRICTIONS
    ===================================================== */

    if (isStudent && STAFF_ONLY_PAGES.includes(page)) {

        if (page === "facility.html") {
            window.location.replace(
                "student-facility.html"
            );
        } else {
            window.location.replace(
                "student-dashboard.html"
            );
        }

        return;
    }

    /* =====================================================
       STAFF PAGE RESTRICTIONS
    ===================================================== */

    if (!isStudent && STUDENT_ONLY_PAGES.includes(page)) {

        if (page === "student-facility.html") {
            window.location.replace("facility.html");
        } else {
            window.location.replace(
                "staff-dashboard.html"
            );
        }

        return;
    }

    /* =====================================================
       USER INFORMATION
    ===================================================== */

    const fullName =
        `${user.firstName || ""} ${user.lastName || ""}`.trim() ||
        user.username ||
        "CampusBooking User";

    const roleLabel = isStudent
        ? "Student"
        : (
            String(user.role || "").toUpperCase() === "ADMIN"
                ? "Administrator"
                : "Staff"
        );

    /* =====================================================
       MAKE CURRENT USER AVAILABLE
    ===================================================== */

    window.currentUser = {
        ...user,
        accountType: isStudent ? "STUDENT" : "STAFF",
        isStudent: isStudent,
        fullName: fullName,
        roleLabel: roleLabel
    };

    /* =====================================================
       HIDE STAFF-ONLY CONTROLS
    ===================================================== */

    if (isStudent) {

        document.documentElement.classList.add(
            "is-student"
        );

        const hideRule = document.createElement("style");

        hideRule.textContent = `
            html.is-student [data-staff-only],
            html.is-student .staff-only {
                display: none !important;
            }
        `;

        document.head.appendChild(hideRule);
    }

    /* =====================================================
       UPDATE PAGE AFTER HTML LOADS
    ===================================================== */

    document.addEventListener(
        "DOMContentLoaded",
        function () {

            /* =============================================
               STUDENT NAVIGATION
            ============================================= */

            if (isStudent) {

                document.querySelectorAll(
                    'a[href="facility.html"]'
                ).forEach(function (link) {
                    link.setAttribute(
                        "href",
                        "student-facility.html"
                    );
                });

                const staffOnlyLinks = STAFF_ONLY_PAGES
                    .filter(function (p) {
                        return p !== "staff-dashboard.html" &&
                            p !== "facility.html";
                    })
                    .map(function (p) {
                        return 'a[href="' + p + '"]';
                    })
                    .join(", ");

                if (staffOnlyLinks) {

                    document.querySelectorAll(
                        staffOnlyLinks
                    ).forEach(function (link) {

                        const card = link.closest(
                            ".dashboard-card"
                        );

                        (card || link).remove();
                    });
                }

                document.querySelectorAll(
                    'a[href="staff-dashboard.html"]'
                ).forEach(function (link) {
                    link.setAttribute(
                        "href",
                        "student-dashboard.html"
                    );
                });

            } else {

                /* =============================================
                   STAFF NAVIGATION
                ============================================= */

                document.querySelectorAll(
                    'a[href="student-facility.html"]'
                ).forEach(function (link) {
                    link.setAttribute(
                        "href",
                        "facility.html"
                    );
                });

                document.querySelectorAll(
                    'a[href="student-dashboard.html"]'
                ).forEach(function (link) {
                    link.setAttribute(
                        "href",
                        "staff-dashboard.html"
                    );
                });
            }

            /* =============================================
               LOGOUT LINKS
            ============================================= */

            document.querySelectorAll(
                "a.logout"
            ).forEach(function (link) {
                link.setAttribute(
                    "href",
                    "login.html"
                );
            });

            /* =============================================
               USER INITIALS
            ============================================= */

            const parts = fullName.split(/\s+/);

            const initials = (
                parts.length > 1
                    ? parts[0][0] +
                    parts[parts.length - 1][0]
                    : fullName.substring(0, 2)
            ).toUpperCase();

            /* =============================================
               UPDATE TOP BAR BY ID
            ============================================= */

            setText("topbarName", fullName);
            setText("topbarRole", roleLabel);
            setText("topbarInitials", initials);

            const avatarSpan =
                document.querySelector(
                    "#topbarAvatar span"
                );

            if (avatarSpan) {
                avatarSpan.textContent = initials;
            }

            /* =============================================
               UPDATE TOP BAR WITHOUT IDS
            ============================================= */

            if (!document.getElementById("topbarName")) {

                const avatar =
                    document.querySelector(
                        ".staff-profile .staff-avatar"
                    );

                const name =
                    document.querySelector(
                        ".staff-profile .staff-info strong"
                    );

                const role =
                    document.querySelector(
                        ".staff-profile .staff-info span"
                    );

                if (avatar) {

                    const avatarText =
                        avatar.querySelector("span");

                    if (avatarText) {
                        avatarText.textContent = initials;
                    } else {
                        avatar.textContent = initials;
                    }
                }

                if (name) {
                    name.textContent = fullName;
                }

                if (role) {
                    role.textContent = roleLabel;
                }
            }
        }
    );

    /* =====================================================
       HELPER: UPDATE ELEMENT TEXT
    ===================================================== */

    function setText(id, value) {

        const element = document.getElementById(id);

        if (element) {
            element.textContent = value;
        }
    }

})();
