
/* =========================================================
   CAMPUS FACILITY BOOKING SYSTEM
   LOGIN.JS - UPDATED
========================================================= */

const API_BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", function () {

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();
            loginUser();
        });
    }

    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";

            const icon = this.querySelector("i");

            if (icon) {
                icon.classList.toggle("fa-eye");
                icon.classList.toggle("fa-eye-slash");
            }
        });
    }

    const forgotPassword = document.getElementById("forgotPassword");

    if (forgotPassword) {
        forgotPassword.addEventListener("click", function (event) {
            event.preventDefault();
            alert("Please contact the campus administrator to reset your password.");
        });
    }
});

/* =========================================================
   LOGIN USER
========================================================= */

async function loginUser() {

    clearErrors();

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    const username = usernameInput.value.trim();
    const password = passwordInput.value;

    let hasError = false;

    if (!username) {
        showFieldError(
            usernameInput,
            document.getElementById("usernameError"),
            "Please enter your username."
        );
        hasError = true;
    }

    if (!password) {
        showFieldError(
            passwordInput,
            document.getElementById("passwordError"),
            "Please enter your password."
        );
        hasError = true;
    }

    if (hasError) return;

    const loginButton = document.getElementById("loginButton");
    const loginButtonText = document.getElementById("loginButtonText");
    const loginButtonIcon = document.getElementById("loginButtonIcon");

    if (loginButton) loginButton.disabled = true;
    if (loginButtonText) loginButtonText.textContent = "Signing In...";
    if (loginButtonIcon) {
        loginButtonIcon.className = "fa-solid fa-spinner fa-spin";
    }

    let success = false;

    try {

        /* ================================================
           AUTHENTICATE
        ================================================ */

        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (response.status === 401) {
            showLoginError("Invalid username or password.");
            return;
        }

        if (response.status === 429) {
            showLoginError(
                "Too many failed attempts. Please wait 15 minutes."
            );
            return;
        }

        if (!response.ok) {
            showLoginError(
                "Login failed. Server returned status " + response.status
            );
            return;
        }

        const authResponse = await response.json();

        /* ================================================
           VERIFY LOGIN SESSION
        ================================================ */

        const sessionResponse = await fetch(
            `${API_BASE_URL}/auth/me`,
            {
                method: "GET",
                credentials: "same-origin",
                cache: "no-store"
            }
        );

        if (!sessionResponse.ok) {
            console.error(
                "Session verification failed:",
                sessionResponse.status
            );

            showLoginError(
                "Your password was accepted, but the server did not keep your login session. Please check the Spring Boot session configuration."
            );
            return;
        }

        const verifiedUser = await sessionResponse.json();

        /* ================================================
           VALIDATE USER DETAILS
        ================================================ */

        const userId = verifiedUser.userId;

        if (!userId) {
            console.error(
                "Missing userId in authentication response:",
                verifiedUser
            );

            showLoginError(
                "Login succeeded, but your account information is incomplete."
            );
            return;
        }

        const accountType = String(
            verifiedUser.accountType || ""
        ).trim().toUpperCase();

        if (accountType !== "STUDENT" && accountType !== "STAFF") {
            console.error("Unexpected account type:", accountType);
            showLoginError(
                "Your account type could not be verified."
            );
            return;
        }

        /* ================================================
           SAVE LOGGED-IN USER
        ================================================ */

        const loggedInUser = {
            userId: userId,
            username: verifiedUser.username,
            email: verifiedUser.email,
            role: verifiedUser.role,
            accountType: accountType,
            firstName: verifiedUser.firstName,
            lastName: verifiedUser.lastName
        };

        sessionStorage.removeItem("loggedInUser");
        localStorage.removeItem("loggedInUser");

        const rememberMe = document.getElementById("rememberMe");

        if (rememberMe && rememberMe.checked) {
            localStorage.setItem(
                "loggedInUser",
                JSON.stringify(loggedInUser)
            );
        } else {
            sessionStorage.setItem(
                "loggedInUser",
                JSON.stringify(loggedInUser)
            );
        }

        localStorage.setItem("loginId", String(userId));
        localStorage.setItem("registrarId", String(userId));

        if (verifiedUser.username) {
            localStorage.setItem(
                "username",
                verifiedUser.username
            );
        }

        /* ================================================
           REDIRECT TO CORRECT DASHBOARD
        ================================================ */

        const redirectUrl = accountType === "STAFF"
            ? "staff-dashboard.html"
            : "student-dashboard.html";

        console.log(
            "Login successful. Redirecting to:",
            redirectUrl
        );

        success = true;

        if (loginButtonText) {
            loginButtonText.textContent = "Success!";
        }

        if (loginButtonIcon) {
            loginButtonIcon.className = "fa-solid fa-check";
        }

        window.location.replace(redirectUrl);

    } catch (error) {

        console.error("Login error:", error);

        showLoginError(
            "Unable to complete login. Check that Spring Boot is running."
        );

    } finally {

        if (!success) {
            if (loginButton) loginButton.disabled = false;

            if (loginButtonText) {
                loginButtonText.textContent = "Sign In";
            }

            if (loginButtonIcon) {
                loginButtonIcon.className = "fa-solid fa-arrow-right";
            }
        }
    }
}

/* =========================================================
   HELPER FUNCTIONS
========================================================= */

function showFieldError(input, errorElement, message) {

    if (input) {
        input.classList.add("input-error");
    }

    if (errorElement) {
        errorElement.textContent = message;
    }
}

function clearErrors() {

    document.querySelectorAll(".error-message").forEach(function (el) {
        el.textContent = "";
    });

    document.querySelectorAll(".input-error").forEach(function (el) {
        el.classList.remove("input-error");
    });

    const loginError = document.getElementById("loginError");

    if (loginError) {
        loginError.classList.remove("show");
    }
}

function showLoginError(message) {

    const loginError = document.getElementById("loginError");

    if (loginError) {
        const errorText = loginError.querySelector("span");

        if (errorText) {
            errorText.textContent = message;
        }

        loginError.classList.add("show");
    }

    console.error(message);
}
