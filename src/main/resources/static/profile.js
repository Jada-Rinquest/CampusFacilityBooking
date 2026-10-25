/* =========================================================
   PROFILE PAGE JAVASCRIPT
   Campus Facility Booking System
========================================================= */


/* =========================================================
   API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080";


/* =========================================================
   PAGE INITIALIZATION
========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    loadProfile();

    setupMobileMenu();

    setupLogout();

    setupChangePassword();

});


/* =========================================================
   LOAD PROFILE
========================================================= */

async function loadProfile() {

    /*
        auth.js runs first and puts the signed-in user
        (saved by script.js at login) on window.currentUser.
    */

    const current = window.currentUser;

    if (!current) {

        setDefaultProfile();

        return;
    }


    // Show what we already know straight away
    showProfile({
        userId: current.userId,
        firstName: current.firstName,
        lastName: current.lastName,
        email: current.email
    }, current);


    try {

        /*
            Get the full user record (the name, email, date of
            birth and department entered when registering).
        */

        const response = await fetch(
            `${API_BASE_URL}/user/read/${encodeURIComponent(current.userId)}`
        );


        if (!response.ok) {

            throw new Error(
                "Unable to retrieve user information."
            );

        }


        const user = await response.json();

        showProfile(user, current);


        /*
            Department name.
        */

        if (user.departmentId) {

            const departmentResponse = await fetch(
                `${API_BASE_URL}/department/read/${encodeURIComponent(user.departmentId)}`
            );

            const departmentName = departmentResponse.ok
                ? (await departmentResponse.json()).name
                : user.departmentId;

            setText("profileDepartment", departmentName);
            setValue("department", departmentName);
        }


        /*
            Student number or staff ID.
        */

        const recordType = current.isStudent ? "student" : "staff";

        const recordResponse = await fetch(
            `${API_BASE_URL}/${recordType}/user/${encodeURIComponent(current.userId)}`
        );

        if (recordResponse.ok) {

            const record = await recordResponse.json();

            if (current.isStudent) {
                setValue("studentNumber", record.studentNumber || "N/A");
            } else {
                setValue("staffId", record.staffId || "N/A");
            }
        }


    } catch (error) {

        console.error(
            "Error loading profile:",
            error
        );

        showStatus(
            "Unable to load profile information.",
            "error"
        );

    }

}


/* =========================================================
   SHOW PROFILE
========================================================= */

function showProfile(user, current) {

    const fullName =
        `${user.firstName || ""} ${user.lastName || ""}`.trim() ||
        current.username;


    setText("topbarName", fullName);
    setText("topbarRole", current.roleLabel);

    setText("profileName", fullName);
    setText("profileRole", current.roleLabel);
    setText("profileEmail", user.email || "N/A");
    setText("profileUserId", user.userId);

    setValue("firstName", user.firstName || "");
    setValue("lastName", user.lastName || "");
    setValue("email", user.email || "");
    setValue("dateOfBirth", user.dateOfBirth || "");
    setValue("userId", user.userId);

    setValue("accountRole", current.roleLabel);
    setText("accountType", current.isStudent ? "Student" : "Staff");
    setText("accountUserId", user.userId);


    /*
        Students see their student number, staff see their staff ID.
    */

    const studentGroup = document.getElementById("studentNumberGroup");
    const staffGroup = document.getElementById("staffIdGroup");

    if (studentGroup) {
        studentGroup.style.display = current.isStudent ? "" : "none";
    }

    if (staffGroup) {
        staffGroup.style.display = current.isStudent ? "none" : "";
    }


    setAvatarInitials(fullName);

}


function setText(id, value) {

    const element = document.getElementById(id);

    if (element) {
        element.textContent = value;
    }

}


function setValue(id, value) {

    const element = document.getElementById(id);

    if (element) {
        element.value = value;
    }

}


/* =========================================================
   DEFAULT PROFILE
========================================================= */

function setDefaultProfile() {

    const defaultName = "User";


    document.getElementById("topbarName").textContent =
        defaultName;

    document.getElementById("topbarRole").textContent =
        "User";

    document.getElementById("profileName").textContent =
        "User Name";

    document.getElementById("profileRole").textContent =
        "User";

    document.getElementById("accountRole").value =
        "User";

    document.getElementById("accountType").textContent =
        "User";

    document.getElementById("profileUserId").textContent =
        "N/A";

    document.getElementById("userId").value =
        "N/A";

    document.getElementById("accountUserId").textContent =
        "N/A";

    setAvatarInitials(defaultName);

}


/* =========================================================
   SET AVATAR INITIALS
========================================================= */

function setAvatarInitials(name) {

    const initialsElement =
        document.getElementById("avatarInitials");

    const topbarAvatar =
        document.getElementById("topbarAvatar");


    if (!name) {

        name = "User";

    }


    /*
        Split the name into individual words.
    */

    const parts =
        name.trim().split(/\s+/);


    let initials = "";


    if (parts.length >= 2) {

        initials =
            parts[0].charAt(0) +
            parts[parts.length - 1].charAt(0);

    } else {

        initials =
            parts[0].substring(0, 2);

    }


    initials =
        initials.toUpperCase();


    if (initialsElement) {

        initialsElement.textContent =
            initials;

    }


    if (topbarAvatar) {

        topbarAvatar.querySelector("span").textContent =
            initials;

    }

}


/* =========================================================
   MOBILE MENU
========================================================= */

function setupMobileMenu() {

    const menuButton =
        document.getElementById("mobileMenuBtn");

    const sidebar =
        document.querySelector(".sidebar");


    if (!menuButton || !sidebar) {

        return;

    }


    menuButton.addEventListener("click", function () {

        sidebar.classList.toggle("open");

    });

}


/* =========================================================
   LOGOUT
========================================================= */

function setupLogout() {

    const logoutButton =
        document.getElementById("logoutButton");


    if (!logoutButton) {

        return;

    }


    logoutButton.addEventListener("click", function () {

        /*
            login.html clears the session and returns to sign in.
        */

        window.location.href = "login.html";

    });

}


/* =========================================================
   CHANGE PASSWORD
========================================================= */

function setupChangePassword() {

    const changePasswordButton =
        document.getElementById("changePasswordButton");


    if (!changePasswordButton) {

        return;

    }


    changePasswordButton.addEventListener(
        "click",
        function () {

            showStatus(
                "Password changing will be connected to the login system.",
                "info"
            );

        }
    );

}


/* =========================================================
   STATUS MESSAGE
========================================================= */

function showStatus(message, type = "info") {

    const statusMessage =
        document.getElementById("statusMessage");

    const statusText =
        document.getElementById("statusText");


    if (!statusMessage || !statusText) {

        return;

    }


    statusText.textContent =
        message;


    statusMessage.classList.remove(
        "hidden",
        "success",
        "error",
        "info"
    );


    statusMessage.classList.add(type);


    /*
        Automatically hide the message.
    */

    setTimeout(function () {

        statusMessage.classList.add("hidden");

    }, 4000);

}