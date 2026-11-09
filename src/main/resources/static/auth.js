
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   SHARED SIGN-IN + ROLE PERMISSIONS

   This script:
   - Checks whether the user is logged in
   - Identifies students and staff
   - Redirects users away from incorrect pages
   - Updates navigation based on account type
   - Hides staff-only controls from students
   - Displays the logged-in user's name and role
   - Preserves the existing logout navigation
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

        // Same email convention as AccountType.java
        if (email.endsWith("@cput.ac.za")) {
            return "STAFF";
        }

        return "STUDENT";
    }

    /* =====================================================
       CHECK LOGIN
    ===================================================== */

    const user = readUser();

    if (!user || !user.userId) {

        window.location.replace("index.html");

        return;
    }

    /* =====================================================
       SERVER SESSION CHECK

       The stored user only decides what the page shows.
       The server enforces access on every request using the
       session cookie. If the server answers 401 (session
       expired, server restarted, signed out elsewhere), the
       local copy is out of date: clear it and sign in again.
    ===================================================== */

    const originalFetch = window.fetch.bind(window);

    function sessionExpired() {

        ["loggedInUser", "loginId", "registrarId", "username"]
            .forEach(function (key) {
                sessionStorage.removeItem(key);
                localStorage.removeItem(key);
            });

        window.location.replace("index.html");
    }

    window.fetch = function (input, init) {

        return originalFetch(input, init).then(function (response) {

            if (response.status === 401) {
                sessionExpired();
            }

            return response;
        });
    };

    // Confirm the session is still valid when the page opens
    originalFetch("/auth/me", { credentials: "same-origin" })
        .then(function (response) {
            if (response.status === 401) {
                sessionExpired();
            }
        })
        .catch(function () {
            /* server not reachable - the page shows its own error */
        });

    /* =====================================================
       DETERMINE ROLE AND CURRENT PAGE
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

            // Students have a separate facility browsing page
            window.location.replace(
                "student-facility.html"
            );

        } else {

            // Other staff-only pages
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

            // Staff use the facility management page
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
       MAKE CURRENT USER AVAILABLE TO OTHER SCRIPTS
    ===================================================== */

    window.currentUser = {

        ...user,

        accountType: isStudent ? "STUDENT" : "STAFF",

        isStudent: isStudent,

        fullName: fullName,

        roleLabel: roleLabel

    };

    /* =====================================================
       HIDE STAFF-ONLY CONTROLS FOR STUDENTS
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

                // Send students to the Student Facilities page
                document.querySelectorAll(
                    'a[href="facility.html"]'
                ).forEach(function (link) {

                    link.setAttribute(
                        "href",
                        "student-facility.html"
                    );

                });

                // Hide staff-only navigation links
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

                // Student dashboard navigation
                document.querySelectorAll(
                    'a[href="staff-dashboard.html"]'
                ).forEach(function (link) {

                    link.setAttribute(
                        "href",
                        "student-dashboard.html"
                    );

                });

            }

            /* =============================================
               STAFF NAVIGATION
            ============================================= */

            else {

                // Staff use the Facility Management page
                document.querySelectorAll(
                    'a[href="student-facility.html"]'
                ).forEach(function (link) {

                    link.setAttribute(
                        "href",
                        "facility.html"
                    );

                });

                // Staff dashboard navigation
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
