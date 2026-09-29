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
        The login page should store the login information
        in localStorage after successful login.

        Example:

        localStorage.setItem("loginId", login.loginId);
        localStorage.setItem("registrarId", login.registrarId);
        localStorage.setItem("username", login.username);
    */

    const loginId = localStorage.getItem("loginId");
    const registrarId = localStorage.getItem("registrarId");
    const username = localStorage.getItem("username");

    /*
        If there is no logged-in user, use default information.
        This prevents the page from completely breaking when
        you open profile.html directly during development.
    */

    if (!loginId) {

        console.log("No logged-in user found.");

        setDefaultProfile();

        return;
    }


    try {

        /*
            Get the login information from the backend.
        */

        const response = await fetch(
            `${API_BASE_URL}/login/read/${loginId}`
        );


        if (!response.ok) {

            throw new Error(
                "Unable to retrieve login information."
            );

        }


        const login = await response.json();


        /*
            Display login information.
        */

        const storedUsername =
            login.username || username || "User";

        const storedRegistrarId =
            login.registrarId || registrarId || "";


        /*
            Display the username.
        */

        document.getElementById("topbarName").textContent =
            storedUsername;

        document.getElementById("profileName").textContent =
            storedUsername;


        /*
            Display registrar ID.
        */

        document.getElementById("profileUserId").textContent =
            storedRegistrarId || "N/A";

        document.getElementById("userId").value =
            storedRegistrarId || "N/A";

        document.getElementById("accountUserId").textContent =
            storedRegistrarId || "N/A";


        /*
            Display username as email if no email is
            currently available from the backend.
        */

        const emailElement =
            document.getElementById("profileEmail");

        if (emailElement) {

            emailElement.textContent =
                login.username || "No email available";

        }


        /*
            Set initials.
        */

        setAvatarInitials(storedUsername);


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
            Remove login information.
        */

        localStorage.removeItem("loginId");
        localStorage.removeItem("registrarId");
        localStorage.removeItem("username");


        /*
            Return to login page.
        */

        window.location.href = "index.html";

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