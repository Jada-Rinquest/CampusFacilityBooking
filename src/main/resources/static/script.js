// =========================================================
// LOGIN JAVASCRIPT - COMPLETE FIXED VERSION
// Campus Facility Booking System
// =========================================================

const API_BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", function () {

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();
            loginUser();
        });
    }

    // Password toggle
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";
            this.querySelector("i").classList.toggle("fa-eye");
            this.querySelector("i").classList.toggle("fa-eye-slash");
        });
    }

    // Forgot password
    const forgotPassword = document.getElementById("forgotPassword");
    if (forgotPassword) {
        forgotPassword.addEventListener("click", function (event) {
            event.preventDefault();
            alert("Your password is your User ID. Please contact the campus administrator if you have forgotten it.");
        });
    }

});


// =========================================================
// LOGIN USER
// =========================================================

async function loginUser() {

    clearErrors();


    // -----------------------------------------------------
    // Get values
    // -----------------------------------------------------

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();


    // -----------------------------------------------------
    // Validation
    // -----------------------------------------------------

    let hasError = false;

    if (!username) {
        showFieldError(
            document.getElementById("username"),
            document.getElementById("usernameError"),
            "Please enter your username."
        );
        hasError = true;
    }

    if (!password) {
        showFieldError(
            document.getElementById("password"),
            document.getElementById("passwordError"),
            "Please enter your password."
        );
        hasError = true;
    }

    if (hasError) {
        return;
    }


    // -----------------------------------------------------
    // Disable button
    // -----------------------------------------------------

    const loginButton = document.getElementById("loginButton");
    const loginButtonText = document.getElementById("loginButtonText");
    const loginButtonIcon = document.getElementById("loginButtonIcon");

    if (loginButton) {
        loginButton.disabled = true;
        loginButtonText.textContent = "Signing In...";
        loginButtonIcon.className = "fa-solid fa-spinner fa-spin";
    }


    try {

        // =================================================
        // AUTHENTICATE USING LOGIN ENDPOINT
        // =================================================

        const response = await fetch(`${API_BASE_URL}/login/authenticate`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });


        // =================================================
        // AUTHENTICATION FAILED
        // =================================================

        if (response.status === 401) {
            showLoginError("Invalid username or password.");
            return;
        }


        // =================================================
        // SERVER ERROR
        // =================================================

        if (!response.ok) {
            showLoginError("Unable to connect to the login service.");
            return;
        }


        // =================================================
        // AUTHENTICATION SUCCESSFUL
        // =================================================

        const authResponse = await response.json();

        console.log("Login successful:", authResponse);


        // =================================================
        // SAVE LOGGED-IN USER
        // =================================================

        const loggedInUser = {
            userId: authResponse.userId,
            username: authResponse.username,
            email: authResponse.email,
            role: authResponse.role,
            firstName: authResponse.firstName,
            lastName: authResponse.lastName
        };

        const rememberMe = document.getElementById("rememberMe");

        if (rememberMe && rememberMe.checked) {
            localStorage.setItem("loggedInUser", JSON.stringify(loggedInUser));
        } else {
            sessionStorage.setItem("loggedInUser", JSON.stringify(loggedInUser));
        }


        // =================================================
        // REDIRECT TO DASHBOARD
        // =================================================

        loginButtonText.textContent = "Success!";
        loginButtonIcon.className = "fa-solid fa-check";

        // Redirect based on role
        let redirectUrl = "student-dashboard.html";
        if (authResponse.role === "ADMIN" || authResponse.role === "LECTURER") {
            redirectUrl = "staff-dashboard.html";
        }

        setTimeout(function () {
            window.location.href = redirectUrl;
        }, 700);


    } catch (error) {

        console.error("Login error:", error);
        showLoginError("Unable to connect to the server. Make sure Spring Boot is running.");

    } finally {

        setTimeout(function () {
            if (loginButton) {
                loginButton.disabled = false;
                loginButtonText.textContent = "Sign In";
                loginButtonIcon.className = "fa-solid fa-arrow-right";
            }
        }, 700);

    }

}


// =========================================================
// HELPER FUNCTIONS
// =========================================================

function showFieldError(input, errorElement, message) {
    input.classList.add("input-error");
    errorElement.textContent = message;
}

function clearErrors() {
    document.querySelectorAll(".error-message").forEach(el => el.textContent = "");
    document.querySelectorAll(".input-error").forEach(el => el.classList.remove("input-error"));
    const loginError = document.getElementById("loginError");
    if (loginError) loginError.classList.remove("show");
}

function showLoginError(message) {
    const loginError = document.getElementById("loginError");
    const errorText = loginError?.querySelector("span");
    if (errorText) {
        errorText.textContent = message;
    }
    if (loginError) {
        loginError.classList.add("show");
    }
}