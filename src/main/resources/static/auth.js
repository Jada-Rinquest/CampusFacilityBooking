/* =========================================================
   CAMPUS FACILITY BOOKING
   SHARED SIGN-IN + PERMISSIONS

   Loaded in the <head> of every page that needs a signed-in
   user. It runs before the page renders so that:
     - signed-out visitors go back to the login page
     - students cannot open staff-only pages (users, invoices,
       equipment, maintenance, staff dashboard) and never see
       links to them
     - anything marked data-staff-only (or class "staff-only")
       is hidden from students, including buttons that a page's
       own script adds later (edit / delete / add facility etc.)
     - the top bar shows the signed-in person's name and role
========================================================= */

(function () {

    const STAFF_ONLY_PAGES = [
        "user.html",
        "invoice.html",
        "equipment.html",
        "maintenance.html",
        "staff-dashboard.html"
    ];
    const STUDENT_ONLY_PAGES = ["student-dashboard.html"];

    function readUser() {
        try {
            return JSON.parse(
                sessionStorage.getItem("loggedInUser") ||
                localStorage.getItem("loggedInUser")
            );
        } catch (error) {
            return null;
        }
    }

    // Same rule as AccountType.java: @mycput = student, @cput.ac.za = staff
    function accountTypeOf(user) {
        if (user.accountType) {
            return user.accountType;
        }
        const email = String(user.email || "").toLowerCase();
        return email.endsWith("@cput.ac.za") ? "STAFF" : "STUDENT";
    }

    const user = readUser();

    if (!user || !user.userId) {
        window.location.replace("index.html");
        return;
    }

    const isStudent = accountTypeOf(user) === "STUDENT";
    const page = window.location.pathname.split("/").pop() || "index.html";

    if (isStudent && STAFF_ONLY_PAGES.includes(page)) {
        window.location.replace("student-dashboard.html");
        return;
    }

    if (!isStudent && STUDENT_ONLY_PAGES.includes(page)) {
        window.location.replace("staff-dashboard.html");
        return;
    }

    const fullName = `${user.firstName || ""} ${user.lastName || ""}`.trim() || user.username;
    const roleLabel = isStudent
        ? "Student"
        : (user.role === "ADMIN" ? "Administrator" : "Lecturer");

    // Available to page scripts (e.g. profile.js)
    window.currentUser = {
        ...user,
        accountType: isStudent ? "STUDENT" : "STAFF",
        isStudent: isStudent,
        fullName: fullName,
        roleLabel: roleLabel
    };

    // Hide staff-only controls from students straight away, before the page
    // paints. A CSS rule (instead of removing elements) also covers buttons
    // that a page's script builds later, such as edit/delete in a table.
    if (isStudent) {
        document.documentElement.classList.add("is-student");

        const hideRule = document.createElement("style");
        hideRule.textContent =
            "html.is-student [data-staff-only], html.is-student .staff-only { display: none !important; }";
        document.head.appendChild(hideRule);
    }

    document.addEventListener("DOMContentLoaded", function () {

        if (isStudent) {

            // Students never see links to staff-only pages
            const staffOnlyLinks = STAFF_ONLY_PAGES
                .filter(function (p) { return p !== "staff-dashboard.html"; })
                .map(function (p) { return 'a[href="' + p + '"]'; })
                .join(", ");

            document.querySelectorAll(staffOnlyLinks)
                .forEach(function (link) {
                    const card = link.closest(".dashboard-card");
                    (card || link).remove();
                });

            document.querySelectorAll('a[href="staff-dashboard.html"]')
                .forEach(function (link) {
                    link.setAttribute("href", "student-dashboard.html");
                });
        }

        // Logout always goes through login.html, which clears the session
        document.querySelectorAll("a.logout").forEach(function (link) {
            link.setAttribute("href", "login.html");
        });

        const parts = fullName.split(/\s+/);
        const initials = (parts.length > 1
            ? parts[0][0] + parts[parts.length - 1][0]
            : fullName.substring(0, 2)).toUpperCase();

        setText("topbarName", fullName);
        setText("topbarRole", roleLabel);
        setText("topbarInitials", initials);

        const avatarSpan = document.querySelector("#topbarAvatar span");
        if (avatarSpan) {
            avatarSpan.textContent = initials;
        }

        // Pages whose top bar has no ids still use the shared
        // "Staff Member / Administrator" block - fill that in too
        if (!document.getElementById("topbarName")) {
            const avatar = document.querySelector(".staff-profile .staff-avatar");
            const name = document.querySelector(".staff-profile .staff-info strong");
            const role = document.querySelector(".staff-profile .staff-info span");

            if (avatar) {
                (avatar.querySelector("span") || avatar).textContent = initials;
            }
            if (name) {
                name.textContent = fullName;
            }
            if (role) {
                role.textContent = roleLabel;
            }
        }
    });

    function setText(id, value) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = value;
        }
    }

})();