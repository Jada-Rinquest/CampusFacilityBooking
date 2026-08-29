// =========================================================
// REGISTER JAVASCRIPT
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

});


// =========================================================
// REGISTER USER
// =========================================================

async function registerUser() {

    clearMessages();


    // -----------------------------------------------------
    // Get form values
    // -----------------------------------------------------

    const userId = document.getElementById("userId").value.trim();
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const email = document.getElementById("email").value.trim();
    const dateOfBirth = document.getElementById("dateOfBirth").value;
    const departmentId = document.getElementById("departmentId").value.trim();


    // -----------------------------------------------------
    // Basic validation
    // -----------------------------------------------------

    if (!userId ||
        !firstName ||
        !lastName ||
        !email ||
        !dateOfBirth ||
        !departmentId) {

        showError("Please complete all required fields.");

        return;
    }


    // -----------------------------------------------------
    // Validate email
    // -----------------------------------------------------

    if (!isValidEmail(email)) {

        showError("Please enter a valid email address.");

        return;
    }


    // -----------------------------------------------------
    // Validate user ID
    // -----------------------------------------------------

    if (userId.length < 3) {

        showError("User ID must contain at least 3 characters.");

        return;
    }


    // -----------------------------------------------------
    // Disable button
    // -----------------------------------------------------

    const registerButton =
        document.getElementById("registerButton");

    if (registerButton) {

        registerButton.disabled = true;

        registerButton.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> Creating Account...';
    }


    try {

        // =================================================
        // STEP 1: CREATE USER
        // =================================================

        const userResponse = await fetch(
            `${API_BASE_URL}/user/create`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    userId: userId,
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    dateOfBirth: dateOfBirth,
                    departmentId: departmentId

                })
            }
        );


        // -------------------------------------------------
        // Handle user creation error
        // -------------------------------------------------

        if (!userResponse.ok) {

            let errorMessage =
                "Unable to create your account.";

            try {

                const errorData =
                    await userResponse.json();

                if (errorData.message) {
                    errorMessage = errorData.message;
                }

            } catch (error) {

                // Backend may not return JSON.
                // Humanity continues.
            }

            throw new Error(errorMessage);
        }


        const createdUser =
            await userResponse.json();


        // =================================================
        // STEP 2: CREATE REGISTER RECORD
        // =================================================

        const registerResponse = await fetch(
            `${API_BASE_URL}/register/create`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    registrarId: userId,
                    email: email,
                    dateRegistered:
                        new Date().toISOString().split("T")[0]

                })
            }
        );


        // -------------------------------------------------
        // Handle registration record error
        // -------------------------------------------------

        if (!registerResponse.ok) {

            let errorMessage =
                "User was created, but registration could not be completed.";

            try {

                const errorData =
                    await registerResponse.json();

                if (errorData.message) {
                    errorMessage = errorData.message;
                }

            } catch (error) {

                // Ignore invalid/non-JSON response
            }

            throw new Error(errorMessage);
        }


        const registration =
            await registerResponse.json();


        // =================================================
        // SUCCESS
        // =================================================

        showSuccess(
            "Registration successful! Redirecting to login..."
        );


        // -------------------------------------------------
        // Store login information
        // -------------------------------------------------
        // Username = email
        // Password = userId
        //
        // We store only the username here.
        // Never store passwords in localStorage.
        // -------------------------------------------------

        localStorage.setItem(
            "registeredEmail",
            email
        );


        // -------------------------------------------------
        // Redirect to login
        // -------------------------------------------------

        setTimeout(function () {

            window.location.href = "index.html";

        }, 2000);


    } catch (error) {

        console.error(
            "Registration error:",
            error
        );

        showError(
            error.message ||
            "Something went wrong during registration."
        );


    } finally {

        // -------------------------------------------------
        // Re-enable button
        // -------------------------------------------------

        if (registerButton) {

            registerButton.disabled = false;

            registerButton.innerHTML =
                '<span>Register</span>' +
                '<i class="fa-solid fa-arrow-right"></i>';

        }

    }

}


// =========================================================
// EMAIL VALIDATION
// =========================================================

function isValidEmail(email) {

    const emailPattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return emailPattern.test(email);

}


// =========================================================
// SHOW ERROR
// =========================================================

function showError(message) {

    const errorElement =
        document.getElementById("registerError");

    if (errorElement) {

        errorElement.textContent = message;

        errorElement.classList.add("show");

    }

}


// =========================================================
// SHOW SUCCESS
// =========================================================

function showSuccess(message) {

    const successElement =
        document.getElementById("successMessage");

    if (successElement) {

        successElement.textContent = message;

        successElement.classList.add("show");

    }

}


// =========================================================
// CLEAR MESSAGES
// =========================================================

function clearMessages() {

    const errorElement =
        document.getElementById("registerError");

    const successElement =
        document.getElementById("successMessage");


    if (errorElement) {

        errorElement.textContent = "";

        errorElement.classList.remove("show");

    }


    if (successElement) {

        successElement.textContent = "";

        successElement.classList.remove("show");

    }

}