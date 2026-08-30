// =========================================================
// REGISTER JAVASCRIPT - COMPLETE FIXED VERSION
// Campus Facility Booking System
// =========================================================

const API_BASE_URL = "http://localhost:8080";

// =========================================================
// FORM SUBMISSION
// =========================================================

document.addEventListener("DOMContentLoaded", function () {

    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();
            registerUser();
        });
    }

    // Password toggles
    setupPasswordToggles();

    // Password validation
    setupPasswordValidation();

    // Mobile menu
    setupMobileMenu();

});


// =========================================================
// SETUP PASSWORD TOGGLES
// =========================================================

function setupPasswordToggles() {
    const togglePassword = document.getElementById("togglePassword");
    const toggleConfirm = document.getElementById("toggleConfirmPassword");
    const passwordInput = document.getElementById("password");
    const confirmInput = document.getElementById("confirmPassword");

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";
            this.querySelector("i").classList.toggle("fa-eye");
            this.querySelector("i").classList.toggle("fa-eye-slash");
        });
    }

    if (toggleConfirm && confirmInput) {
        toggleConfirm.addEventListener("click", function () {
            const isPassword = confirmInput.type === "password";
            confirmInput.type = isPassword ? "text" : "password";
            this.querySelector("i").classList.toggle("fa-eye");
            this.querySelector("i").classList.toggle("fa-eye-slash");
        });
    }
}


// =========================================================
// SETUP PASSWORD VALIDATION
// =========================================================

function setupPasswordValidation() {
    const passwordInput = document.getElementById("password");

    if (passwordInput) {
        passwordInput.addEventListener("input", function () {
            const value = this.value;
            const lengthReq = document.getElementById("lengthRequirement");
            const numberReq = document.getElementById("numberRequirement");

            if (lengthReq) {
                if (value.length >= 8) {
                    lengthReq.innerHTML = '<i class="fa-solid fa-circle-check"></i> At least 8 characters';
                    lengthReq.style.color = "#16a34a";
                } else {
                    lengthReq.innerHTML = '<i class="fa-solid fa-circle"></i> At least 8 characters';
                    lengthReq.style.color = "#8b96a3";
                }
            }

            if (numberReq) {
                if (/\d/.test(value)) {
                    numberReq.innerHTML = '<i class="fa-solid fa-circle-check"></i> At least one number';
                    numberReq.style.color = "#16a34a";
                } else {
                    numberReq.innerHTML = '<i class="fa-solid fa-circle"></i> At least one number';
                    numberReq.style.color = "#8b96a3";
                }
            }
        });
    }
}


// =========================================================
// SETUP MOBILE MENU
// =========================================================

function setupMobileMenu() {
    const mobileMenuBtn = document.getElementById("mobileMenuBtn");
    const sidebar = document.querySelector(".sidebar");

    if (mobileMenuBtn && sidebar) {
        mobileMenuBtn.addEventListener("click", function () {
            sidebar.classList.toggle("open");
        });
    }
}


// =========================================================
// REGISTER USER - COMPLETE FIXED VERSION
// =========================================================

