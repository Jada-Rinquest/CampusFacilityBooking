/* =========================================================
   LOGIN
========================================================= */

const loginForm = document.getElementById("loginForm");

const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");

const usernameError = document.getElementById("usernameError");
const passwordError = document.getElementById("passwordError");

const loginError = document.getElementById("loginError");

const loginButton = document.getElementById("loginButton");
const loginButtonText = document.getElementById("loginButtonText");
const loginButtonIcon = document.getElementById("loginButtonIcon");

const togglePassword = document.getElementById("togglePassword");

const API_URL = "http://localhost:8080";


/* =========================================================
   PASSWORD SHOW / HIDE
========================================================= */

togglePassword.addEventListener("click", function () {

    const isPassword =
        passwordInput.getAttribute("type") === "password";

    if (isPassword) {

        passwordInput.setAttribute("type", "text");

        togglePassword.innerHTML =
            '<i class="fa-solid fa-eye-slash"></i>';

        togglePassword.setAttribute(
            "aria-label",
            "Hide password"
        );

    } else {

        passwordInput.setAttribute("type", "password");

        togglePassword.innerHTML =
            '<i class="fa-solid fa-eye"></i>';

        togglePassword.setAttribute(
            "aria-label",
            "Show password"
        );
    }
});


/* =========================================================
   CLEAR ERRORS
========================================================= */

function clearErrors() {

    usernameError.textContent = "";
    passwordError.textContent = "";

    usernameInput.classList.remove("input-error");
    passwordInput.classList.remove("input-error");

    loginError.classList.remove("show");
}


/* =========================================================
   VALIDATE FORM
========================================================= */

function validateForm() {

    let valid = true;

    clearErrors();

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();


    /* Username */

    if (username === "") {

        usernameError.textContent =
            "Please enter your username.";

        usernameInput.classList.add("input-error");

        valid = false;
    }


    /* Password */

    if (password === "") {

        passwordError.textContent =
            "Please enter your password.";

        passwordInput.classList.add("input-error");

        valid = false;
    }


    return valid;
}


/* =========================================================
   BUTTON LOADING STATE
========================================================= */

function setLoading(isLoading) {

    if (isLoading) {

        loginButton.disabled = true;

        loginButtonText.textContent = "Signing in...";

        loginButtonIcon.className =
            "fa-solid fa-spinner fa-spin";

    } else {

        loginButton.disabled = false;

        loginButtonText.textContent = "Sign In";

        loginButtonIcon.className =
            "fa-solid fa-arrow-right";
    }
}


/* =========================================================
   LOGIN FORM SUBMISSION
========================================================= */

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    if (!validateForm()) {
        return;
    }


    const username = usernameInput.value.trim();
    const password = passwordInput.value;


    setLoading(true);


    try {

        const response = await fetch(
            `${API_URL}/login/authenticate`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username: username,
                    password: password
                })
            }
        );


        /* =================================================
           INVALID LOGIN
        ================================================= */

        if (response.status === 401) {

            loginError.classList.add("show");

            setLoading(false);

            return;
        }


        /* =================================================
           OTHER SERVER ERROR
        ================================================= */

        if (!response.ok) {

            throw new Error(
                "Unable to connect to the server."
            );
        }


        /* =================================================
           SUCCESSFUL LOGIN
        ================================================= */

        const login = await response.json();


        /*
         * Store login information temporarily.
         *
         * This allows the dashboard to know which
         * account has logged in.
         */

        sessionStorage.setItem(
            "loginId",
            login.loginId
        );

        sessionStorage.setItem(
            "registrarId",
            login.registrarId
        );

        sessionStorage.setItem(
            "username",
            login.username
        );


        /*
         * Redirect to the dashboard.
         *
         * For now this goes to the staff dashboard.
         * We will later determine the user's actual
         * role using UserRole.
         */

        window.location.href = "staff-dashboard.html";

    } catch (error) {

        console.error("Login error:", error);

        loginError.classList.add("show");

        loginError.querySelector("span").textContent =
            "Unable to connect to the server. Please try again.";

    } finally {

        setLoading(false);
    }

});