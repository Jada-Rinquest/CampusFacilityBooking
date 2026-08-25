document.addEventListener("DOMContentLoaded", () => {

    // =========================================================
    // ELEMENTS
    // =========================================================

    const loginForm = document.getElementById("loginForm");

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    const usernameError =
        document.getElementById("usernameError");

    const passwordError =
        document.getElementById("passwordError");

    const loginError =
        document.getElementById("loginError");

    const togglePassword =
        document.getElementById("togglePassword");

    const forgotPassword =
        document.getElementById("forgotPassword");

    const loginButton =
        document.getElementById("loginButton");

    const loginButtonText =
        document.getElementById("loginButtonText");

    const loginButtonIcon =
        document.getElementById("loginButtonIcon");


    // =========================================================
    // PASSWORD VISIBILITY
    // =========================================================

    if (togglePassword) {

        togglePassword.addEventListener("click", () => {

            const isPassword =
                passwordInput.type === "password";

            passwordInput.type =
                isPassword ? "text" : "password";


            const icon =
                togglePassword.querySelector("i");

            if (icon) {

                icon.classList.toggle(
                    "fa-eye",
                    !isPassword
                );

                icon.classList.toggle(
                    "fa-eye-slash",
                    isPassword
                );

            }


            togglePassword.setAttribute(
                "aria-label",
                isPassword
                    ? "Hide password"
                    : "Show password"
            );

        });

    }


    // =========================================================
    // CLEAR ERRORS
    // =========================================================

    function clearErrors() {

        usernameError.textContent = "";
        passwordError.textContent = "";

        usernameInput.classList.remove("input-error");
        passwordInput.classList.remove("input-error");

        loginError.classList.remove("show");

    }


    // =========================================================
    // SHOW FIELD ERROR
    // =========================================================

    function showFieldError(input, errorElement, message) {

        input.classList.add("input-error");

        errorElement.textContent = message;

    }


    // =========================================================
    // SHOW GENERAL LOGIN ERROR
    // =========================================================

    function showLoginError(message) {

        const errorText =
            loginError.querySelector("span");

        if (errorText) {
            errorText.textContent = message;
        }

        loginError.classList.add("show");

    }


    // =========================================================
    // BUTTON LOADING
    // =========================================================

    function setLoading(loading) {

        if (!loginButton) {
            return;
        }

        loginButton.disabled = loading;

        if (loading) {

            loginButtonText.textContent =
                "Signing In...";

            loginButtonIcon.className =
                "fa-solid fa-spinner fa-spin";

        } else {

            loginButtonText.textContent =
                "Sign In";

            loginButtonIcon.className =
                "fa-solid fa-arrow-right";

        }

    }


    // =========================================================
    // LOGIN
    // =========================================================

    loginForm.addEventListener("submit", async (event) => {

        event.preventDefault();

        clearErrors();


        // =====================================================
        // GET VALUES
        // =====================================================

        const email =
            usernameInput.value.trim();

        const userId =
            passwordInput.value.trim();


        // =====================================================
        // VALIDATION
        // =====================================================

        let hasError = false;


        if (!email) {

            showFieldError(
                usernameInput,
                usernameError,
                "Please enter your email address."
            );

            hasError = true;

        }


        if (!userId) {

            showFieldError(
                passwordInput,
                passwordError,
                "Please enter your User ID."
            );

            hasError = true;

        }


        if (hasError) {
            return;
        }


        // =====================================================
        // EMAIL FORMAT
        // =====================================================

        const emailPattern =
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/;


        if (!emailPattern.test(email)) {

            showFieldError(
                usernameInput,
                usernameError,
                "Please enter a valid email address."
            );

            return;

        }


        // =====================================================
        // START LOGIN
        // =====================================================

        setLoading(true);


        try {

            /*
             * The current backend does not have a login endpoint.
             *
             * We therefore use:
             *
             * GET /user/read/{userId}
             *
             * The User ID acts as the password.
             */

            const response = await fetch(
                `http://localhost:8080/user/read/${encodeURIComponent(userId)}`
            );


            // =================================================
            // USER NOT FOUND
            // =================================================

            if (response.status === 404) {

                showLoginError(
                    "Invalid email or User ID."
                );

                return;
            }


            // =================================================
            // SERVER ERROR
            // =================================================

            if (!response.ok) {

                showLoginError(
                    "Unable to connect to the login service."
                );

                return;
            }


            // =================================================
            // GET USER
            // =================================================

            const user =
                await response.json();


            console.log(
                "User returned from backend:",
                user
            );


            // =================================================
            // CHECK EMAIL
            // =================================================

            if (
                !user.email ||
                user.email.toLowerCase() !== email.toLowerCase()
            ) {

                showLoginError(
                    "Invalid email or User ID."
                );

                return;
            }


            // =================================================
            // LOGIN SUCCESSFUL
            // =================================================

            console.log(
                "Login successful:",
                user
            );


            // =================================================
            // SAVE LOGGED-IN USER
            // =================================================

            const loggedInUser = {

                userId: user.userId,

                firstName: user.firstName,

                lastName: user.lastName,

                email: user.email,

                dateOfBirth: user.dateOfBirth,

                departmentId: user.departmentId

            };


            /*
             * Remember Me determines whether the user
             * information survives after the browser closes.
             */

            const rememberMe =
                document.getElementById("rememberMe");


            if (
                rememberMe &&
                rememberMe.checked
            ) {

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


            // =================================================
            // REDIRECT
            // =================================================

            loginButtonText.textContent =
                "Success!";

            loginButtonIcon.className =
                "fa-solid fa-check";


            setTimeout(() => {

                window.location.href =
                    "student-dashboard.html";

            }, 700);


        } catch (error) {

            console.error(
                "Login error:",
                error
            );


            showLoginError(
                "Unable to connect to the server. Make sure Spring Boot is running."
            );

        } finally {

            setTimeout(() => {
                setLoading(false);
            }, 700);

        }

    });


    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    if (forgotPassword) {

        forgotPassword.addEventListener("click", (event) => {

            event.preventDefault();

            alert(
                "Your password is your User ID. Please contact the campus administrator if you have forgotten it."
            );

        });

    }

});