async function registerUser() {

    clearMessages();


    // -----------------------------------------------------
    // Get form values
    // -----------------------------------------------------

    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const username = document.getElementById("username").value.trim();
    const studentNumber = document.getElementById("studentNumber").value.trim();
    const email = document.getElementById("email").value.trim();
    const department = document.getElementById("department").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const terms = document.getElementById("terms").checked;


    // -----------------------------------------------------
    // Validation
    // -----------------------------------------------------

    let hasError = false;

    if (!firstName) {
        document.getElementById("firstNameError").textContent = "First name is required.";
        hasError = true;
    }

    if (!lastName) {
        document.getElementById("lastNameError").textContent = "Last name is required.";
        hasError = true;
    }

    if (!username) {
        document.getElementById("usernameError").textContent = "Username is required.";
        hasError = true;
    }

    if (!studentNumber) {
        document.getElementById("studentNumberError").textContent = "Student number is required.";
        hasError = true;
    }

    if (!email) {
        document.getElementById("emailError").textContent = "Email is required.";
        hasError = true;
    } else if (!isValidEmail(email)) {
        document.getElementById("emailError").textContent = "Please enter a valid email address.";
        hasError = true;
    }

    if (!department) {
        document.getElementById("departmentError").textContent = "Please select a department.";
        hasError = true;
    }

    if (!password) {
        document.getElementById("passwordError").textContent = "Password is required.";
        hasError = true;
    } else if (password.length < 8) {
        document.getElementById("passwordError").textContent = "Password must be at least 8 characters.";
        hasError = true;
    }

    if (!confirmPassword) {
        document.getElementById("confirmPasswordError").textContent = "Please confirm your password.";
        hasError = true;
    } else if (password !== confirmPassword) {
        document.getElementById("confirmPasswordError").textContent = "Passwords do not match.";
        hasError = true;
    }

    if (!terms) {
        document.getElementById("termsError").textContent = "You must agree to the terms and conditions.";
        hasError = true;
    }

    if (hasError) {
        return;
    }


    // -----------------------------------------------------
    // Disable button
    // -----------------------------------------------------

    const registerButton = document.getElementById("registerButton");

    if (registerButton) {
        registerButton.disabled = true;
        registerButton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Creating Account...';
    }


    try {

        // Generate IDs
        const userId = "USER-" + Date.now().toString().substring(6);
        const loginId = "LOGIN-" + Date.now().toString().substring(6);
        const userRoleId = "UR-" + Date.now().toString().substring(6);
        const today = new Date().toISOString().split("T")[0];

        // Department mapping
        const departmentMap = {
            "ICT": "D001",
            "Business": "D002",
            "Engineering": "D003",
            "Education": "D004",
            "Health": "D005",
            "Applied Sciences": "D006",
            "Other": "D007"
        };
        const departmentId = departmentMap[department] || "D007";


        // =================================================
        // STEP 1: CREATE USER
        // =================================================

        const userResponse = await fetch(`${API_BASE_URL}/user/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userId: userId,
                firstName: firstName,
                lastName: lastName,
                email: email,
                dateOfBirth: "2000-01-01",
                departmentId: departmentId
            })
        });

        if (!userResponse.ok) {
            let errorMessage = "Unable to create your account.";
            try {
                const errorData = await userResponse.json();
                if (errorData.message) errorMessage = errorData.message;
            } catch (e) {}
            throw new Error(errorMessage);
        }


        // =================================================
        // STEP 2: CREATE REGISTER RECORD
        // =================================================

        const registerResponse = await fetch(`${API_BASE_URL}/register/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                registrarId: userId,
                email: email,
                dateRegistered: today
            })
        });

        if (!registerResponse.ok) {
            throw new Error("Registration record could not be created.");
        }


        // =================================================
        // STEP 3: CREATE LOGIN CREDENTIALS
        // =================================================

        const loginResponse = await fetch(`${API_BASE_URL}/login/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                loginId: loginId,
                registrarId: userId,
                username: username,
                password: password
            })
        });

        if (!loginResponse.ok) {
            throw new Error("Login credentials could not be created.");
        }


        // =================================================
        // STEP 4: CREATE USER ROLE
        // =================================================

        try {
            await fetch(`${API_BASE_URL}/userrole/create`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userRoleId: userRoleId,
                    userId: userId,
                    role: "STUDENT"
                })
            });
        } catch (e) {
            console.warn("UserRole could not be created:", e);
        }


        // =================================================
        // SUCCESS
        // =================================================

        showSuccess("Registration successful! Redirecting to login...");

        localStorage.setItem("registeredUsername", username);

        setTimeout(function () {
            window.location.href = "index.html";
        }, 2000);


    } catch (error) {

        console.error("Registration error:", error);
        showError(error.message || "Something went wrong during registration.");

    } finally {

        if (registerButton) {
            registerButton.disabled = false;
            registerButton.innerHTML = '<span>Create Account</span><i class="fa-solid fa-arrow-right"></i>';
        }

    }

}


// =========================================================
// HELPER FUNCTIONS
// =========================================================

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function showError(message) {
    const errorElement = document.getElementById("registerError");
    const errorText = document.getElementById("registerErrorText");
    if (errorElement && errorText) {
        errorText.textContent = message;
        errorElement.classList.add("show");
    }
}

function showSuccess(message) {
    const existing = document.querySelector(".register-success");
    if (existing) existing.remove();

    const successDiv = document.createElement("div");
    successDiv.className = "register-success";
    successDiv.style.cssText = `
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 14px 17px;
        background: #f0fdf4;
        border: 1px solid #bbf7d0;
        border-radius: 9px;
        color: #16a34a;
        font-size: 13px;
        margin-bottom: 20px;
    `;
    successDiv.innerHTML = `<i class="fa-solid fa-circle-check"></i> ${message}`;

    const form = document.getElementById("registerForm");
    if (form) {
        form.parentNode.insertBefore(successDiv, form);
    }
}

function clearMessages() {
    document.querySelectorAll(".error-message").forEach(el => el.textContent = "");
    const errorElement = document.getElementById("registerError");
    if (errorElement) errorElement.classList.remove("show");
    const successElement = document.querySelector(".register-success");
    if (successElement) successElement.remove();
}